package org.universAAL.ucc.configuration.internal;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.uAALBundleContainer;

import org.universAAL.middleware.container.utils.ModuleConfigHome;
import org.universAAL.ucc.configuration.configdefinitionregistry.interfaces.ConfigurationDefinitionRegistry;

public class Activator implements BundleActivator {
	private static ModuleContext moduleContext;
	public static BundleContext bc;

    ConfigurationDefinitionRegistry configReg;
    private static ModuleConfigHome moduleConfigHome;
    
	public static ModuleContext getContext() {
	    return moduleContext;
    }

    public void start(BundleContext context) throws Exception {
    	moduleConfigHome = new ModuleConfigHome("uCC", "tmpConfigFiles");
    	
		Activator.moduleContext = uAALBundleContainer.THE_CONTAINER
		.registerModule(new Object[] { context });
    }

    public void stop(BundleContext arg0) throws Exception {
	// TODO Auto-generated method stub

    }

	public static ModuleConfigHome getModuleConfigHome() {
		return moduleConfigHome;
	}

	public static void setModuleConfigHome(ModuleConfigHome moduleConfigHome) {
		Activator.moduleConfigHome = moduleConfigHome;
	}
    
    

}
