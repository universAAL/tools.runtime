package org.universAAL.ucc.model.jaxb;

import javax.xml.bind.annotation.*;

public class SimpleObject {
	
	protected String label = "";
	
	protected String description = "";
	
	protected String validator = "";
	
	protected boolean required = false;
	
	protected boolean id = false;
	
	protected String name = "";
	
	
	@XmlElement(name="label", required=true)
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	@XmlElement(name="description", required=true)
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	@XmlElement(name="validator", required=false)
	public String getValidator() {
		return validator;
	}
	public void setValidator(String validator) {
		this.validator = validator;
	}
	public boolean isRequired() {
		return required;
	}
	public void setRequired(boolean required) {
		this.required = required;
	}
	public boolean isId() {
		return id;
	}
	public void setId(boolean id) {
		this.id = id;
	}
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}	
	
	

}
