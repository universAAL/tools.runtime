package org.universAAL.ucc.model;

import java.util.ArrayList;
import java.util.List;

import org.universAAL.middleware.deploymanager.uapp.model.Part;

/**
 * An UAPPPart represents a single part of a uapp file. It has a name, an appId,
 * a uapplocation, a version, a description and consists a Part, a bundleId and
 * bundleVersion.
 * 
 * @author Nicole Merkle
 * 
 */

public class UAPPPart {
	private String name;
	private String appId;
	private String uappLocation;
	private int minor;
	private int major;
	private int micro;
	private String description;
	private boolean multipart;
	private Part part;
	private String bundleId;
	private String bundleVersion;
	private List<UAPPReqAtom> reqAtoms = new ArrayList<UAPPReqAtom>();

	public UAPPPart() {
	}

	public UAPPPart(String appId, String name, String location, int major, int minor, int micro, String description,
			boolean multipart, Part part, String bundleId, String bundleVersion) {
		this.appId = appId;
		this.name = name;
		this.uappLocation = location;
		this.description = description;
		this.major = major;
		this.micro = micro;
		this.minor = minor;
		this.multipart = multipart;
		this.part = part;
		this.bundleId = bundleId;
		this.bundleVersion = bundleVersion;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUappLocation() {
		return uappLocation;
	}

	public void setUappLocation(String uappLocation) {
		this.uappLocation = uappLocation;
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

	public boolean isMultipart() {
		return multipart;
	}

	public void setMultipart(boolean multipart) {
		this.multipart = multipart;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Part getPart() {
		return part;
	}

	public void setPart(Part part) {
		this.part = part;
	}

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getBundleVersion() {
		return bundleVersion;
	}

	public void setBundleVersion(String bundleVersion) {
		this.bundleVersion = bundleVersion;
	}

	public List<UAPPReqAtom> getReqAtoms() {
		return reqAtoms;
	}

	public void addReqAtoms(String name, List<String> value, String criteria) {
		this.reqAtoms.add(new UAPPReqAtom(name, value, criteria));
	}

	public void addReqAtoms(UAPPReqAtom atom) {
		this.reqAtoms.add(atom);
	}
}
