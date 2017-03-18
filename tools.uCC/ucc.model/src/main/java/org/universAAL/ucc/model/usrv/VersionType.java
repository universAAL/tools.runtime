//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.4-2 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.05 at 09:10:43 PM CEST 
//


package org.universAAL.ucc.model.usrv;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for versionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="versionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="major" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="minor" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="micro" type="{http://www.w3.org/2001/XMLSchema}int"/>
 *         &lt;element name="build" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "versionType", propOrder = {
    "major",
    "minor",
    "micro",
    "build"
})
public class VersionType
    implements Serializable
{

    private final static long serialVersionUID = 12343L;
    @XmlElement(defaultValue = "0")
    protected int major;
    @XmlElement(defaultValue = "0")
    protected int minor;
    @XmlElement(defaultValue = "0")
    protected int micro;
    protected String build;

    /**
     * Gets the value of the major property.
     * 
     */
    public int getMajor() {
        return major;
    }

    /**
     * Sets the value of the major property.
     * 
     */
    public void setMajor(int value) {
        this.major = value;
    }

    public boolean isSetMajor() {
        return true;
    }

    /**
     * Gets the value of the minor property.
     * 
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Sets the value of the minor property.
     * 
     */
    public void setMinor(int value) {
        this.minor = value;
    }

    public boolean isSetMinor() {
        return true;
    }

    /**
     * Gets the value of the micro property.
     * 
     */
    public int getMicro() {
        return micro;
    }

    /**
     * Sets the value of the micro property.
     * 
     */
    public void setMicro(int value) {
        this.micro = value;
    }

    public boolean isSetMicro() {
        return true;
    }

    /**
     * Gets the value of the build property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getBuild() {
        return build;
    }

    /**
     * Sets the value of the build property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setBuild(String value) {
        this.build = value;
    }

    public boolean isSetBuild() {
        return (this.build!= null);
    }

}
