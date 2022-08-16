/*
 * ViewRawLogForm.java
 *
 * Created on May 17, 2003, 6:45 PM
 */

package com.perpetual.viewer.presentation;

import org.apache.struts.action.ActionForm;
import java.util.HashMap;
import java.util.Collection;

/**
 *
 * @author  brunob
 */
public class AddDomainActionForm extends ActionForm
{
    private String domainName;
	private Long 	collectionInterval = null;
	private boolean summaryProcessingEnabled = false;
	private Long 	lastCollectionTime = null;
    private String [] facilities;
    private String [] severities;
    private String [] hosts;
    private String [] messagePatterns;
//    private String [] users;
 
    public void setDomainName(String pDomainName)
    {
        domainName = pDomainName;
    }
    public String getDomainName()
    {
        return domainName;
    }
    public void setFacilities(String [] pList)
    {
        facilities = pList;
    }
    public String []  getFacilities()
    {
        return facilities;
    }
    public void setHosts(String [] pList)
    {
        hosts = pList;
    }
    public String []  getHosts()
    {
        return hosts;
    }
    public void setSeverities(String [] pList)
    {
        severities = pList;
    }
    public String []  getSeverities()
    {
        return severities;
    }
    
	public void setMessagePatterns(String [] pList)
	{
		messagePatterns = pList;
	}
	public String []  getMessagePatterns()
	{
		return messagePatterns;
	}
    
/*
	public void setUsers(String[] users)
	{
		this.users = users;
	}
	public String[] getUsers()
	{
		return users;
	}
*/
	/**
	 * @return
	 */
	public Long getCollectionInterval() {
		return collectionInterval;
	}

	/**
	 * @return
	 */
	public Long getLastCollectionTime() {
		return lastCollectionTime;
	}

	/**
	 * @return
	 */
	public boolean getSummaryProcessingEnabled() {
		return summaryProcessingEnabled;
	}

	/**
	 * @param long1
	 */
	public void setCollectionInterval(Long long1) {
		collectionInterval = long1;
	}

	/**
	 * @param long1
	 */
	public void setLastCollectionTime(Long long1) {
		lastCollectionTime = long1;
	}

	/**
	 * @param b
	 */
	public void setSummaryProcessingEnabled(boolean b) {
		summaryProcessingEnabled = b;
	}

}


