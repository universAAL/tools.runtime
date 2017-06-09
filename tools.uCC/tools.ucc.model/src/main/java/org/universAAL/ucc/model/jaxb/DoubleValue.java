package org.universAAL.ucc.model.jaxb;

public class DoubleValue extends SimpleObject {

	private double value;
	private double defaultValue;

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public double getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(double defaultValue) {
		this.defaultValue = defaultValue;
	}

}
