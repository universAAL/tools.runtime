package org.universAAL.ucc.configuration.storage.internal;

import java.io.File;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;

import org.universAAL.ucc.configuration.storage.ConfigurationInstancesStorageImpl;
import org.universAAL.ucc.configuration.storage.interfaces.ConfigurationInstancesStorage;

/**
 * 
 * This activator class registers the configuration instances storage service.
 * 
 * @author Sebastian.Schoebinge
 * 
 */

public class Activator implements BundleActivator {

	public static BundleContext context;
	private static ModuleContext mc;
	private static File tmpConfigFiles;

	public static ModuleContext getContext() {
		return mc;
	}

	public void start(BundleContext bundleContext) throws Exception {
		context = bundleContext;
		mc = uAALBundleContainer.THE_CONTAINER.registerModule(new Object[] { context });

		tmpConfigFiles = new File(new File(mc.getConfigHome().getParent(), "uCC"), "tmpConfigFiles");
		context.registerService(ConfigurationInstancesStorage.class.getName(), new ConfigurationInstancesStorageImpl(),
				null);
	}

	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	public static File getTmpConfigFiles() {
		return tmpConfigFiles;
	}
}
