package org.universAAL.tools.ucc.model;

import java.util.HashMap;

/**
 * Represents an UAPP file which consists a arbitrary UAPPParts. Every UAPPPart
 * can retrieved about the partId of a part.
 * 
 * @author Nicole Merkle
 * 
 */
public class UAPP {
	private HashMap<String, UAPPPart> parts;
	private String name;
	private String version;
	private Provider provider;
	private String location;
	private String appID;

	public UAPP(String name, String version, Provider provider) {
		parts = new HashMap<String, UAPPPart>();
		this.name = name;
		this.version = version;
		this.provider = provider;
	}

	public UAPP() {
		parts = new HashMap<String, UAPPPart>();
	}

	public HashMap<String, UAPPPart> getParts() {
		return parts;
	}

	public void setParts(HashMap<String, UAPPPart> parts) {
		this.parts = parts;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Provider getProvider() {
		return provider;
	}

	public void setProvider(Provider provider) {
		this.provider = provider;
	}

	public UAPPPart getPart(String partId) {
		return parts.get(partId);
	}

	public void addPart(String partId, UAPPPart part) {
		parts.put(partId, part);
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAppID() {
		return appID;
	}

	public void setAppID(String appID) {
		this.appID = appID;
	}

}
