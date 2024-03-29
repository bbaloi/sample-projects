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
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}userId" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}firstName" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}middleName" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}lastName" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}age" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}gender" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}dateOfBirth" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}profession" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}faceBookId" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}linkedInId" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}twitterId" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}addressList" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}paymentPrefferenceList" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}loyaltyProgram" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}languageSpokenList" minOccurs="0"/&gt;
 *         &lt;element ref="{http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd}preferencesList" minOccurs="0"/&gt;
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
    "userId",
    "firstName",
    "middleName",
    "lastName",
    "age",
    "gender",
    "dateOfBirth",
    "profession",
    "faceBookId",
    "linkedInId",
    "twitterId",
    "addressList",
    "paymentPrefferenceList",
    "loyaltyProgram",
    "languageSpokenList",
    "preferencesList"
})
@XmlRootElement(name = "CustomerProfileCBO", namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
public class CustomerProfileCBO {

    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected String userId;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected String firstName;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected String middleName;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected String lastName;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected Integer age;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected String gender;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected String dateOfBirth;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected String profession;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected String faceBookId;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected String linkedInId;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected String twitterId;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected AddressList addressList;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected PaymentPrefferenceList paymentPrefferenceList;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected LoyaltyProgram loyaltyProgram;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected LanguageSpokenList languageSpokenList;
    @XmlElement(namespace = "http://www.tibco.com/schemas/CEM/SharedResources/XSD/CustProfileSchema.xsd")
    protected PreferencesList preferencesList;

    /**
     * Gets the value of the userId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Sets the value of the userId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserId(String value) {
        this.userId = value;
    }

    /**
     * Gets the value of the firstName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the value of the firstName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFirstName(String value) {
        this.firstName = value;
    }

    /**
     * Gets the value of the middleName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * Sets the value of the middleName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMiddleName(String value) {
        this.middleName = value;
    }

    /**
     * Gets the value of the lastName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the value of the lastName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLastName(String value) {
        this.lastName = value;
    }

    /**
     * Gets the value of the age property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getAge() {
        return age;
    }

    /**
     * Sets the value of the age property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setAge(Integer value) {
        this.age = value;
    }

    /**
     * Gets the value of the gender property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGender() {
        return gender;
    }

    /**
     * Sets the value of the gender property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGender(String value) {
        this.gender = value;
    }

    /**
     * Gets the value of the dateOfBirth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Sets the value of the dateOfBirth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDateOfBirth(String value) {
        this.dateOfBirth = value;
    }

    /**
     * Gets the value of the profession property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfession() {
        return profession;
    }

    /**
     * Sets the value of the profession property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfession(String value) {
        this.profession = value;
    }

    /**
     * Gets the value of the faceBookId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFaceBookId() {
        return faceBookId;
    }

    /**
     * Sets the value of the faceBookId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFaceBookId(String value) {
        this.faceBookId = value;
    }

    /**
     * Gets the value of the linkedInId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLinkedInId() {
        return linkedInId;
    }

    /**
     * Sets the value of the linkedInId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLinkedInId(String value) {
        this.linkedInId = value;
    }

    /**
     * Gets the value of the twitterId property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTwitterId() {
        return twitterId;
    }

    /**
     * Sets the value of the twitterId property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTwitterId(String value) {
        this.twitterId = value;
    }

    /**
     * Gets the value of the addressList property.
     * 
     * @return
     *     possible object is
     *     {@link AddressList }
     *     
     */
    public AddressList getAddressList() {
        return addressList;
    }

    /**
     * Sets the value of the addressList property.
     * 
     * @param value
     *     allowed object is
     *     {@link AddressList }
     *     
     */
    public void setAddressList(AddressList value) {
        this.addressList = value;
    }

    /**
     * Gets the value of the paymentPrefferenceList property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentPrefferenceList }
     *     
     */
    public PaymentPrefferenceList getPaymentPrefferenceList() {
        return paymentPrefferenceList;
    }

    /**
     * Sets the value of the paymentPrefferenceList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentPrefferenceList }
     *     
     */
    public void setPaymentPrefferenceList(PaymentPrefferenceList value) {
        this.paymentPrefferenceList = value;
    }

    /**
     * Gets the value of the loyaltyProgram property.
     * 
     * @return
     *     possible object is
     *     {@link LoyaltyProgram }
     *     
     */
    public LoyaltyProgram getLoyaltyProgram() {
        return loyaltyProgram;
    }

    /**
     * Sets the value of the loyaltyProgram property.
     * 
     * @param value
     *     allowed object is
     *     {@link LoyaltyProgram }
     *     
     */
    public void setLoyaltyProgram(LoyaltyProgram value) {
        this.loyaltyProgram = value;
    }

    /**
     * Gets the value of the languageSpokenList property.
     * 
     * @return
     *     possible object is
     *     {@link LanguageSpokenList }
     *     
     */
    public LanguageSpokenList getLanguageSpokenList() {
        return languageSpokenList;
    }

    /**
     * Sets the value of the languageSpokenList property.
     * 
     * @param value
     *     allowed object is
     *     {@link LanguageSpokenList }
     *     
     */
    public void setLanguageSpokenList(LanguageSpokenList value) {
        this.languageSpokenList = value;
    }

    /**
     * Gets the value of the preferencesList property.
     * 
     * @return
     *     possible object is
     *     {@link PreferencesList }
     *     
     */
    public PreferencesList getPreferencesList() {
        return preferencesList;
    }

    /**
     * Sets the value of the preferencesList property.
     * 
     * @param value
     *     allowed object is
     *     {@link PreferencesList }
     *     
     */
    public void setPreferencesList(PreferencesList value) {
        this.preferencesList = value;
    }

}
