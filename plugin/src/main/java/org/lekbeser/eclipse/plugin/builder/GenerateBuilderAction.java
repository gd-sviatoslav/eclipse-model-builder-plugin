package org.lekbeser.eclipse.plugin.builder;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.IWorkingCopyManager;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.texteditor.AbstractTextEditor;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.lekbeser.eclipse.plugin.builder.BuilderOptions.BuilderOptionsBuilder;

public class GenerateBuilderAction extends Action implements IEditorActionDelegate, IWorkbenchWindowActionDelegate {

    private IEditorPart editor;

    public void run(final IAction action) {
        final BuilderOptionsBuilder options = BuilderOptions.builder();

        IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
        IEditorInput editorInput = editor.getEditorInput();
        try {
            ITextSelection textSelection = (ITextSelection) editor.getSite().getSelectionProvider().getSelection();
            ITypeRoot root = (ITypeRoot) JavaUI.getEditorInputJavaElement(editorInput);
            IJavaElement elt = root.getElementAt(textSelection.getOffset());
            IType enclosingType = (IType) elt.getAncestor(IJavaElement.TYPE);

            options.initialOffset(getColumnOffset(editor, textSelection));
            options.enclosingType(enclosingType);
            options.fullTypeName(enclosingType.getFullyQualifiedName());
            options.typeName(enclosingType.getElementName());

            manager.connect(editorInput);
            ICompilationUnit workingCopy = manager.getWorkingCopy(editorInput);
            options.compilationUnit(workingCopy);

            BuilderDialog dialog = new BuilderDialog(new Shell());
            dialog.show(options);

            synchronized (workingCopy) {
                workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, null);
            }

        } catch (JavaModelException e) {
            Activator.error(e, "Could not generate code.");
        } catch (CoreException e) {
            Activator.error(e, "Could not generate code.");
        } catch (Throwable e) {
            Activator.error(e, "Could not generate code.");
        } finally {
            manager.disconnect(editorInput);
        }
    }

    private static int getColumnOffset(IEditorPart editor, ITextSelection textSelection) {
        if (!(editor instanceof AbstractTextEditor)) {
            return 0;
        }
        final ITextEditor textEditor = (ITextEditor) editor;
        IDocumentProvider dp = textEditor.getDocumentProvider();
        IDocument doc = dp.getDocument(textEditor.getEditorInput());
        int line = textSelection.getStartLine();
        int column = 0;
        try {
            column = textSelection.getOffset() - doc.getLineOffset(line);
        } catch (BadLocationException e) {
            Activator.error(e, "Could not find cursor position.");
        }
        return column;
    }

    public void selectionChanged(IAction action, ISelection selection) {
    }

    public void setActiveEditor(IAction action, IEditorPart targetEditor) {
        this.editor = targetEditor;
    }

    public void dispose() {
    }

    public void init(IWorkbenchWindow window) {
        editor = window.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
    }

}