//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.22 at 05:58:39 PM EST 
//


package com.extemp.cem.profiles;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element ref="{}IntentName" minOccurs="0"/&gt;
 *         &lt;element ref="{}IntentId" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "intentName",
    "intentId"
})
@XmlRootElement(name = "Intent")
public class Intent {

    @XmlElement(name = "IntentName")
    protected String intentName;
    @XmlElement(name = "IntentId")
    protected String intentId;

    /**
     * Gets the value of the intentName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntentName() {
        return intentName;
    }

    /**
     * Sets the value of the intentName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntentName(String value) {
        this.intentName = value;
    }

    /**
     * Gets the value of the intentId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIntentId() {
        return intentId;
    }

    /**
     * Sets the value of the intentId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIntentId(String value) {
        this.intentId = value;
    }

}
