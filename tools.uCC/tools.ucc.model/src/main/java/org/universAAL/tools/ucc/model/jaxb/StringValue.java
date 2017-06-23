package org.universAAL.tools.ucc.model.jaxb;

import javax.xml.bind.annotation.*;

public class StringValue extends SimpleObject {

	private String value = "";
	private String defaultValue = "";

	@XmlElement(required = true)
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

}
