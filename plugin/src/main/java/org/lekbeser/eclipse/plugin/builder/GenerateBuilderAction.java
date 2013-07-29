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
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorActionDelegate;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

public class GenerateBuilderAction extends Action implements IEditorActionDelegate, IWorkbenchWindowActionDelegate {

    private IEditorPart editor;

    public void run(final IAction action) {
        IWorkingCopyManager manager = JavaUI.getWorkingCopyManager();
        IEditorInput editorInput = editor.getEditorInput();
        try {
            ITextSelection textSelection = (ITextSelection) editor.getSite().getSelectionProvider().getSelection();
            int offset = textSelection.getOffset();
            ITypeRoot root = (ITypeRoot) JavaUI.getEditorInputJavaElement(editorInput);
            IJavaElement elt = root.getElementAt(offset);
            IType enclosingType = (IType) elt.getAncestor(IJavaElement.TYPE);

            manager.connect(editorInput);
            ICompilationUnit workingCopy = manager.getWorkingCopy(editorInput);

            BuilderDialog dialog = new BuilderDialog(new Shell());
            dialog.show(workingCopy, enclosingType);

            synchronized (workingCopy) {
                workingCopy.reconcile(ICompilationUnit.NO_AST, false, null, null);
            }

        } catch (JavaModelException e) {
            e.printStackTrace();
        } catch (CoreException e) {
            e.printStackTrace();
        } catch (Throwable e) {
            Util.showError("Error: {0}", e);
        } finally {
            manager.disconnect(editorInput);
        }

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