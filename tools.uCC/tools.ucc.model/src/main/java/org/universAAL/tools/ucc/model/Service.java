package org.universAAL.tools.ucc.model;

import java.util.ArrayList;

import org.universAAL.tools.ucc.model.install.License;

public class Service {
	private String serviceId;
	private int minor = 0;
	private int major = 0;
	private int micro = 0;
	private String description = "";
	private String name = "";
	private String provider = "";
	private ArrayList<String> tags;
	private License licenses;
	private ArrayList<UAPP> uaapList;
	private String ontologyUri;
	private String iconPath;
	private String menuName;

	public Service() {
		tags = new ArrayList<String>();
		uaapList = new ArrayList<UAPP>();
	}

	public Service(String serviceId, int minor, int major, int micro, String description, String name,
			String provider, ArrayList<String> tags, License licenses, String ontologyUri, String iconPath) {
		this.serviceId = serviceId;
		this.minor = minor;
		this.major = major;
		this.micro = micro;
		this.description = description;
		this.name = name;
		this.tags = tags;
		this.licenses = licenses;
		this.provider = provider;
		this.ontologyUri = ontologyUri;
		this.iconPath = iconPath;
		tags = new ArrayList<String>();
		uaapList = new ArrayList<UAPP>();
	}

	public int getMinor() {
		return minor;
	}

	public void setMinor(int minor) {
		this.minor = minor;
	}

	public int getMajor() {
		return major;
	}

	public void setMajor(int major) {
		this.major = major;
	}

	public int getMicro() {
		return micro;
	}

	public void setMicro(int micro) {
		this.micro = micro;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<String> getTags() {
		return tags;
	}

	public void setTags(ArrayList<String> tags) {
		this.tags = tags;
	}

	public License getLicenses() {
		return licenses;
	}

	public void setLicenses(License licenses) {
		this.licenses = licenses;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public ArrayList<UAPP> getUaapList() {
		return uaapList;
	}

	public void setUaapList(ArrayList<UAPP> uaapList) {
		this.uaapList = uaapList;
	}

	public String getServiceId() {
		return serviceId;
	}

	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}

	public String getOntologyUri() {
		return ontologyUri;
	}

	public void setOntologyUri(String ontologyUri) {
		this.ontologyUri = ontologyUri;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

}
