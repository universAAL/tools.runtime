package org.universAAL.tools.ucc.service.api;

public interface IServiceRegistration {

	public boolean unregisterService(String serviceId);

	public boolean registerApp(String sericeId, String appId, String entryName, String vendorURL, String serviceClass,
			String iconURL);

	public boolean registerBundle(String serviceId, String bundleId, String bundleVersion);

}
