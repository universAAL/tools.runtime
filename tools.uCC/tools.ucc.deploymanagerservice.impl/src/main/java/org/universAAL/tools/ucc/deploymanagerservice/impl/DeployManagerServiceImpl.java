package org.universAAL.tools.ucc.deploymanagerservice.impl;

import org.universAAL.tools.ucc.deploymanagerservice.DeployManagerService;

/**
 * Implemenation of the DeployManager Web service
 *
 * @author sji
 * @modified by Nicole Merkle
 *
 */

public class DeployManagerServiceImpl implements DeployManagerService {

	// private IInstaller installer;
	public void install(String sessionKey, String serviceId, String serviceLink) {
		System.out.println("[DeployManagerServiceImpl] Install with sessionKey: " + sessionKey + " for Service-ID: "
				+ serviceId + " and URL: " + serviceLink);
		Activator.getFrontend().installService(sessionKey, serviceId, serviceLink);
		// TODO what check should be done with the sessionKey?
	}

	public void update(String sessionKey, String serviceId, String serviceLink) {
		System.out.println(
				"[DeployManagerService] Update with sessionKey: " + sessionKey + " for serviceId: " + serviceId);
		Activator.getFrontend().update(sessionKey, serviceId, serviceLink);
	}

	public void uninstall(String sessionKey, String serviceId) {
		System.out.println("[DeployManagerServiceImpl] uninstall of serviceId: " + serviceId);
		Activator.getFrontend().uninstallService(sessionKey, serviceId);
	}

	public String getInstalledServices(String sessionKey) {
		String installedServices = Activator.getFrontend().getInstalledServices(sessionKey);
		return installedServices;
	}

	public String getInstalledUnitsForService(String sessionKey, String serviceId) {
		String units = Activator.getFrontend().getInstalledUnitsForService(sessionKey, serviceId);
		return units;
	}

	public String getSpaceProfile() {
		return null;
	}

	public String getUserProfile(String sessionKey) {
		return null;
	}

	public String getSessionKey(String userName, String password) {
		String sessionKey = Activator.getFrontend().getSessionKey(userName, password);
		return sessionKey;
	}

	public void update(String arg0, String arg1) {
		// TODO Auto-generated method stub

	}

}