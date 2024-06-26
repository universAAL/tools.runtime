//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2013.08.05 at 09:10:43 PM CEST
//

package org.universAAL.tools.ucc.model.usrv;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;

/**
 * temporary type
 *
 * <p>
 * Java class for toBeDefined complex type.
 *
 * <p>
 * The following schema fragment specifies the expected content contained within
 * this class.
 *
 * <pre>
 * &lt;complexType name="toBeDefined">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;anyAttribute processContents='skip'/>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "toBeDefined", propOrder = { "any" })
public class ToBeDefined implements Serializable {

	private final static long serialVersionUID = 12343L;
	@XmlAnyElement
	protected List<Element> any;
	@XmlAnyAttribute
	private Map<QName, String> otherAttributes = new HashMap<QName, String>();

	/**
	 * Gets the value of the any property.
	 *
	 * <p>
	 * This accessor method returns a reference to the live list, not a
	 * snapshot. Therefore any modification you make to the returned list will
	 * be present inside the JAXB object. This is why there is not a
	 * <CODE>set</CODE> method for the any property.
	 *
	 * <p>
	 * For example, to add a new item, do as follows:
	 *
	 * <pre>
	 * getAny().add(newItem);
	 * </pre>
	 *
	 *
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Element }
	 *
	 *
	 */
	public List<Element> getAny() {
		if (any == null) {
			any = new ArrayList<Element>();
		}
		return this.any;
	}

	public boolean isSetAny() {
		return ((this.any != null) && (!this.any.isEmpty()));
	}

	public void unsetAny() {
		this.any = null;
	}

	/**
	 * Gets a map that contains attributes that aren't bound to any typed
	 * property on this class.
	 *
	 * <p>
	 * the map is keyed by the name of the attribute and the value is the string
	 * value of the attribute.
	 *
	 * the map returned by this method is live, and you can add new attribute by
	 * updating the map directly. Because of this design, there's no setter.
	 *
	 *
	 * @return always non-null
	 */
	public Map<QName, String> getOtherAttributes() {
		return otherAttributes;
	}

}
