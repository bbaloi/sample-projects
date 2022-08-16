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

import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;


public class UpdateDomainAction extends PerpetualAction 
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewLogstoreAction.class );
	private static Object homeObj=null;
	private static DomainCRUDHome lDomainCrudHome=null;
     
    public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
    {
        try
        {
            sLog.debug("I'm in UpdateDomainAction.....");

			DomainCRUD domainCRUD = (DomainCRUD)EJBLoader.createEJBObject("perpetual/DomainCRUD", DomainCRUDHome.class);
			try
			{
				UpdateDomainActionForm actionForm = (UpdateDomainActionForm) form;
				DomainVO domainVO = (DomainVO) this.session.getAttribute("DomainVO");

				sLog.debug("Doing Update for Domain #"+domainVO.getId()+ " - "+domainVO.getName());
				domainVO.setName(actionForm.getDomainName());
				domainVO.setSummaryProcessingEnabled(actionForm.getSummaryProcessingEnabled());
				domainVO.setCollectionInterval(actionForm.getCollectionInterval());
				domainVO.setLastCollectionTime(actionForm.getLastCollectionTime());
				//String domainName = actionForm.getDomainName();
				//String domainId   = actionForm.getDomainId();
				String [] freeHosts = actionForm.getFreeHosts();
				if(freeHosts==null)
				{
					sLog.debug("No hosts selected for adding - creating empty array !");
					freeHosts= new String [0];
				}
				String [] freeSeverities = actionForm.getFreeSeverities();
				if(freeSeverities==null)
				{
					sLog.debug("No severities selected for adding - creating empty array !");
					freeSeverities= new String [0];
				}
				String [] freeFacilities = actionForm.getFreeFacilities();
				if(freeFacilities==null)
				{
					sLog.debug("No facilities selected for adding - createing empty array !");
					freeFacilities= new String [0];
				}
				String [] freeMessagePatterns = actionForm.getFreeMessagePatterns();
				if(freeMessagePatterns == null)
				{
					sLog.debug("No message patterns selected for adding - createing empty array !");
					freeMessagePatterns = new String [0];
				}
				
				String [] takenHosts = actionForm.getTakenHosts();
				if(takenHosts==null)
				{
					sLog.debug("No hosts selected for deletion - createing empty array !");
					takenHosts= new String [0];
				}
				String [] takenFacilities = actionForm.getTakenFacilities();
				if(takenFacilities==null)
				{
					 sLog.debug("No facilities selected for deletion - createing empty array !");
					takenFacilities =  new String [0];
				}
				String [] takenSeverities = actionForm.getTakenSeverities();    
				if(takenSeverities==null)
				{
					 sLog.debug("No severities selected for deletion - createing empty array !");
					 takenSeverities= new String [0];
				}
				
				String [] takenMessagePatterns = actionForm.getTakenMessagePatterns();
				if(takenMessagePatterns == null)
				{
					sLog.debug("No taken message patterns selected for deletion - createing empty array !");
					takenMessagePatterns = new String [0];
				}
				
				Collection delFacs=deleteElements(takenFacilities,(Collection) this.session.getAttribute("takenFacilityList"),Constants.FACILITY);
				sLog.debug("remainderFacilities:"+delFacs.size());
				Collection delSevs=deleteElements(takenSeverities,(Collection) this.session.getAttribute("takenSeverityList"),Constants.SEVERITY);
				sLog.debug("remainderSeverities:"+delSevs.size());
				Collection delHosts=deleteElements(takenHosts,(Collection) this.session.getAttribute("takenHostList"),Constants.HOST);
				sLog.debug("remainderHosts:"+delHosts.size());
				Collection delMessagePatterns=deleteElements(takenMessagePatterns,
					(Collection) this.session.getAttribute("takenMessagePatternList"),
					Constants.MESSAGEPATTERN);
				sLog.debug("remainderMessagePatterns:"+delMessagePatterns.size());
				
				Collection finalHostList = mergeLists(delHosts,freeHosts,(Collection) this.session.getAttribute("freeHostList"),Constants.HOST);
				Collection finalFacilityList = mergeLists(delFacs,freeFacilities,(Collection) this.session.getAttribute("freeFacilityList"),Constants.FACILITY);
				Collection finalSeverityList = mergeLists(delSevs,freeSeverities,(Collection) this.session.getAttribute("freeSeverityList"),Constants.SEVERITY);
				Collection finalMessagePatternList = mergeLists(delMessagePatterns, freeMessagePatterns,
						(Collection) this.session.getAttribute("freeMessagePatternList"),
						Constants.MESSAGEPATTERN);
											  
				sLog.debug("Final Host List");
				Iterator hostIt = finalHostList.iterator();                
				while(hostIt.hasNext())
				{
					 HostVO hVo = (HostVO) hostIt.next();    
					 sLog.debug("+"+hVo.getId()+":"+hVo.getName());
				}
				sLog.debug("Final FacilityList");
				Iterator facilityIt = finalFacilityList.iterator();
				while(facilityIt.hasNext())
				{
					  FacilityVO fVo = (FacilityVO) facilityIt.next();    
					 sLog.debug("+"+fVo.getId()+":"+fVo.getName());
				}
				sLog.debug("Final Severity List");
				Iterator severityIt = finalSeverityList.iterator();
				while(severityIt.hasNext())
				{
					  SeverityVO sVo = (SeverityVO) severityIt.next();    
					 sLog.debug("+"+sVo.getId()+":"+sVo.getName());
				}

				domainCRUD.update(domainVO, null, finalHostList, finalFacilityList,
						finalSeverityList, finalMessagePatternList);
/*				
				domainCRUD.update(domainVO, null, finalHostList, finalFacilityList,
						finalSeverityList, domainPatternList);
*/

//				if (actionForm.getAddUsers() != null)
//					domainCRUD.addUsersToDomain(actionForm.getAddUsers(), domainVO.getId());
//				if (actionForm.getRemoveUsers() != null)
//					domainCRUD.removeUsersFromDomain(actionForm.getRemoveUsers(), domainVO.getId());
			}
			finally
			{
				domainCRUD.remove();
			}
        }
        catch(Exception ex)
        {
            sLog.error(ex);                
            return (mapping.findForward("update_domain.failure"));
        }
              
        sLog.debug("forwarding to update_domain.success");
        return (mapping.findForward("update_domain.success"));
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
		if(entityType==Constants.MESSAGEPATTERN)
		   {
			   sLog.debug("Removing selected message patterns!");
			   for(int y =0;y< selected.length;y++)
				   {
						 int id = (new Integer(selected[y])).intValue();
						 Iterator it = finalList.iterator();
						 while(it.hasNext())
						 {
						   MessagePatternData curVo = (MessagePatternData)it.next();
						   if(id==curVo.getId().intValue())
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
       
	if(entityType==Constants.MESSAGEPATTERN)
	  {
		   sLog.debug("Merging Severity Lists:"
		   		+ remainderList.size()+" existing + "+newList.length+" new");       
		   Iterator it = remainderList.iterator();
		   while(it.hasNext())
		   {
			   MessagePatternData vo = (MessagePatternData) it.next();
			   finalList.add(vo);
			   sLog.debug("added message pattern:" + vo.getName()
			   		+ " from remainder");
		   }
		   for(int i=0;i<newList.length;i++)
		   {
			   int messagePatternId = (new Integer(newList[i])).intValue();
			   Iterator bit = fullNewList.iterator();
			   while (bit.hasNext())
			   {
				   MessagePatternData tvo = (MessagePatternData) bit.next(); 
				   if(messagePatternId == tvo.getId().intValue())
				   {
					   MessagePatternData tmpVo = tvo; 
					   finalList.add(tmpVo);     
					   sLog.debug("added message pattern:"+ tvo.getName() + " from new list");
				   }
			   }
		   }
	  }
       
       
       return (Collection) finalList;
   }
}
