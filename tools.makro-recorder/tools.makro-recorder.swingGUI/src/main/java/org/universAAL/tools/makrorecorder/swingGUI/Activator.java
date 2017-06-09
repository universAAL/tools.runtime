package org.universAAL.tools.makrorecorder.swingGUI;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;

import org.universAAL.middleware.serialization.MessageContentSerializer;

import org.universAAL.tools.makrorecorder.osgi.MakroRecorder;

/**
 *
 * @author maxim djakow
 */
public class Activator implements BundleActivator {

	private static BundleContext bundleContext = null;
	private static ModuleContext moduleContext = null;

	private static MessageContentSerializer serializer = null;

	private static MakroRecorder makroRecorder = null;

	public void start(BundleContext context) throws Exception {
		bundleContext = context;
		moduleContext = uAALBundleContainer.THE_CONTAINER.registerModule(new Object[] { bundleContext });
		serializer = (MessageContentSerializer) context
				.getService(context.getServiceReference(MessageContentSerializer.class.getName()));

		new MakroRecorderSwingGUI(makroRecorder = new MakroRecorder(moduleContext)).setVisible(true);
	}

	public void stop(BundleContext arg0) throws Exception {
		// TODO Auto-generated method stub
	}

	public static BundleContext getBundleContext() {
		return bundleContext;
	}

	public static ModuleContext getModuleContext() {
		return moduleContext;
	}

	public static MessageContentSerializer getSerializer() {
		return serializer;
	}

	public static MakroRecorder getMakroRecorder() {
		return makroRecorder;
	}

}
