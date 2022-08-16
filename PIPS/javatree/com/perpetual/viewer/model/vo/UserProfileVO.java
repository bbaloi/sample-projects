/*
 * UserProfileVO.java
 *
 * Created on June 28, 2003, 3:42 PM
 */

package com.perpetual.viewer.model.vo;

import java.util.Collection;

/**
 *
 * @author  brunob
 */
public class UserProfileVO implements java.io.Serializable
{
    private UserVO lUser=null;
    private SessionDomainVO lDomain=null; 
    private SessionRoleVO lRole=null;
    
    
    /** Creates a new instance of UserProfileVO */
    public UserProfileVO(UserVO pUser,SessionDomainVO pDomain,SessionRoleVO pRole)
    {
        lUser=pUser;
        lRole=pRole;
        lDomain=pDomain;
    }
    public int getUserId()
    {
        return lUser.getId();
    }    
    public String getUserName()
    {
        return lUser.getUserId();
    }
    public String getPassword()
    {
        return lUser.getPassword();
    }
    public String getPhone()
    {
        return lUser.getPhone();
    }
    public String getEmail()
    {
        return lUser.getEmail();
    }
    public String getRealName()
    {
        return lUser.getRealname();
    }
    public int getRoleId()
    {
        return lRole.getRole().getId();
    }
    public String getRoleName()
    {
        return lRole.getRole().getRoleName();
    }
    //returns a Collection of ActionVO
    public Collection getActionList()
    {
        return lRole.getActionList();
    }
    public int getDomainId()
    {
        return lDomain.getDomain().getId();
    }
    public String getDomainName()
    {
        return lDomain.getDomain().getName();
    }
    //returns a Collection of FacilityVO
    public Collection getFacilityList()
    {
        return lDomain.getFacilityList();
    }
    //returns a Collection of SeverityVO
    public Collection getSeverityList()
    {
        return lDomain.getSeverityList();
    }
    //returns a Collection of HostVO
    public Collection getHostList()
    {
        return lDomain.getHostList();
    }
    
     
}
