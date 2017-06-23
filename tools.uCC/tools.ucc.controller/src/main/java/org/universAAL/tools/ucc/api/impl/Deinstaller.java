package org.universAAL.tools.ucc.api.impl;

import org.universAAL.middleware.managers.api.DeployManager;
import org.universAAL.middleware.managers.api.InstallationResultsDetails;
import org.universAAL.tools.ucc.api.IDeinstaller;
import org.universAAL.tools.ucc.controller.Activator;

public class Deinstaller implements IDeinstaller {

	public InstallationResultsDetails requestToUninstall(String serviceId, String id) {
		DeployManager deployManager = Activator.getDeployManager();
		if (deployManager == null) {
			System.out.println("[Deinstaller.requestToUninstall] DeployManager is null!");
			return null;
		}

		InstallationResultsDetails results = deployManager.requestToUninstall(serviceId, id);
		System.out.println(
				"[Deinstaller.requestToInstall] the uninstallation results: " + results.getGlobalResult().toString());
		return results;

	}

}
