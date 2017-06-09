package org.universAAL.ucc.frontend.api;

import org.universAAL.ucc.windows.DeinstallWindow;

public interface IFrontend {
	public boolean installService(String sessionkey, String serviceId, String serviceLink);

	public void uninstallService(String sessionkey, String serviceId);

	public void update(String sessionKey, String serviceId, String serviceLink);

	public String getInstalledServices(String sessionKey);

	public String getInstalledUnitsForService(String sessionKey, String serviceId);

	public String getSessionKey(String username, String password);

	public void startUCC();
}
