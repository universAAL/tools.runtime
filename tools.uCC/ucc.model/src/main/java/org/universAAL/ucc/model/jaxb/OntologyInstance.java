package org.universAAL.ucc.model.jaxb;

import java.util.ArrayList;
import javax.xml.bind.annotation.*;

public class OntologyInstance {
	private String id;
	private ArrayList<Subprofile> subprofiles = new ArrayList<Subprofile>();
	private String type;

	@XmlElementWrapper(name="subprofiles")
	@XmlElement(name ="subprofile")
	public ArrayList<Subprofile> getSubprofiles() {
		return subprofiles;
	}

	public void setSubprofiles(ArrayList<Subprofile> subprofiles) {
		this.subprofiles = subprofiles;
	}

	@XmlAttribute(name="id", required=true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	

}
