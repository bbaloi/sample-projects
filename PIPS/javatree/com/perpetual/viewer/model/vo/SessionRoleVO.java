/*
 * SessionRoleVO.java
 *
 * Created on July 6, 2003, 5:47 PM
 */

package com.perpetual.viewer.model.vo;

import java.util.Vector;
import java.util.Iterator;
import java.util.Collection;
/**
 *
 * @author  brunob
 */
public class SessionRoleVO implements java.io.Serializable
{
    
    private RoleVO lRole=null;
    private  Collection lActionList=null;
    
    /** Creates a new instance of SessionRoleVO */
    public SessionRoleVO(RoleVO pRole)
    {
        lRole = pRole;
        lActionList = new Vector();
        
    }
    public SessionRoleVO(RoleVO pRole,Collection pActionList)
    {
        lRole = pRole;
        lActionList = pActionList;        
    }
    
    public void addAction(ActionVO pAction)
    {
        lActionList.add(pAction);
    }
    public Collection getActionList()
    {
        
        return lActionList;
    }
    public RoleVO getRole()
    {
        return lRole;
    }
    
}
