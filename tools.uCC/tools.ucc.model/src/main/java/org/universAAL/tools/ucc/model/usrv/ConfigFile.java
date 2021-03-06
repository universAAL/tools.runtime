//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.08.05 at 09:10:43 PM CEST
//

package org.universAAL.tools.ucc.model.usrv;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * Additional configuration files which should be created during feature
 * installation.
 *
 *
 * <p>
 * Java class for configFile complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="configFile">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 *       &lt;attribute name="finalname" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "configFile", namespace = "http://karaf.apache.org/xmlns/features/v1.0.0", propOrder = { "value" })
public class ConfigFile implements Serializable {

	private final static long serialVersionUID = 12343L;
	@XmlValue
	protected String value;
	@XmlAttribute(name = "finalname", required = true)
	protected String finalname;

	/**
	 * Gets the value of the value property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Sets the value of the value property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isSetValue() {
		return (this.value != null);
	}

	/**
	 * Gets the value of the finalname property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getFinalname() {
		return finalname;
	}

	/**
	 * Sets the value of the finalname property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setFinalname(String value) {
		this.finalname = value;
	}

	public boolean isSetFinalname() {
		return (this.finalname != null);
	}

}
