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
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd}favouriteFoodList" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd}favouriteRestaurantList" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd}favouriteChefList" minOccurs="0"/&gt;
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
    "favouriteFoodList",
    "favouriteRestaurantList",
    "favouriteChefList"
})
@XmlRootElement(name = "FoodProfile", namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd")
public class FoodProfile {

    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd")
    protected FavouriteFoodList favouriteFoodList;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd")
    protected FavouriteRestaurantList favouriteRestaurantList;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/FoodSchema.xsd")
    protected FavouriteChefList favouriteChefList;

    /**
     * Gets the value of the favouriteFoodList property.
     * 
     * @return
     *     possible object is
     *     {@link FavouriteFoodList }
     *     
     */
    public FavouriteFoodList getFavouriteFoodList() {
        return favouriteFoodList;
    }

    /**
     * Sets the value of the favouriteFoodList property.
     * 
     * @param value
     *     allowed object is
     *     {@link FavouriteFoodList }
     *     
     */
    public void setFavouriteFoodList(FavouriteFoodList value) {
        this.favouriteFoodList = value;
    }

    /**
     * Gets the value of the favouriteRestaurantList property.
     * 
     * @return
     *     possible object is
     *     {@link FavouriteRestaurantList }
     *     
     */
    public FavouriteRestaurantList getFavouriteRestaurantList() {
        return favouriteRestaurantList;
    }

    /**
     * Sets the value of the favouriteRestaurantList property.
     * 
     * @param value
     *     allowed object is
     *     {@link FavouriteRestaurantList }
     *     
     */
    public void setFavouriteRestaurantList(FavouriteRestaurantList value) {
        this.favouriteRestaurantList = value;
    }

    /**
     * Gets the value of the favouriteChefList property.
     * 
     * @return
     *     possible object is
     *     {@link FavouriteChefList }
     *     
     */
    public FavouriteChefList getFavouriteChefList() {
        return favouriteChefList;
    }

    /**
     * Sets the value of the favouriteChefList property.
     * 
     * @param value
     *     allowed object is
     *     {@link FavouriteChefList }
     *     
     */
    public void setFavouriteChefList(FavouriteChefList value) {
        this.favouriteChefList = value;
    }

}
