/*
 * PrivillegesCache.java
 *
 * Created on July 7, 2003, 1:34 PM
 */

package com.perpetual.viewer.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Vector;
import com.perpetual.viewer.model.vo.SessionRoleVO;
import com.perpetual.viewer.model.vo.RoleVO;
import com.perpetual.viewer.model.vo.ActionVO;

import com.perpetual.viewer.model.ejb.Role;
import com.perpetual.viewer.model.ejb.RoleHome;
import com.perpetual.viewer.model.ejb.RoleAction;
import com.perpetual.viewer.model.ejb.RoleActionHome;
import com.perpetual.viewer.model.ejb.Action;
import com.perpetual.viewer.model.ejb.ActionPK;
import com.perpetual.viewer.model.ejb.ActionHome;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.Constants;
import com.perpetual.util.ServiceLocator;
import com.perpetual.exception.BasePerpetualException;
import javax.rmi.PortableRemoteObject;

/**
 *
 * @author  brunob
 */
public final class PrivillegesCache 
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( PrivillegesCache.class );
        
    private static PrivillegesCache lInstance = null;
    private SessionRoleVO  lRoles=null; 
    private static Object homeObj=null;
    private static RoleHome lRoleHome=null;
    private static RoleActionHome lRoleActionHome=null;
    private static ActionHome lActionHome =null;
    private Hashtable lRoleMap=null;
    
    static
    {
        try
        {
          homeObj = ServiceLocator.findHome(Constants.jndiName_Role);
          lRoleHome = (RoleHome) PortableRemoteObject.narrow(homeObj,RoleHome.class); 
          sLog.debug("got RoleHome");
          homeObj = ServiceLocator.findHome(Constants.jndiName_RoleAction);
          lRoleActionHome = (RoleActionHome) PortableRemoteObject.narrow(homeObj,RoleActionHome.class); 
          sLog.debug("got RoleActionHome");  
          homeObj = ServiceLocator.findHome(Constants.jndiName_Action);
          lActionHome = (ActionHome) PortableRemoteObject.narrow(homeObj,ActionHome.class); 
          sLog.debug("got ActionHome");           
          sLog.debug("-----Building Privilleges Cache !-----");
        }
        catch(javax.naming.NamingException excp)
        {
            String errMsg ="Could not instantiate Home interfaces:";
            sLog.error(errMsg);
        }
    }
    
    /** Creates a new instance of PrivillegesCache */
    private PrivillegesCache() 
    {
        lRoleMap= new Hashtable();
        init();
    }
    private void init()
    {
        try
        {
            Collection roles = lRoleHome.findAll();
            Iterator it = roles.iterator();
            while(it.hasNext())
            {
                Role lRole = (Role) it.next();
                int roleId = lRole.getId();
                Integer role = new Integer(roleId);
                Vector actionList = new Vector();
                sLog.debug("adding Role:"+lRole.getRoleName());
                Collection actions = lRoleActionHome.findByRoleId(roleId);
                Iterator actionIt = actions.iterator();
                while(actionIt.hasNext())
                {
                    RoleAction roleAction = (RoleAction) actionIt.next();
                    Integer actionId = roleAction.getActionId();
                     Action action = lActionHome.findByPrimaryKey(new ActionPK(actionId.intValue()));
                    ActionVO actionVo = new ActionVO(action.getId(),action.getName());
                    actionList.add(actionVo);
                }
                lRoleMap.put(role,actionList);
            }
        }
        catch(javax.ejb.FinderException excp)
        {
            String errMsg ="Exception in PrivillegesCache init()";
            sLog.error(errMsg);
        }
         catch(java.rmi.RemoteException excp)
        {
            String errMsg ="Exception in PrivillegesCache init()";
            sLog.error(errMsg);            
        }
    }
    public static PrivillegesCache getInstance()
    {
        if(lInstance==null)
                lInstance = new PrivillegesCache();
        return lInstance;
    }
    public Collection getActionList(int pRoleId) throws BasePerpetualException
    {
        Integer role = new Integer(pRoleId);
        //should return the collection of actions that it has
        Collection actionSet = (Collection)lRoleMap.get(role);
        if(actionSet==null)
        {
            sLog.debug("Role not in cache ! - looking in the DB");
            Collection newActionSet=getActionsByRole(pRoleId);
            if(newActionSet==null)
            {
                sLog.error("Invalid role !!!");
                throw new BasePerpetualException("Invalid role !!!");
            }
            else
            {
                lRoleMap.put(new Integer(pRoleId),newActionSet);
            }
        }
        
        return actionSet;
    }
    private Collection getActionsByRole(int pRoleId)
    {
        Collection actionSet = null;
        Collection roleActionSet=null;
        try
        {
            Vector actionList = new Vector();
                roleActionSet = lRoleActionHome.findByRoleId(pRoleId);
                Iterator actionIt = roleActionSet.iterator();
                while(actionIt.hasNext())
                {
                    RoleAction roleAction = (RoleAction) actionIt.next();
                    Integer actionId = roleAction.getActionId();
                    Action action = lActionHome.findByPrimaryKey(new ActionPK(actionId.intValue()));
                    ActionVO actionVo = new ActionVO(action.getId(),action.getName());
                    actionList.add(actionVo);
                }
        }
         catch(javax.ejb.FinderException excp)
        {
            String errMsg ="Exception in PrivillegesCache init()";
            sLog.error(errMsg);
        }
         catch(java.rmi.RemoteException excp)
        {
            String errMsg ="Exception in PrivillegesCache init()";
            sLog.error(errMsg);            
        }
        return actionSet;
    }
    
}
