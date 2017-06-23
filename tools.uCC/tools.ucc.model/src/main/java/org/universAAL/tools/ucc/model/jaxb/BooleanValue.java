package org.universAAL.tools.ucc.model.jaxb;

import javax.xml.bind.annotation.*;

public class BooleanValue extends SimpleObject {

	private Boolean value;

	@XmlElement
	public Boolean isValue() {
		return value;
	}

	public void setValue(Boolean value) {
		this.value = value;
	}

	public Boolean getValue() {
		return value;
	}

}
