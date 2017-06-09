package org.universAAL.ucc.configuration.configdefinitionregistry.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;
import org.universAAL.ucc.configuration.configdefinitionregistry.ConfigurationDefinitionRegistryImpl;
import org.universAAL.ucc.configuration.configdefinitionregistry.interfaces.ConfigurationDefinitionRegistry;

/**
 * 
 * This activator registers the config definition storage. Bundles could
 * register their configuration definition here. Every bundle which has
 * registered his configuration definition here could be configured with the
 * configurator.
 * 
 * @author Sebastian.Schoebinge
 * 
 */

public class Activator implements BundleActivator {

	private static BundleContext context;
	private static ModuleContext moduleContext;

	public static ModuleContext getContext() {
		return moduleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {

		Activator.moduleContext = uAALBundleContainer.THE_CONTAINER.registerModule(new Object[] { context });

		Activator.context = bundleContext;
		context.registerService(ConfigurationDefinitionRegistry.class.getName(),
				new ConfigurationDefinitionRegistryImpl(), null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

}
