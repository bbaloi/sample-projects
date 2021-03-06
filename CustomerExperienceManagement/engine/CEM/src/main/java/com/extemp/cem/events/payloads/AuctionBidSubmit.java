//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.22 at 05:58:41 PM EST 
//


package com.extemp.cem.events.payloads;

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
 *         &lt;element ref="{}userId_auction" minOccurs="0"/&gt;
 *         &lt;element ref="{}current_bid" minOccurs="0"/&gt;
 *         &lt;element ref="{}applicationSrc_auction" minOccurs="0"/&gt;
 *         &lt;element ref="{}auctionObjectId" minOccurs="0"/&gt;
 *         &lt;element ref="{}running_total" minOccurs="0"/&gt;
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
    "userIdAuction",
    "currentBid",
    "applicationSrcAuction",
    "auctionObjectId",
    "runningTotal"
})
@XmlRootElement(name = "AuctionBidSubmit")
public class AuctionBidSubmit {

    @XmlElement(name = "userId_auction")
    protected String userIdAuction;
    @XmlElement(name = "current_bid")
    protected Double currentBid;
    @XmlElement(name = "applicationSrc_auction")
    protected String applicationSrcAuction;
    protected String auctionObjectId;
    @XmlElement(name = "running_total")
    protected Double runningTotal;

    /**
     * Gets the value of the userIdAuction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserIdAuction() {
        return userIdAuction;
    }

    /**
     * Sets the value of the userIdAuction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserIdAuction(String value) {
        this.userIdAuction = value;
    }

    /**
     * Gets the value of the currentBid property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getCurrentBid() {
        return currentBid;
    }

    /**
     * Sets the value of the currentBid property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setCurrentBid(Double value) {
        this.currentBid = value;
    }

    /**
     * Gets the value of the applicationSrcAuction property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getApplicationSrcAuction() {
        return applicationSrcAuction;
    }

    /**
     * Sets the value of the applicationSrcAuction property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setApplicationSrcAuction(String value) {
        this.applicationSrcAuction = value;
    }

    /**
     * Gets the value of the auctionObjectId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuctionObjectId() {
        return auctionObjectId;
    }

    /**
     * Sets the value of the auctionObjectId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuctionObjectId(String value) {
        this.auctionObjectId = value;
    }

    /**
     * Gets the value of the runningTotal property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRunningTotal() {
        return runningTotal;
    }

    /**
     * Sets the value of the runningTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRunningTotal(Double value) {
        this.runningTotal = value;
    }

}
