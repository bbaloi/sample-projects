package com.perpetual.viewer.presentation;

/**
 * @author BrunoB, MikeP
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.Iterator;
import java.util.Vector;

import javax.rmi.PortableRemoteObject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.perpetual.exception.BasePerpetualException;
import com.perpetual.util.Constants;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUD;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.vo.DomainVO;
import com.perpetual.viewer.model.vo.FacilityVO;
import com.perpetual.viewer.model.vo.HostVO;
import com.perpetual.viewer.model.vo.SeverityVO;


public class AddDomainAction extends PerpetualAction 
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( ViewLogstoreAction.class );
 
    public ActionForward doAction(ActionMapping mapping, ActionForm form,
    		HttpServletRequest request, HttpServletResponse response) 
	{
		try
		{
			sLog.debug("I'm in AddDomainAction.....");
			AddDomainActionForm actionForm = (AddDomainActionForm) form;

			String domainName = actionForm.getDomainName();
			
			Long collectionInterval = actionForm.getCollectionInterval();
			boolean summaryEnabled = actionForm.getSummaryProcessingEnabled();
			Long lastCollectionTime = actionForm.getLastCollectionTime();
			
			if(domainName ==null)
				throw new BasePerpetualException("Cannot create a domain without a name !");
			DomainVO vo = new DomainVO(-1,domainName, summaryEnabled,
					lastCollectionTime, collectionInterval);
			sLog.debug("Selected Hosts for Domain:"+ actionForm.getDomainName());
			Vector hostListV = new Vector();
			Vector facilityListV = new Vector();
			Vector severityListV = new Vector();
			Vector messagePatternListV = new Vector();

			String [] hostList =  actionForm.getHosts();
			if(hostList==null)
			{
				sLog.debug("No hosts selected for the domain ! Creating empty array");
				hostList =  new String [0];
			}
			for(int i=0;i<hostList.length;i++)
			{
				sLog.debug("+"+hostList[i]);
				HostVO hVo = new HostVO(new Integer(hostList[i]).intValue(),"", null);
				hostListV.add(hVo);
			}
			sLog.debug("Selected facilities for Domain:"+ actionForm.getDomainName());
			String [] facilityList =  actionForm.getFacilities();
			if(facilityList==null)
			{
				sLog.debug("No facilities selected for the domain ! Creating empty array");
				facilityList =  new String [0];
			}
			for(int i=0;i<facilityList.length;i++)
			{
				sLog.debug("+"+facilityList[i]);
				FacilityVO fVo = new FacilityVO(new Integer(facilityList[i]).intValue(),"");
				facilityListV.add(fVo);
			}
			sLog.debug("Selected severities for Domain:"+ actionForm.getDomainName());
			String [] severityList =  actionForm.getSeverities();
			if(severityList==null)
			{
				sLog.debug("No severities selected for the domain ! Creating empty array");
				severityList =  new String [0];
			}
			for(int i=0;i<severityList.length;i++)
			{
				sLog.debug("+"+severityList[i]);
				SeverityVO sVo = new SeverityVO(new Integer(severityList[i]).intValue(),"");
				severityListV.add(sVo);
			}

			String [] messagePatternList =  actionForm.getMessagePatterns();
			if (messagePatternList == null)
			{
				sLog.debug("No message patterns selected for the domain ! Creating empty array");
				messagePatternList =  new String [0];
			}
			for(int i=0; i< messagePatternList.length; i++)
			{
				sLog.debug("+" + messagePatternList[i]);
				MessagePatternData data = new MessagePatternData(
						new Integer(messagePatternList[i]), null, null, null);
				messagePatternListV.add(data);
			}


			
			DomainCRUD domainCRUD = (DomainCRUD)EJBLoader.createEJBObject("perpetual/DomainCRUD", DomainCRUDHome.class);
			try
			{
				vo = domainCRUD.create(vo, null, hostListV, facilityListV,
					severityListV, messagePatternListV);		// vo is now freshly updated with an id, right...?  right???

//				if (actionForm.getUsers() != null)
//					domainCRUD.addUsersToDomain(actionForm.getUsers(), vo.getId());
			}
			finally
			{
				domainCRUD.remove();
			}

			return mapping.findForward("add_domain.success");
		}
		catch(Exception ex)
		{
			sLog.error("Problem in AddDomainAction", ex);
		}

		return mapping.findForward("add_domain.failure");
	}
}



