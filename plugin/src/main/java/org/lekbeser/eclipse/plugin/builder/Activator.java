package org.lekbeser.eclipse.plugin.builder;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class Activator extends AbstractUIPlugin {

    public static final String PLUGIN_ID = "org.lekbeser.eclipse.model.builder";

    private static Activator plugin;

    public Activator() {
    }

    @Override
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }

    @Override
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }

    /**
     * Returns the shared instance.
     * 
     * @return the shared instance
     */
    public static Activator getDefault() {
        return plugin;
    }

    /**
     * Returns an image descriptor for the image file at the given plug-in relative path.
     * 
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }

    public static void info(String msg, Object... params) {
        if (null != plugin) {
            plugin.getLog().log(new Status(IStatus.INFO, PLUGIN_ID, MessageFormat.format(msg, params)));
        } else {
            Util.showMessage(msg, params);
        }
    }

    public static void warn(String msg, Object... params) {
        if (null != plugin) {
            plugin.getLog().log(new Status(IStatus.WARNING, PLUGIN_ID, MessageFormat.format(msg, params)));
        } else {
            Util.showMessage(msg, params);
        }
    }

    public static void error(String msg, Object... params) {
        if (null != plugin) {
            plugin.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, MessageFormat.format(msg, params)));
        } else {
            Util.showError(msg, params);
        }
    }

    public static void error(Throwable t, String msg, Object... params) {
        if (null != plugin) {
            plugin.getLog().log(new Status(IStatus.ERROR, PLUGIN_ID, IStatus.OK, MessageFormat.format(msg, params), t));
        } else {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            Util.showError(msg + "\n\n" + sw.toString(), params);
        }
    }

}
