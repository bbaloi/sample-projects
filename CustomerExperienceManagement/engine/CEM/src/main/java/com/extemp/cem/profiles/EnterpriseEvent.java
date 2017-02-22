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
 *         &lt;element ref="{}ent_event_id" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_event_name" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_event_type" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_id" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_app_source" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_user_id" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_activity" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_activity_object" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_activity_object_uri" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_activity_category" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_activity_context_category" minOccurs="0"/&gt;
 *         &lt;element ref="{}ent_activity_context_content" minOccurs="0"/&gt;
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
    "entEventId",
    "entEventName",
    "entEventType",
    "entId",
    "entAppSource",
    "entUserId",
    "entActivity",
    "entActivityObject",
    "entActivityObjectUri",
    "entActivityCategory",
    "entActivityContextCategory",
    "entActivityContextContent"
})
@XmlRootElement(name = "EnterpriseEvent")
public class EnterpriseEvent {

    @XmlElement(name = "ent_event_id")
    protected String entEventId;
    @XmlElement(name = "ent_event_name")
    protected String entEventName;
    @XmlElement(name = "ent_event_type")
    protected String entEventType;
    @XmlElement(name = "ent_id")
    protected String entId;
    @XmlElement(name = "ent_app_source")
    protected String entAppSource;
    @XmlElement(name = "ent_user_id")
    protected String entUserId;
    @XmlElement(name = "ent_activity")
    protected String entActivity;
    @XmlElement(name = "ent_activity_object")
    protected String entActivityObject;
    @XmlElement(name = "ent_activity_object_uri")
    protected String entActivityObjectUri;
    @XmlElement(name = "ent_activity_category")
    protected String entActivityCategory;
    @XmlElement(name = "ent_activity_context_category")
    protected String entActivityContextCategory;
    @XmlElement(name = "ent_activity_context_content")
    protected String entActivityContextContent;

    /**
     * Gets the value of the entEventId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntEventId() {
        return entEventId;
    }

    /**
     * Sets the value of the entEventId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntEventId(String value) {
        this.entEventId = value;
    }

    /**
     * Gets the value of the entEventName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntEventName() {
        return entEventName;
    }

    /**
     * Sets the value of the entEventName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntEventName(String value) {
        this.entEventName = value;
    }

    /**
     * Gets the value of the entEventType property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntEventType() {
        return entEventType;
    }

    /**
     * Sets the value of the entEventType property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntEventType(String value) {
        this.entEventType = value;
    }

    /**
     * Gets the value of the entId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntId() {
        return entId;
    }

    /**
     * Sets the value of the entId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntId(String value) {
        this.entId = value;
    }

    /**
     * Gets the value of the entAppSource property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntAppSource() {
        return entAppSource;
    }

    /**
     * Sets the value of the entAppSource property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntAppSource(String value) {
        this.entAppSource = value;
    }

    /**
     * Gets the value of the entUserId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntUserId() {
        return entUserId;
    }

    /**
     * Sets the value of the entUserId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntUserId(String value) {
        this.entUserId = value;
    }

    /**
     * Gets the value of the entActivity property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntActivity() {
        return entActivity;
    }

    /**
     * Sets the value of the entActivity property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntActivity(String value) {
        this.entActivity = value;
    }

    /**
     * Gets the value of the entActivityObject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntActivityObject() {
        return entActivityObject;
    }

    /**
     * Sets the value of the entActivityObject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntActivityObject(String value) {
        this.entActivityObject = value;
    }

    /**
     * Gets the value of the entActivityObjectUri property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntActivityObjectUri() {
        return entActivityObjectUri;
    }

    /**
     * Sets the value of the entActivityObjectUri property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntActivityObjectUri(String value) {
        this.entActivityObjectUri = value;
    }

    /**
     * Gets the value of the entActivityCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntActivityCategory() {
        return entActivityCategory;
    }

    /**
     * Sets the value of the entActivityCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntActivityCategory(String value) {
        this.entActivityCategory = value;
    }

    /**
     * Gets the value of the entActivityContextCategory property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntActivityContextCategory() {
        return entActivityContextCategory;
    }

    /**
     * Sets the value of the entActivityContextCategory property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntActivityContextCategory(String value) {
        this.entActivityContextCategory = value;
    }

    /**
     * Gets the value of the entActivityContextContent property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getEntActivityContextContent() {
        return entActivityContextContent;
    }

    /**
     * Sets the value of the entActivityContextContent property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setEntActivityContextContent(String value) {
        this.entActivityContextContent = value;
    }

}
