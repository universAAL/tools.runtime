package org.universAAL.ucc.model;

import java.util.ArrayList;
import java.util.List;

public class RegisteredService {
	private String serviceId;
	private List<String> bundleId;
	private List<String> appId;
	private String bundleVersion;
	private String menuName;
	private String userID;
	private String provider;
	private String serviceClass;
	private String iconURL;
	
	public RegisteredService() {
		bundleId = new ArrayList<String>();
		appId = new ArrayList<String>();
	}
	
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public List<String> getBundleId() {
		return bundleId;
	}
	public void setBundleId(List<String> bundleId) {
		this.bundleId = bundleId;
	}
	public List<String> getAppId() {
		return appId;
	}
	public void setAppId(List<String> appId) {
		this.appId = appId;
	}
	public String getBundleVersion() {
		return bundleVersion;
	}
	public void setBundleVersion(String bundleVersion) {
		this.bundleVersion = bundleVersion;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getServiceClass() {
		return serviceClass;
	}

	public void setServiceClass(String serviceClass) {
		this.serviceClass = serviceClass;
	}

	public String getIconURL() {
		return iconURL;
	}

	public void setIconURL(String iconURL) {
		this.iconURL = iconURL;
	}
	
	

}
