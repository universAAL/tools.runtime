package org.universAAL.ucc.service.api;

import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface IServiceManagement {

	public String getInstalledServices();

	public String getInstalledUnitsForService(String serviceId);

	public List<String> getInstalledApps(String serviceId);

	public void addUserIDToMenuEntry(String serviceId, String appId);

	public boolean isServiceId(String serviceId);
}
