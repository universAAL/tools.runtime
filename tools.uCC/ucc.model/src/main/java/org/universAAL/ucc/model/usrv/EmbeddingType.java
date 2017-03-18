//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.05 at 09:10:43 PM CEST 
//


package org.universAAL.ucc.model.usrv;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for embeddingType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="embeddingType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="singleContainer"/>
 *     &lt;enumeration value="anyContainer"/>
 *     &lt;enumeration value="namedContainer"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "embeddingType")
@XmlEnum
public enum EmbeddingType {

    @XmlEnumValue("singleContainer")
    SINGLE_CONTAINER("singleContainer"),
    @XmlEnumValue("anyContainer")
    ANY_CONTAINER("anyContainer"),
    @XmlEnumValue("namedContainer")
    NAMED_CONTAINER("namedContainer");
    private final String value;

    EmbeddingType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static EmbeddingType fromValue(String v) {
        for (EmbeddingType c: EmbeddingType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
