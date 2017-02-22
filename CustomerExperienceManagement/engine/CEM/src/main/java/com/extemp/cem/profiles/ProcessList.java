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
 *         &lt;element ref="{}Process" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element ref="{}jms" minOccurs="0"/&gt;
 *         &lt;element ref="{}log_to_console" minOccurs="0"/&gt;
 *         &lt;element ref="{}wait_for_process_end_milis" minOccurs="0"/&gt;
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
    "process",
    "jms",
    "logToConsole",
    "waitForProcessEndMilis"
})
@XmlRootElement(name = "ProcessList")
public class ProcessList {

    @XmlElement(name = "Process")
    protected List<Process> process;
    protected Jms jms;
    @XmlElement(name = "log_to_console")
    protected String logToConsole;
    @XmlElement(name = "wait_for_process_end_milis")
    protected String waitForProcessEndMilis;

    /**
     * Gets the value of the process property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the process property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProcess().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Process }
     * 
     * 
     */
    public List<Process> getProcess() {
        if (process == null) {
            process = new ArrayList<Process>();
        }
        return this.process;
    }

    /**
     * Gets the value of the jms property.
     * 
     * @return
     *     possible object is
     *     {@link Jms }
     *     
     */
    public Jms getJms() {
        return jms;
    }

    /**
     * Sets the value of the jms property.
     * 
     * @param value
     *     allowed object is
     *     {@link Jms }
     *     
     */
    public void setJms(Jms value) {
        this.jms = value;
    }

    /**
     * Gets the value of the logToConsole property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getLogToConsole() {
        return logToConsole;
    }

    /**
     * Sets the value of the logToConsole property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setLogToConsole(String value) {
        this.logToConsole = value;
    }

    /**
     * Gets the value of the waitForProcessEndMilis property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWaitForProcessEndMilis() {
        return waitForProcessEndMilis;
    }

    /**
     * Sets the value of the waitForProcessEndMilis property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWaitForProcessEndMilis(String value) {
        this.waitForProcessEndMilis = value;
    }

}
