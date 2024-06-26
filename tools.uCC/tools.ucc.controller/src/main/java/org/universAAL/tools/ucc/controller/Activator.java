package org.universAAL.tools.ucc.controller;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.universAAL.middleware.container.ModuleContext;
import org.universAAL.middleware.container.osgi.OSGiContainer;
import org.universAAL.middleware.managers.api.SpaceManager;
import org.universAAL.tools.ucc.api.IDeinstaller;
import org.universAAL.tools.ucc.api.IInstaller;
import org.universAAL.tools.ucc.api.impl.Deinstaller;
import org.universAAL.tools.ucc.api.impl.Installer;
import org.universAAL.middleware.managers.api.DeployManager;

/**
 * Activator of ucc.controller
 *
 * @author Shanshan Jiang
 *
 */
public class Activator implements BundleActivator {

	private static BundleContext context;
	private static DeployManager deployManager;
	private static SpaceManager spaceManager;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		Activator.context = bundleContext;
		context.registerService(IInstaller.class.getName(), new Installer(), null);
		context.registerService(IDeinstaller.class.getName(), new Deinstaller(), null);
		getManagers();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {

	}

	private void getManagers() {
		System.out.println("[ucc.controller.Activator.getManagers]");
		ModuleContext moduleContext = OSGiContainer.THE_CONTAINER.registerModule(new Object[] { context });

		if (moduleContext == null) {
			System.out.println("[ucc.controller.Activator.getManagers] moduleContext is null!");
			return;
		} else {
			System.out.println("[ucc.controller.Activator.getManagers] moduleContext exists!");
		}
		// Object[] managers = (Object[])
		// moduleContext.getContainer().fetchSharedObject(moduleContext,new
		// Object[]{SpaceManager.class.getName().toString()});
		Object managers = moduleContext.getContainer().fetchSharedObject(moduleContext,
				new Object[] { SpaceManager.class.getName().toString() });
		if (managers != null) {
			// moduleContext.logDebug("[ucc.controller.Activator.getManagers]
			// SpaceManagers found...",
			// null, null);
			System.out.println("[ucc.controller.Activator.getManagers] Managers found...");
			if (managers instanceof SpaceManager) {
				spaceManager = (SpaceManager) managers;
			}

			else {
				// moduleContext.logWarn("[ucc.controller.Activator.getManagers]
				// No SpaceManagers found",
				// null, null);
				System.out.println("[ucc.controller.Activator.getManagers]No SpaceManagers found");
			}
		} else {
			// moduleContext.logWarn("[ucc.controller.Activator.getManagers] No
			// SpaceManagers found",
			// null, null);
			System.out.println("[ucc.controller.Activator.getManagers]No Managers found");
		}

		Object refs = moduleContext.getContainer().fetchSharedObject(moduleContext,
				new Object[] { DeployManager.class.getName().toString() });
		if (refs != null) {
			deployManager = (DeployManager) refs;
		} else {
			// moduleContext.logWarn("[ucc.controller.Activator.getManagers] No
			// DeployManager found",
			// null, null);
			System.out.println("[ucc.controller.Activator.getManagers] No DeployManager found");
		}
	}

	public static SpaceManager getSpaceManager() {
		return spaceManager;
	}

	public static DeployManager getDeployManager() {
		return deployManager;
	}

}
