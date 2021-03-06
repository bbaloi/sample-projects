//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.11 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2017.02.22 at 05:58:39 PM EST 
//


package com.extemp.cem.profiles;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for User complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="User"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="contributorsEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="createdAt" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="description" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descriptionURLEntities" type="{}URLEntity" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="favouritesCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="followersCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="friendsCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *         &lt;element name="isContributorsEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isFollowRequestSent" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isGeoEnabled" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isProtected" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="isVerified" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="language" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="listedCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="location" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileBackgroundColor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileBackgroundImageURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileBackgroundImageURLHttps" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileBackgroundTiled" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileBannerImageURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileImageURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileImageURLHttps" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileLinkColor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileSidebarBorderColor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileSidebarFillColor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileTextColor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="profileUseBackgroundImage" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="screenName" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="status" type="{}Status" minOccurs="0"/&gt;
 *         &lt;element name="statusesCount" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *         &lt;element name="timeZone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="translator" type="{http://www.w3.org/2001/XMLSchema}boolean" minOccurs="0"/&gt;
 *         &lt;element name="URL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="UTCOffset" type="{http://www.w3.org/2001/XMLSchema}int" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "User", propOrder = {
    "contributorsEnabled",
    "createdAt",
    "description",
    "descriptionURLEntities",
    "favouritesCount",
    "followersCount",
    "friendsCount",
    "id",
    "isContributorsEnabled",
    "isFollowRequestSent",
    "isGeoEnabled",
    "isProtected",
    "isVerified",
    "language",
    "listedCount",
    "location",
    "name",
    "profileBackgroundColor",
    "profileBackgroundImageURL",
    "profileBackgroundImageURLHttps",
    "profileBackgroundTiled",
    "profileBannerImageURL",
    "profileImageURL",
    "profileImageURLHttps",
    "profileLinkColor",
    "profileSidebarBorderColor",
    "profileSidebarFillColor",
    "profileTextColor",
    "profileUseBackgroundImage",
    "screenName",
    "status",
    "statusesCount",
    "timeZone",
    "translator",
    "url",
    "utcOffset"
})
public class User {

    protected Boolean contributorsEnabled;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar createdAt;
    protected String description;
    protected List<URLEntity> descriptionURLEntities;
    protected Integer favouritesCount;
    protected Integer followersCount;
    protected Integer friendsCount;
    protected Long id;
    protected Boolean isContributorsEnabled;
    protected Boolean isFollowRequestSent;
    protected Boolean isGeoEnabled;
    protected Boolean isProtected;
    protected Boolean isVerified;
    protected String language;
    protected Integer listedCount;
    protected String location;
    protected String name;
    protected String profileBackgroundColor;
    protected String profileBackgroundImageURL;
    protected String profileBackgroundImageURLHttps;
    protected String profileBackgroundTiled;
    protected String profileBannerImageURL;
    protected String profileImageURL;
    protected String profileImageURLHttps;
    protected String profileLinkColor;
    protected String profileSidebarBorderColor;
    protected String profileSidebarFillColor;
    protected String profileTextColor;
    protected Boolean profileUseBackgroundImage;
    protected String screenName;
    protected Status status;
    protected Integer statusesCount;
    protected String timeZone;
    protected Boolean translator;
    @XmlElement(name = "URL")
    protected String url;
    @XmlElement(name = "UTCOffset")
    protected Integer utcOffset;

    /**
     * Gets the value of the contributorsEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isContributorsEnabled() {
        return contributorsEnabled;
    }

    /**
     * Sets the value of the contributorsEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setContributorsEnabled(Boolean value) {
        this.contributorsEnabled = value;
    }

    /**
     * Gets the value of the createdAt property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatedAt() {
        return createdAt;
    }

    /**
     * Sets the value of the createdAt property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatedAt(XMLGregorianCalendar value) {
        this.createdAt = value;
    }

    /**
     * Gets the value of the description property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the descriptionURLEntities property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the descriptionURLEntities property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getDescriptionURLEntities().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link URLEntity }
     * 
     * 
     */
    public List<URLEntity> getDescriptionURLEntities() {
        if (descriptionURLEntities == null) {
            descriptionURLEntities = new ArrayList<URLEntity>();
        }
        return this.descriptionURLEntities;
    }

    /**
     * Gets the value of the favouritesCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFavouritesCount() {
        return favouritesCount;
    }

    /**
     * Sets the value of the favouritesCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFavouritesCount(Integer value) {
        this.favouritesCount = value;
    }

    /**
     * Gets the value of the followersCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFollowersCount() {
        return followersCount;
    }

    /**
     * Sets the value of the followersCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFollowersCount(Integer value) {
        this.followersCount = value;
    }

    /**
     * Gets the value of the friendsCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getFriendsCount() {
        return friendsCount;
    }

    /**
     * Sets the value of the friendsCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setFriendsCount(Integer value) {
        this.friendsCount = value;
    }

    /**
     * Gets the value of the id property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setId(Long value) {
        this.id = value;
    }

    /**
     * Gets the value of the isContributorsEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsContributorsEnabled() {
        return isContributorsEnabled;
    }

    /**
     * Sets the value of the isContributorsEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsContributorsEnabled(Boolean value) {
        this.isContributorsEnabled = value;
    }

    /**
     * Gets the value of the isFollowRequestSent property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsFollowRequestSent() {
        return isFollowRequestSent;
    }

    /**
     * Sets the value of the isFollowRequestSent property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsFollowRequestSent(Boolean value) {
        this.isFollowRequestSent = value;
    }

    /**
     * Gets the value of the isGeoEnabled property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsGeoEnabled() {
        return isGeoEnabled;
    }

    /**
     * Sets the value of the isGeoEnabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsGeoEnabled(Boolean value) {
        this.isGeoEnabled = value;
    }

    /**
     * Gets the value of the isProtected property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsProtected() {
        return isProtected;
    }

    /**
     * Sets the value of the isProtected property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsProtected(Boolean value) {
        this.isProtected = value;
    }

    /**
     * Gets the value of the isVerified property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isIsVerified() {
        return isVerified;
    }

    /**
     * Sets the value of the isVerified property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsVerified(Boolean value) {
        this.isVerified = value;
    }

    /**
     * Gets the value of the language property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLanguage() {
        return language;
    }

    /**
     * Sets the value of the language property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLanguage(String value) {
        this.language = value;
    }

    /**
     * Gets the value of the listedCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getListedCount() {
        return listedCount;
    }

    /**
     * Sets the value of the listedCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setListedCount(Integer value) {
        this.listedCount = value;
    }

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLocation(String value) {
        this.location = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the profileBackgroundColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileBackgroundColor() {
        return profileBackgroundColor;
    }

    /**
     * Sets the value of the profileBackgroundColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileBackgroundColor(String value) {
        this.profileBackgroundColor = value;
    }

    /**
     * Gets the value of the profileBackgroundImageURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileBackgroundImageURL() {
        return profileBackgroundImageURL;
    }

    /**
     * Sets the value of the profileBackgroundImageURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileBackgroundImageURL(String value) {
        this.profileBackgroundImageURL = value;
    }

    /**
     * Gets the value of the profileBackgroundImageURLHttps property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileBackgroundImageURLHttps() {
        return profileBackgroundImageURLHttps;
    }

    /**
     * Sets the value of the profileBackgroundImageURLHttps property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileBackgroundImageURLHttps(String value) {
        this.profileBackgroundImageURLHttps = value;
    }

    /**
     * Gets the value of the profileBackgroundTiled property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileBackgroundTiled() {
        return profileBackgroundTiled;
    }

    /**
     * Sets the value of the profileBackgroundTiled property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileBackgroundTiled(String value) {
        this.profileBackgroundTiled = value;
    }

    /**
     * Gets the value of the profileBannerImageURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileBannerImageURL() {
        return profileBannerImageURL;
    }

    /**
     * Sets the value of the profileBannerImageURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileBannerImageURL(String value) {
        this.profileBannerImageURL = value;
    }

    /**
     * Gets the value of the profileImageURL property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileImageURL() {
        return profileImageURL;
    }

    /**
     * Sets the value of the profileImageURL property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileImageURL(String value) {
        this.profileImageURL = value;
    }

    /**
     * Gets the value of the profileImageURLHttps property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileImageURLHttps() {
        return profileImageURLHttps;
    }

    /**
     * Sets the value of the profileImageURLHttps property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileImageURLHttps(String value) {
        this.profileImageURLHttps = value;
    }

    /**
     * Gets the value of the profileLinkColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileLinkColor() {
        return profileLinkColor;
    }

    /**
     * Sets the value of the profileLinkColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileLinkColor(String value) {
        this.profileLinkColor = value;
    }

    /**
     * Gets the value of the profileSidebarBorderColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileSidebarBorderColor() {
        return profileSidebarBorderColor;
    }

    /**
     * Sets the value of the profileSidebarBorderColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileSidebarBorderColor(String value) {
        this.profileSidebarBorderColor = value;
    }

    /**
     * Gets the value of the profileSidebarFillColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileSidebarFillColor() {
        return profileSidebarFillColor;
    }

    /**
     * Sets the value of the profileSidebarFillColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileSidebarFillColor(String value) {
        this.profileSidebarFillColor = value;
    }

    /**
     * Gets the value of the profileTextColor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProfileTextColor() {
        return profileTextColor;
    }

    /**
     * Sets the value of the profileTextColor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProfileTextColor(String value) {
        this.profileTextColor = value;
    }

    /**
     * Gets the value of the profileUseBackgroundImage property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isProfileUseBackgroundImage() {
        return profileUseBackgroundImage;
    }

    /**
     * Sets the value of the profileUseBackgroundImage property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setProfileUseBackgroundImage(Boolean value) {
        this.profileUseBackgroundImage = value;
    }

    /**
     * Gets the value of the screenName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getScreenName() {
        return screenName;
    }

    /**
     * Sets the value of the screenName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setScreenName(String value) {
        this.screenName = value;
    }

    /**
     * Gets the value of the status property.
     * 
     * @return
     *     possible object is
     *     {@link Status }
     *     
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the value of the status property.
     * 
     * @param value
     *     allowed object is
     *     {@link Status }
     *     
     */
    public void setStatus(Status value) {
        this.status = value;
    }

    /**
     * Gets the value of the statusesCount property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getStatusesCount() {
        return statusesCount;
    }

    /**
     * Sets the value of the statusesCount property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setStatusesCount(Integer value) {
        this.statusesCount = value;
    }

    /**
     * Gets the value of the timeZone property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the value of the timeZone property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTimeZone(String value) {
        this.timeZone = value;
    }

    /**
     * Gets the value of the translator property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isTranslator() {
        return translator;
    }

    /**
     * Sets the value of the translator property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setTranslator(Boolean value) {
        this.translator = value;
    }

    /**
     * Gets the value of the url property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getURL() {
        return url;
    }

    /**
     * Sets the value of the url property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setURL(String value) {
        this.url = value;
    }

    /**
     * Gets the value of the utcOffset property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getUTCOffset() {
        return utcOffset;
    }

    /**
     * Sets the value of the utcOffset property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setUTCOffset(Integer value) {
        this.utcOffset = value;
    }

}
