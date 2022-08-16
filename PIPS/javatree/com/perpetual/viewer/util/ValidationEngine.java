/*
 * ValidationEngine.java
 *
 * Created on July 8, 2003, 11:36 PM
 */

package com.perpetual.viewer.util;

import com.perpetual.util.PerpetualC2Logger;

import java.util.Collection;
import java.util.Iterator;
import com.perpetual.viewer.model.vo.ActionVO;
import com.perpetual.exception.BasePerpetualException;

/**
 *
 * @author  brunob
 */
public final class ValidationEngine 
{
     private static PerpetualC2Logger sLog = new PerpetualC2Logger( ValidationEngine.class );
   
    private static ValidationEngine lInstance=null;
    
    /** Creates a new instance of ValidationEngine */
    private ValidationEngine() 
    {
    }
    public static ValidationEngine getInstance()
    {
        if(lInstance==null)
            lInstance = new ValidationEngine();
        return lInstance;
    }
    public boolean isValid(int pRoleId,String pActionName) throws BasePerpetualException
    {
        sLog.debug("Validating if action "+pActionName+" belongs to role:"+pRoleId);
        Collection actionList =PrivillegesCache.getInstance().getActionList(pRoleId);
       
        Iterator it = actionList.iterator();
        while(it.hasNext())
        {
            ActionVO action = (ActionVO) it.next();
            if(action.getName().equals(pActionName))
                return true;
        }
        
        
        return false;
    }
    
}
