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
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd}merchandiseName" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd}merchandiseType" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd}merchandiseMaxPrice" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd}merchandiseSize" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd}merchandiseCategory" minOccurs="0"/&gt;
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
    "merchandiseName",
    "merchandiseType",
    "merchandiseMaxPrice",
    "merchandiseSize",
    "merchandiseCategory"
})
@XmlRootElement(name = "Merchandise", namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd")
public class Merchandise {

    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd")
    protected String merchandiseName;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd")
    protected String merchandiseType;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd")
    protected Double merchandiseMaxPrice;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd")
    protected String merchandiseSize;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/ShoppingSchema.xsd")
    protected String merchandiseCategory;

    /**
     * Gets the value of the merchandiseName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerchandiseName() {
        return merchandiseName;
    }

    /**
     * Sets the value of the merchandiseName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerchandiseName(String value) {
        this.merchandiseName = value;
    }

    /**
     * Gets the value of the merchandiseType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerchandiseType() {
        return merchandiseType;
    }

    /**
     * Sets the value of the merchandiseType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerchandiseType(String value) {
        this.merchandiseType = value;
    }

    /**
     * Gets the value of the merchandiseMaxPrice property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getMerchandiseMaxPrice() {
        return merchandiseMaxPrice;
    }

    /**
     * Sets the value of the merchandiseMaxPrice property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setMerchandiseMaxPrice(Double value) {
        this.merchandiseMaxPrice = value;
    }

    /**
     * Gets the value of the merchandiseSize property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerchandiseSize() {
        return merchandiseSize;
    }

    /**
     * Sets the value of the merchandiseSize property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerchandiseSize(String value) {
        this.merchandiseSize = value;
    }

    /**
     * Gets the value of the merchandiseCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMerchandiseCategory() {
        return merchandiseCategory;
    }

    /**
     * Sets the value of the merchandiseCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMerchandiseCategory(String value) {
        this.merchandiseCategory = value;
    }

}
