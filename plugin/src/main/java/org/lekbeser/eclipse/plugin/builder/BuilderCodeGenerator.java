package org.lekbeser.eclipse.plugin.builder;

import static org.lekbeser.eclipse.plugin.builder.Util.getName;
import static org.lekbeser.eclipse.plugin.builder.Util.getType;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.NamingConventions;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class BuilderCodeGenerator {

    public static String line(final String pattern, Object... params) {
        return MessageFormat.format(pattern, params);
    }

    public static void generate(final BuilderOptions opts) {
        try {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            generateBuilderCode(pw, opts);

            final IType enclosingType = opts.getEnclosingType();
            final IBuffer buffer = opts.getCompilationUnit().getBuffer();
            final int pos = enclosingType.getSourceRange().getOffset() + enclosingType.getSourceRange().getLength() - 1;
            if (opts.isFormatSourceCode()) {
                buffer.replace(pos, 0, sw.toString());
                String builderSource = buffer.getContents();

                TextEdit text = ToolFactory.createCodeFormatter(null).format(CodeFormatter.K_COMPILATION_UNIT, builderSource, 0,
                        builderSource.length(), 0, "\n");
                if (text != null) {
                    Document simpleDocument = new Document(builderSource);
                    text.apply(simpleDocument);
                    buffer.setContents(simpleDocument.get());
                }
            } else {
                buffer.replace(pos, 0, sw.toString());
            }
        } catch (JavaModelException e) {
            Activator.error(e, "Could not generate code.");
        } catch (MalformedTreeException e) {
            Activator.error(e, "Could not generate code.");
        } catch (BadLocationException e) {
            Activator.error(e, "Could not generate code.");
        }
    }

    private static void generateBuilderCode(final PrintWriter pw, final BuilderOptions opts) {
        final String typeName = opts.getTypeName();
        pw.println();
        pw.println(line("public static {0}Builder builder() '{'", typeName));
        pw.println(line("    return new {0}Builder();", typeName));
        pw.println("}");

        pw.println();
        pw.println(line("public static class {0}Builder '{'", typeName));
        for (IField field : opts.getFields()) { // fields
            pw.println(line("    private {0} {1};", getType(field), getName(field)));
        }
        for (IField field : opts.getFields()) { // builder methods
            String fieldName = getName(field);
            String baseName = getFieldBaseName(fieldName);
            pw.println();
            pw.println(line("    public {0}Builder {1}({2} {3}) '{'", typeName, baseName, getType(field), fieldName));
            pw.println(line("        this.{0} = {0};", fieldName));
            pw.println("        return this;");
            pw.println("    }");
        }

        pw.println(); // 'from'-method
        pw.println(line("    public {0}Builder from({0} origin) '{'", typeName));
        for (IField field : opts.getFields()) { // builder methods
            String fieldName = getName(field);
            String baseName = getFieldBaseName(fieldName);
            pw.println(line("        this.{0}(origin.{1});", baseName, fieldName));
        }
        pw.println("        return this;");
        pw.println("    }");

        pw.println(); // 'build'-method
        pw.println(line("    public {0} build() '{'", typeName));
        pw.println(line("        {0} m = new {0}();", typeName));
        for (IField field : opts.getFields()) {
            String fieldName = getName(field);
            pw.println(line("        m.{0} = this.{0};", fieldName));
        }
        pw.println("        return m;");
        pw.println("    }");
        pw.println("}");
        pw.println();
    }

    private static String getFieldBaseName(String fieldName) {
        IJavaProject javaProject = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot().getProject());
        return NamingConventions.getBaseName(NamingConventions.VK_INSTANCE_FIELD, fieldName, javaProject);
    }

}
