package org.universAAL.ucc.configuration.beans;

/**
 * 
 * A bean class for the form to save a configuration instance.
 * 
 * @author Sebastian.Schoebinge
 * 
 */

public class ConfigurationSaveOptions {

	String id;
	String useCaseId;
	String version;
	String author;
	boolean primary;
	boolean secondary;

	public String getVersion() {
		if (version != null) {
			return version;
		} else {
			return "";
		}
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAuthor() {
		if (author != null) {
			return author;
		} else {
			return "";
		}
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getId() {
		if (id != null) {
			return id;
		} else {
			return "";
		}
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUseCaseId() {
		if (useCaseId != null) {
			return useCaseId;
		} else {
			return "";
		}
	}

	public void setUseCaseId(String useCaseId) {
		this.useCaseId = useCaseId;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public boolean isSecondary() {
		return secondary;
	}

	public void setSecondary(boolean secondary) {
		this.secondary = secondary;
	}

}
