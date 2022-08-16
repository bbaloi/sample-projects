package com.perpetual.viewer.presentation;

import java.util.Date;

import org.apache.struts.action.ActionForm;

/**
 *
 * @author  brunob
 */
public class UpdateDomainActionForm extends ActionForm
{
    private String   domainName=null;
    private String  domainId=null;
    private Long 	collectionInterval = null;
    private boolean summaryProcessingEnabled = false;
    private Long 	lastCollectionTime = null;
    private String [] takenFacilities=null;
    private String [] takenSeverities=null;
    private String [] takenHosts=null;
	private String [] takenMessagePatterns=null;
    private String [] freeFacilities=null;
    private String [] freeSeverities=null;
    private String [] freeHosts=null;
	private String [] freeMessagePatterns=null;
//	private String [] addUsers, removeUsers;
 
    /** Creates a new instance of ViewRawLogForm */
    public UpdateDomainActionForm() 
    {
    }
    public void setDomainName(String pDomainName)
    {
        domainName = pDomainName;
    }
    public String getDomainName()
    {
        return domainName;
    }
    public void setDomainId(String pDomainId)
    {
        domainId = pDomainId;
    }
    public String getDomainId()
    {
        return domainId;
    }
    public void setTakenFacilities(String [] pList)
    {
        takenFacilities = pList;
    }
    public String []  getTakenFacilities()
    {
        return takenFacilities;
    }
    public void setTakenHosts(String [] pList)
    {
        takenHosts = pList;
    }
    public String []  getTakenHosts()
    {
        return takenHosts;
    }
    public void setTakenSeverities(String [] pList)
    {
        takenSeverities = pList;
    }
    public String []  getTakenSeverities()
    {
        return takenSeverities;
    }
    //----------------
    public void setFreeFacilities(String [] pList)
    {
        freeFacilities = pList;
    }
    public String []  getFreeFacilities()
    {
        return freeFacilities;
    }
    public void setFreeHosts(String [] pList)
    {
        freeHosts = pList;
    }
    public String []  getFreeHosts()
    {
        return freeHosts;
    }
    public void setFreeSeverities(String [] pList)
    {
        freeSeverities = pList;
    }
    public String []  getFreeSeverities()
    {
        return freeSeverities;
    }
    

/*
	public void setAddUsers(String[] addUsers)
	{
		this.addUsers = addUsers;
	}
	public String[] getAddUsers()
	{
		return addUsers;
	}
	public void setRemoveUsers(String[] removeUsers)
	{
		this.removeUsers = removeUsers;
	}
	public String[] getRemoveUsers()
	{
		return removeUsers;
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
	 * @param b
	 */
	public void setSummaryProcessingEnabled(boolean b) {
		summaryProcessingEnabled = b;
	}

	/**
	 * @return
	 */
	public Long getLastCollectionTime() {
		return lastCollectionTime;
	}

	/**
	 * @param date
	 */
	public void setLastCollectionTime(Long date) {
		lastCollectionTime = date;
	}

	/**
	 * @return
	
	**/
	public String[] getFreeMessagePatterns() {
		return freeMessagePatterns;
	}

	/**
	 * @return
	 */
	public String[] getTakenMessagePatterns() {
		return takenMessagePatterns;
	}

	/**
	 * @param strings
	 */
	public void setFreeMessagePatterns(String[] strings) {
		freeMessagePatterns = strings;
	}

	/**
	 * @param strings
	 */
	public void setTakenMessagePatterns(String[] strings) {
		takenMessagePatterns = strings;
	}

}


