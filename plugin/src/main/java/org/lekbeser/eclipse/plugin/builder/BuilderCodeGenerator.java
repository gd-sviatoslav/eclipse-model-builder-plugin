package org.lekbeser.eclipse.plugin.builder;

import static org.lekbeser.eclipse.plugin.builder.Util.getName;
import static org.lekbeser.eclipse.plugin.builder.Util.getType;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IBuffer;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMethod;
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

    public static void generate(final BuilderOptions opts) {
        try {
            // removeOldClassConstructor(opts.getCompilationUnit());
            // removeOldBuilderClass(opts.getCompilationUnit());

            IBuffer buffer = opts.getCompilationUnit().getBuffer();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            pw.println();
            pw.println("public static class Builder {");

            IType clazz = opts.getEnclosingType();
            int pos = clazz.getSourceRange().getOffset() + clazz.getSourceRange().getLength() - 1;
            createFieldDeclarations(pw, opts.getFields());
            createBuilderMethods(pw, opts.getFields());
            
            if (null != opts) { // todo now sl: remove fake
                createPrivateBuilderConstructor(pw, clazz, opts.getFields());
                pw.println("}");
            } else {
                createClassBuilderConstructor(pw, clazz, opts.getFields());
                pw.println("}");
                createClassConstructor(pw, clazz, opts.getFields());
            }

            if (opts.isFormatSourceCode()) {
                pw.println();
                buffer.replace(pos, 0, sw.toString());
                String builderSource = buffer.getContents();

                TextEdit text = ToolFactory.createCodeFormatter(null).format(CodeFormatter.K_COMPILATION_UNIT, builderSource, 0, builderSource.length(), 0, "\n");
                if (text != null) {
                    Document simpleDocument = new Document(builderSource);
                    text.apply(simpleDocument);
                    buffer.setContents(simpleDocument.get());
                } 
            } else {
                buffer.replace(pos, 0, sw.toString());
            }
        } catch (JavaModelException e) {
            e.printStackTrace();
        } catch (MalformedTreeException e) {
            e.printStackTrace();
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    private static void removeOldBuilderClass(ICompilationUnit cu) throws JavaModelException {
        for (IType type : cu.getTypes()[0].getTypes()) {
            if (type.getElementName().equals("Builder") && type.isClass()) {
                type.delete(true, null);
                break;
            }
        }
    }

    private static void removeOldClassConstructor(ICompilationUnit cu) throws JavaModelException {
        for (IMethod method : cu.getTypes()[0].getMethods()) {
            if (method.isConstructor() && method.getParameterTypes().length == 1 && method.getParameterTypes()[0].equals("QBuilder;")) {
                method.delete(true, null);
                break;
            }
        }
    }

    private static void createCopyConstructor(PrintWriter pw, IType clazz, List<IField> fields) {
        String clazzName = clazz.getElementName();
        pw.println("public Builder(){}");
        pw.println("public Builder(" + clazzName + " bean){");
        for (IField field : fields) {
            pw.println("this." + getName(field) + "=bean." + getName(field) + ";");
        }
        pw.println("}");

    }

    private static void createClassConstructor(PrintWriter pw, IType clazz, List<IField> fields) throws JavaModelException {
        String clazzName = clazz.getElementName();
        pw.println(clazzName + "(Builder builder){");
        for (IField field : fields) {
            pw.println("this." + getName(field) + "=builder." + getName(field) + ";");
        }
        pw.println("}");
    }

    private static void createClassBuilderConstructor(PrintWriter pw, IType clazz, List<IField> fields) {
        String clazzName = clazz.getElementName();
        pw.println("public " + clazzName + " build(){");
        pw.println("return new " + clazzName + "(this);\n}");
    }

    private static void createPrivateBuilderConstructor(PrintWriter pw, IType clazz, List<IField> fields) {
        String clazzName = clazz.getElementName();
        String clazzVariable = clazzName.substring(0, 1).toLowerCase() + clazzName.substring(1);
        pw.println("public " + clazzName + " build(){");
        pw.println(clazzName + " " + clazzVariable + "=new " + clazzName + "();");
        for (IField field : fields) {
            String name = getName(field);
            pw.println(clazzVariable + "." + name + "=" + name + ";");
        }
        pw.println("return " + clazzVariable + ";\n}");
    }

    private static void createBuilderMethods(PrintWriter pw, List<IField> fields) throws JavaModelException {
        for (IField field : fields) {
            String fieldName = getName(field);
            String fieldType = getType(field);
            String baseName = getFieldBaseName(fieldName);
            String parameterName = baseName;
            pw.println("public Builder " + baseName + "(" + fieldType + " " + parameterName + ") {");
            pw.println("this." + fieldName + "=" + parameterName + ";");
            pw.println("return this;\n}");
        }
    }

    private static String getFieldBaseName(String fieldName) {
        IJavaProject javaProject = JavaCore.create(ResourcesPlugin.getWorkspace().getRoot().getProject());
        return NamingConventions.getBaseName(NamingConventions.VK_INSTANCE_FIELD, fieldName, javaProject);
    }

    private static void createFieldDeclarations(PrintWriter pw, List<IField> fields) throws JavaModelException {
        for (IField field : fields) {
            pw.println("private " + getType(field) + " " + getName(field) + ";");
        }
    }

}
