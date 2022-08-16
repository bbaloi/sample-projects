package com.perpetual.viewer.presentation;

/**
 * @author BrunoB, MikeP
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import javax.rmi.*;
import javax.servlet.http.*;

import org.apache.struts.action.*;

import com.perpetual.util.Constants;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.EJBLoader;

import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.UserVO;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUDHome;
import com.perpetual.viewer.control.ejb.crud.user.UserCRUD;


public class UpdateDomainAdminAction extends PerpetualAction 
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewLogstoreAction.class );
    
	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    {
        try
        {
			UserCRUD userCRUD = (UserCRUD)EJBLoader.createEJBObject("perpetual/UserCRUD", UserCRUDHome.class);
			DomainCRUD domainCRUD = (DomainCRUD) EJBLoader
					.createEJBObject("perpetual/DomainCRUD", DomainCRUDHome.class);
			try
			{
				UpdateDomainAdminActionForm actionForm = (UpdateDomainAdminActionForm) form;

				// update Domain with RP parameters
				DomainVO dvo = domainCRUD.retrieveByPrimaryKey(
						new DomainVO(actionForm.getId(), ""));
						
				dvo.setCollectionInterval(actionForm.getCollectionInterval());		
				dvo.setSummaryProcessingEnabled(actionForm.getSummaryProcessingEnabled());

				domainCRUD.update(dvo);

				String[] enableUsers = actionForm.getEnableUsers(), disableUsers = actionForm.getDisableUsers();
				if (enableUsers != null)
				{
					for (int i = 0; i < enableUsers.length; i++)
					{
						UserVO userVO = userCRUD.retrieveByPrimaryKey(Integer.parseInt(enableUsers[i]));
						userVO.setEnabled(true);
						userCRUD.update(userVO);
					}
				}
				if (disableUsers != null)
				{
					for (int i = 0; i < disableUsers.length; i++)
					{
						UserVO userVO = userCRUD.retrieveByPrimaryKey(Integer.parseInt(disableUsers[i]));
						userVO.setEnabled(false);
						userCRUD.update(userVO);
					}
				}
			}
			finally
			{
				userCRUD.remove();
				domainCRUD.remove();
			}

			return mapping.findForward("success");
        }
        catch(Exception ex)
        {
            sLog.error("Problem with UpdateDomainAdminActionForm", ex);
        }
              
		return mapping.findForward("failure");
    }
    
    private Collection deleteElements(String [] selected,Collection wholeList,int entityType)
    {
        Collection finalList = wholeList;
 
        sLog.debug("selected for deletion:"+selected.length);
        sLog.debug("full list has:"+wholeList.size()+" elements");
        
        if(entityType==Constants.HOST)
        {
            sLog.debug("Removing selected hosts !");
           for(int x =0;x< selected.length;x++)
                {
                      int id = (new Integer(selected[x])).intValue();
                      Iterator it = finalList.iterator();
                      while(it.hasNext())
                      {
                        HostVO curVo = (HostVO)it.next();
                        if(id==curVo.getId())
                        {
                            finalList.remove(curVo);
                            sLog.debug("removed - "+id+"-from final list");
                            break;
                         }
                      }
                 }
        }
        if(entityType==Constants.FACILITY)
        {
            sLog.debug("Removing selected facilities !");
                for(int x =0;x< selected.length;x++)
                {
                      int id = (new Integer(selected[x])).intValue();
                      Iterator it = finalList.iterator();
                      while(it.hasNext())
                      {
                        FacilityVO curVo = (FacilityVO)it.next();
                        if(id==curVo.getId())
                        {
                            finalList.remove(curVo);
                            sLog.debug("removed - "+id+"-from final list");
                            break;
                         }
                      }
                 }
        }
        if(entityType==Constants.SEVERITY)
        {
            sLog.debug("Removing selected severities!");
            for(int y =0;y< selected.length;y++)
                {
                      int id = (new Integer(selected[y])).intValue();
                      Iterator it = finalList.iterator();
                      while(it.hasNext())
                      {
                        SeverityVO curVo = (SeverityVO)it.next();
                        if(id==curVo.getId())
                        {
                            finalList.remove(curVo);
                            sLog.debug("removed - "+id+"-from final list");
                            break;
                         }
                      }
                 }
        }
           return finalList;
    }
   private Collection mergeLists(Collection remainderList,String [] newList,Collection fullNewList,int entityType)
   {
       Vector finalList = new Vector();
       
       sLog.debug("Full List:"+fullNewList.size());
       
       if(entityType==Constants.HOST)
       {      
            sLog.debug("Merging Host Lists:"+remainderList.size()+" existing + "+newList.length+" new");       
            Iterator it = remainderList.iterator();
            while(it.hasNext())
            {
                HostVO vo = (HostVO) it.next();
                finalList.add(vo);
                sLog.debug("added host:"+vo.getName()+" from remainder");
             
            }
            for(int i=0;i<newList.length;i++)
            {
                int hostId = (new Integer(newList[i])).intValue();
                //sLog.debug("new Id:"+hostId);
                Iterator bit = fullNewList.iterator();
                while (bit.hasNext())
                {
                    HostVO tvo = (HostVO)bit.next(); 
                    //sLog.debug("old id:"+tvo.getId());
                    if(hostId == tvo.getId())
                    {
                        HostVO tmpVo = tvo; 
                        finalList.add(tmpVo);      
                        sLog.debug("added host:"+tvo.getName()+" from new list");
                    }
                }
            }
       }
       if(entityType==Constants.FACILITY)
       {
            sLog.debug("Merging Facility Lists:"+remainderList.size()+" existing + "+newList.length+" new");       
             
            Iterator it = remainderList.iterator();
            while(it.hasNext())
            {
                FacilityVO vo = (FacilityVO) it.next();
                finalList.add(vo);
                sLog.debug("added host:"+vo.getName()+" from remainder");
            }
            for(int i=0;i<newList.length;i++)
            {
                int facilityId = (new Integer(newList[i])).intValue();
                Iterator bit = fullNewList.iterator();
                while (bit.hasNext())
                {
                    FacilityVO tvo = (FacilityVO)bit.next(); 
                    if(facilityId == tvo.getId())
                    {
                        FacilityVO tmpVo = tvo; 
                        finalList.add(tmpVo);  
                        sLog.debug("added host:"+tvo.getName()+" from new list");
                    }
                }
            }
       }
       if(entityType==Constants.SEVERITY)
       {
            sLog.debug("Merging Severity Lists:"+remainderList.size()+" existing + "+newList.length+" new");       
            Iterator it = remainderList.iterator();
            while(it.hasNext())
            {
                SeverityVO vo = (SeverityVO) it.next();
                finalList.add(vo);
                sLog.debug("added host:"+vo.getName()+" from remainder");
            }
            for(int i=0;i<newList.length;i++)
            {
                int severityId = (new Integer(newList[i])).intValue();
                Iterator bit = fullNewList.iterator();
                while (bit.hasNext())
                {
                    SeverityVO tvo = (SeverityVO)bit.next(); 
                    if(severityId == tvo.getId())
                    {
                        SeverityVO tmpVo = tvo; 
                        finalList.add(tmpVo);     
                        sLog.debug("added host:"+tvo.getName()+" from new list");
                    }
                }
            }
       }
       
       
       return (Collection) finalList;
   }
}

