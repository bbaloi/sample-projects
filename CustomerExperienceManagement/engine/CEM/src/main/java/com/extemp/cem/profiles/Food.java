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
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd}foodType" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd}dishName" minOccurs="0"/&gt;
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
    "foodType",
    "dishName"
})
@XmlRootElement(name = "Food", namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd")
public class Food {

    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd")
    protected String foodType;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd")
    protected String dishName;

    /**
     * Gets the value of the foodType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFoodType() {
        return foodType;
    }

    /**
     * Sets the value of the foodType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFoodType(String value) {
        this.foodType = value;
    }

    /**
     * Gets the value of the dishName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDishName() {
        return dishName;
    }

    /**
     * Sets the value of the dishName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDishName(String value) {
        this.dishName = value;
    }

}
