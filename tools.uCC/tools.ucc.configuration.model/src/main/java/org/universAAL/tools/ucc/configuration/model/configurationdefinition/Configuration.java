//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2012.08.02 at 01:53:27 PM MESZ
//

package org.universAAL.tools.ucc.configuration.model.configurationdefinition;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * <p>
 * Java class for Configuration complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="Configuration">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="category" type="{http://universaal.cas.de}Category" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *       &lt;attribute name="id" use="required" type="{http://www.w3.org/2001/XMLSchema}ID" />
 *       &lt;attribute name="bundlename" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="version" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *             &lt;pattern value="([0-9]+.{0,1})+[0-9]+"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="author" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Configuration", propOrder = { "category" })
public class Configuration {

	@XmlElement(required = true)
	protected List<Category> category;
	@XmlAttribute(required = true)
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	@XmlID
	@XmlSchemaType(name = "ID")
	protected String id;
	@XmlAttribute(required = true)
	protected String bundlename;
	@XmlAttribute(required = true)
	protected String version;
	@XmlAttribute(required = true)
	protected String author;

	/**
	 * Gets the value of the category property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the category property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getCategory().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Category
	 * }
	 *
	 *
	 */
	public List<Category> getCategory() {
		if (category == null) {
			category = new ArrayList<Category>();
		}
		return this.category;
	}

	/**
	 * Gets the value of the id property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getId() {
		return id;
	}

	/**
	 * Sets the value of the id property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setId(String value) {
		this.id = value;
	}

	/**
	 * Gets the value of the bundlename property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getBundlename() {
		return bundlename;
	}

	/**
	 * Sets the value of the bundlename property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setBundlename(String value) {
		this.bundlename = value;
	}

	/**
	 * Gets the value of the version property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets the value of the version property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setVersion(String value) {
		this.version = value;
	}

	/**
	 * Gets the value of the author property.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getAuthor() {
		return author;
	}

	/**
	 * Sets the value of the author property.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setAuthor(String value) {
		this.author = value;
	}

	@Override
	public String toString() {
		return bundlename;
	}

}
