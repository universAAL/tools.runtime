package org.universAAL.tools.ucc.configuration.internal;

import java.io.File;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.tools.ucc.configuration.configdefinitionregistry.interfaces.ConfigurationDefinitionRegistry;

public class Activator implements BundleActivator {
	private static ModuleContext mc;
	public static BundleContext bc;

	ConfigurationDefinitionRegistry configReg;
	private static File tmpConfigFiles;

	public static ModuleContext getContext() {
		return mc;
	}

	public void start(BundleContext context) throws Exception {
		bc = context;
		mc = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });

		tmpConfigFiles = new File(new File(mc.getConfigHome().getParent(), "uCC"), "tmpConfigFiles");
	}

	public void stop(BundleContext arg0) throws Exception {
	}

	public static File getTmpConfigFiles() {
		return tmpConfigFiles;
	}
}
