package org.universAAL.ucc.model.jaxb;

import java.util.ArrayList;
import javax.xml.bind.annotation.*;

public class EnumObject {
	private String type;
	private String selectedValue;
	private ArrayList<String> values = new ArrayList<String>();
	private String label;
	private String description;
	private boolean required;
	private boolean treeParentNode;

	@XmlAttribute(required = true)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElementWrapper(name = "values")
	@XmlElement(name = "value")
	public ArrayList<String> getValues() {
		return values;
	}

	public void setValues(ArrayList<String> values) {
		this.values = values;
	}

	public String getSelectedValue() {
		return selectedValue;
	}

	public void setSelectedValue(String selectedValue) {
		this.selectedValue = selectedValue;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired(boolean required) {
		this.required = required;
	}

	public boolean isTreeParentNode() {
		return treeParentNode;
	}

	public void setTreeParentNode(boolean treeParentNode) {
		this.treeParentNode = treeParentNode;
	}

}
