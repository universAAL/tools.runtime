
package org.universAAL.tools.ucc.commerce.ustore.tools;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Clase Java para getUserProfile complex type.
 * 
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que
 * haya en esta clase.
 * 
 * <pre>
 * &lt;complexType name="getUserProfile">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sessionKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUserProfile", propOrder = { "sessionKey" })
public class GetUserProfile {

	protected String sessionKey;

	/**
	 * Obtiene el valor de la propiedad sessionKey.
	 * 
	 * @return possible object is {@link String }
	 * 
	 */
	public String getSessionKey() {
		return sessionKey;
	}

	/**
	 * Define el valor de la propiedad sessionKey.
	 * 
	 * @param value
	 *            allowed object is {@link String }
	 * 
	 */
	public void setSessionKey(String value) {
		this.sessionKey = value;
	}

}
