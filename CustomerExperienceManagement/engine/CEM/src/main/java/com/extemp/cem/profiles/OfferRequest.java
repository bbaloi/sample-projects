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
 *         &lt;element ref="{}userId_OfferReq" minOccurs="0"/&gt;
 *         &lt;element ref="{}reqId_offer" minOccurs="0"/&gt;
 *         &lt;element ref="{}userName" minOccurs="0"/&gt;
 *         &lt;element ref="{}offer" minOccurs="0"/&gt;
 *         &lt;element ref="{}offerType" minOccurs="0"/&gt;
 *         &lt;element ref="{}applicationSrc_offer" minOccurs="0"/&gt;
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
    "userIdOfferReq",
    "reqIdOffer",
    "userName",
    "offer",
    "offerType",
    "applicationSrcOffer"
})
@XmlRootElement(name = "OfferRequest")
public class OfferRequest {

    @XmlElement(name = "userId_OfferReq")
    protected String userIdOfferReq;
    @XmlElement(name = "reqId_offer")
    protected String reqIdOffer;
    protected String userName;
    protected String offer;
    protected String offerType;
    @XmlElement(name = "applicationSrc_offer")
    protected String applicationSrcOffer;

    /**
     * Gets the value of the userIdOfferReq property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserIdOfferReq() {
        return userIdOfferReq;
    }

    /**
     * Sets the value of the userIdOfferReq property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserIdOfferReq(String value) {
        this.userIdOfferReq = value;
    }

    /**
     * Gets the value of the reqIdOffer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getReqIdOffer() {
        return reqIdOffer;
    }

    /**
     * Sets the value of the reqIdOffer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setReqIdOffer(String value) {
        this.reqIdOffer = value;
    }

    /**
     * Gets the value of the userName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Sets the value of the userName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserName(String value) {
        this.userName = value;
    }

    /**
     * Gets the value of the offer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOffer() {
        return offer;
    }

    /**
     * Sets the value of the offer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOffer(String value) {
        this.offer = value;
    }

    /**
     * Gets the value of the offerType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getOfferType() {
        return offerType;
    }

    /**
     * Sets the value of the offerType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setOfferType(String value) {
        this.offerType = value;
    }

    /**
     * Gets the value of the applicationSrcOffer property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationSrcOffer() {
        return applicationSrcOffer;
    }

    /**
     * Sets the value of the applicationSrcOffer property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationSrcOffer(String value) {
        this.applicationSrcOffer = value;
    }

}
