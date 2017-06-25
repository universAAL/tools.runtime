
package org.universAAL.tools.ucc.commerce.ustore.tools;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Clase Java para getFreeServices complex type.
 *
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que
 * haya en esta clase.
 *
 * <pre>
 * &lt;complexType name="getFreeServices">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="sessionKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="isFitToUser" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getFreeServices", propOrder = { "sessionKey", "isFitToUser" })
public class GetFreeServices {

	protected String sessionKey;
	protected boolean isFitToUser;

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

	/**
	 * Obtiene el valor de la propiedad isFitToUser.
	 *
	 */
	public boolean isIsFitToUser() {
		return isFitToUser;
	}

	/**
	 * Define el valor de la propiedad isFitToUser.
	 *
	 */
	public void setIsFitToUser(boolean value) {
		this.isFitToUser = value;
	}

}
