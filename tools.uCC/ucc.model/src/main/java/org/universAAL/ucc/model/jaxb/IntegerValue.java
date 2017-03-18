package org.universAAL.ucc.model.jaxb;

import javax.xml.bind.annotation.*;

public class IntegerValue extends SimpleObject {

	private int value;
	private int defaultValue;
	
	
	@XmlElement
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public int getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(int defaultValue) {
		this.defaultValue = defaultValue;
	}
	
	

}
