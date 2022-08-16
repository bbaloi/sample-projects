/*
 * SessionDomainVO.java
 *
 * Created on July 6, 2003, 5:55 PM
 */

package com.perpetual.viewer.model.vo;

import java.util.Vector;
import java.util.Collection;

/**
 *
 * @author  brunob
 */
public class SessionDomainVO implements java.io.Serializable
{
    
    private DomainVO lDomain=null;
    private Collection lFacilityList=null;
    private Collection lSeverityList=null;
    private Collection lHostList=null;
    /** Creates a new instance of SessionDomainVO */
    public SessionDomainVO(DomainVO pDomain) 
    {
        lDomain=pDomain;
        lFacilityList = new Vector();
        lSeverityList = new Vector();
        lHostList = new Vector();
    }
    public SessionDomainVO(DomainVO pDomain,Collection pHosts,Collection pFacilities,Collection pSeverities) 
    {
        lDomain=pDomain;
        lFacilityList = pFacilities;
        lSeverityList = pSeverities;
        lHostList = pHosts;
    }
    public Collection getFacilityList()
    {
        return lFacilityList;
    }
    public Collection getSeverityList()
    {
        return lSeverityList;
    }
    public Collection getHostList()
    {
        return lHostList;
    }
    public void addHost(HostVO pHost)
    {
        lHostList.add(pHost);
    }
    public void addFacility(FacilityVO pFacility)
    {
        lFacilityList.add(pFacility);
    }
    public void addSeverity(SeverityVO pSeverity)
    {
        lSeverityList.add(pSeverity);
    }
    public DomainVO getDomain()
    {
        return lDomain;
    }
}
