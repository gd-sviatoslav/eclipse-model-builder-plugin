package org.lekbeser.eclipse.plugin.builder;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

public final class Util {

    private Util() {
    }

    public static void showError(String template, Object... params) {
        IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        MessageDialog.openError(activeWindow.getShell(), "Error", MessageFormat.format(template, params));
    }

    public static void showMessage(String template, Object... params) {
        IWorkbenchWindow activeWindow = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        MessageDialog.openInformation(activeWindow.getShell(), "Message", MessageFormat.format(template, params));
    }

    public static String getName(final IField field) {
        return field.getElementName();
    }

    public static String getType(final IField field) {
        try {
            return Signature.toString(field.getTypeSignature());
        } catch (IllegalArgumentException e) {
            Activator.error(e, "Could not generate code.");
        } catch (JavaModelException e) {
            Activator.error(e, "Could not generate code.");
        }
        return null;
    }

    public static List<IField> findAllFields(IType enclosingType) {
        List<IField> fields = new ArrayList<IField>();
        try {
            for (IField field : enclosingType.getFields()) {
                int flags = field.getFlags();
                boolean notStatic = !Flags.isStatic(flags);
                if (notStatic) {
                    fields.add(field);
                }
            }
        } catch (JavaModelException e) {
            Activator.error(e, "Could not generate code.");
        }
        return fields;
    }

}
