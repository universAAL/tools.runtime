
package org.universAAL.tools.ucc.commerce.ustore.tools;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>
 * Clase Java para getSessionKeyResponse complex type.
 *
 * <p>
 * El siguiente fragmento de esquema especifica el contenido que se espera que
 * haya en esta clase.
 *
 * <pre>
 * &lt;complexType name="getSessionKeyResponse">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="return" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 *
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getSessionKeyResponse", propOrder = { "_return" })
public class GetSessionKeyResponse {

	@XmlElement(name = "return")
	protected String _return;

	/**
	 * Obtiene el valor de la propiedad return.
	 *
	 * @return possible object is {@link String }
	 *
	 */
	public String getReturn() {
		return _return;
	}

	/**
	 * Define el valor de la propiedad return.
	 *
	 * @param value
	 *            allowed object is {@link String }
	 *
	 */
	public void setReturn(String value) {
		this._return = value;
	}

}
