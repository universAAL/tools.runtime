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
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 *
 * Deployable element to install.
 *
 *
 * <p>
 * Java class for bundle complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="bundle">
 *   &lt;simpleContent>
 *     &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>anyURI">
 *       &lt;attribute name="start-level" type="{http://www.w3.org/2001/XMLSchema}int" />
 *       &lt;attribute name="start" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="dependency" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *     &lt;/extension>
 *   &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bundle", namespace = "http://karaf.apache.org/xmlns/features/v1.0.0", propOrder = { "value" })
public class Bundle implements Serializable {

	private final static long serialVersionUID = 12343L;
	@XmlValue
	@XmlSchemaType(name = "anyURI")
	protected String value;
	@XmlAttribute(name = "start-level")
	protected Integer startLevel;
	@XmlAttribute(name = "start")
	protected Boolean start;
	@XmlAttribute(name = "dependency")
	protected Boolean dependency;

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
	 * Gets the value of the startLevel property.
	 *
	 * @return possible object is {@link Integer }
	 *
	 */
	public int getStartLevel() {
		return startLevel;
	}

	/**
	 * Sets the value of the startLevel property.
	 *
	 * @param value
	 *            allowed object is {@link Integer }
	 *
	 */
	public void setStartLevel(int value) {
		this.startLevel = value;
	}

	public boolean isSetStartLevel() {
		return (this.startLevel != null);
	}

	public void unsetStartLevel() {
		this.startLevel = null;
	}

	/**
	 * Gets the value of the start property.
	 *
	 * @return possible object is {@link Boolean }
	 *
	 */
	public boolean isStart() {
		return start;
	}

	/**
	 * Sets the value of the start property.
	 *
	 * @param value
	 *            allowed object is {@link Boolean }
	 *
	 */
	public void setStart(boolean value) {
		this.start = value;
	}

	public boolean isSetStart() {
		return (this.start != null);
	}

	public void unsetStart() {
		this.start = null;
	}

	/**
	 * Gets the value of the dependency property.
	 *
	 * @return possible object is {@link Boolean }
	 *
	 */
	public boolean isDependency() {
		return dependency;
	}

	/**
	 * Sets the value of the dependency property.
	 *
	 * @param value
	 *            allowed object is {@link Boolean }
	 *
	 */
	public void setDependency(boolean value) {
		this.dependency = value;
	}

	public boolean isSetDependency() {
		return (this.dependency != null);
	}

	public void unsetDependency() {
		this.dependency = null;
	}

}
