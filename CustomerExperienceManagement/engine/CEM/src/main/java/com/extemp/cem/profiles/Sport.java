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
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/SportsSchema.xsd}sportType" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/SportsSchema.xsd}generalGameSentiment" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/SportsSchema.xsd}numGamesAttendedSeason" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/SportsSchema.xsd}numGamesAttendedTotal" minOccurs="0"/&gt;
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
    "sportType",
    "generalGameSentiment",
    "numGamesAttendedSeason",
    "numGamesAttendedTotal"
})
@XmlRootElement(name = "Sport", namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/SportsSchema.xsd")
public class Sport {

    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/SportsSchema.xsd")
    protected String sportType;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/SportsSchema.xsd")
    protected String generalGameSentiment;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/SportsSchema.xsd")
    protected Long numGamesAttendedSeason;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/SportsSchema.xsd")
    protected Long numGamesAttendedTotal;

    /**
     * Gets the value of the sportType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSportType() {
        return sportType;
    }

    /**
     * Sets the value of the sportType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSportType(String value) {
        this.sportType = value;
    }

    /**
     * Gets the value of the generalGameSentiment property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGeneralGameSentiment() {
        return generalGameSentiment;
    }

    /**
     * Sets the value of the generalGameSentiment property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGeneralGameSentiment(String value) {
        this.generalGameSentiment = value;
    }

    /**
     * Gets the value of the numGamesAttendedSeason property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumGamesAttendedSeason() {
        return numGamesAttendedSeason;
    }

    /**
     * Sets the value of the numGamesAttendedSeason property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumGamesAttendedSeason(Long value) {
        this.numGamesAttendedSeason = value;
    }

    /**
     * Gets the value of the numGamesAttendedTotal property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getNumGamesAttendedTotal() {
        return numGamesAttendedTotal;
    }

    /**
     * Sets the value of the numGamesAttendedTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setNumGamesAttendedTotal(Long value) {
        this.numGamesAttendedTotal = value;
    }

}
