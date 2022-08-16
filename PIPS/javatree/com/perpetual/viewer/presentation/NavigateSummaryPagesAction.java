package com.perpetual.viewer.presentation;

/**
 * @author Mike Pot http://www.mikepot.com
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import java.text.*;
import javax.servlet.http.*;
import javax.rmi.*;
import javax.servlet.*;

import org.apache.struts.action.*;
import org.apache.struts.util.*;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.model.vo.SeverityVO;
import com.perpetual.viewer.model.vo.FacilityVO;
/*import com.perpetual.log.LogRecordFormat;
import com.perpetual.log.LogDatabase;
import com.perpetual.log.LogFilter;
import com.perpetual.log.LogRecord;
import com.perpetual.log.Cursor;
import com.perpetual.log.LogSystem;*/

import com.perpetual.logserver.LogRecordFormat;
import com.perpetual.logserver.LogDatabase;
import com.perpetual.logserver.LogFilter;
import com.perpetual.logserver.LogRecord;
import com.perpetual.logserver.Cursor;
import com.perpetual.logserver.LogSystem;

import com.perpetual.util.Constants;
import com.perpetual.viewer.util.ViewerGlobals;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUD;
import com.perpetual.viewer.control.query.SummaryFilter;
import com.perpetual.viewer.control.ejb.crud.summary.SummaryCRUDHome;
import com.perpetual.viewer.control.SummaryLogicDescriptor;
import com.perpetual.viewer.control.query.SummaryRecord;
import com.perpetual.viewer.model.vo.SummaryRecordVO;
import java.sql.Timestamp;
import com.perpetual.rp.util.RPConstants;
import com.perpetual.util.Constants;
import java.util.LinkedHashMap;

import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUD;
import com.perpetual.viewer.control.ejb.crud.messagepattern.MessagePatternCRUDHome;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.util.EJBLoader;

import com.perpetual.viewer.control.query.SummaryCursor;
import com.perpetual.viewer.control.query.SummaryQueryEngine;

public class NavigateSummaryPagesAction extends PerpetualAction
{
	private static PerpetualC2Logger sLog = new PerpetualC2Logger( NavigateSummaryPagesAction.class );
        private static MessagePatternCRUD messagePatternCRUD = (MessagePatternCRUD)EJBLoader.createEJBObject(MessagePatternCRUDHome.JNDI_NAME, MessagePatternCRUDHome.class);
	
        private DateFormat m_fullDateFormat, m_compactDateFormat;
	private int m_line;
        private boolean bSeveritiesAnd,bFacilitiesAnd;
        private boolean bFacilitiesOrSeverities,bFacilitiesOrMessages,bSeveritiesOrMessages;
        private SummaryCRUDHome summaryHome=null;
        private SummaryCRUD queryEngine=null;
        private ViewGeneralSummaryActionForm form2=null;
        private int page;
        private SummaryCursor viewSummaryList=null;
        protected String [] filterSeverities,filterFacilities,filterHosts,filterMessagePatterns;

        
        private ArrayList facilityCollection=null;        
        private ArrayList facilityCollectionVal=null; 
        private ArrayList severityCollection=null;
        private ArrayList severityCollectionVal=null;
        private ArrayList messagePatternCollection=null;
        private HashMap summaryFilterMap=null;
               
	public ActionForward doAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) 
	{
                   
            facilityCollection=new ArrayList();        
            facilityCollectionVal=new ArrayList(); 
            severityCollection=new ArrayList();
            severityCollectionVal=new ArrayList();
            messagePatternCollection=new ArrayList();
        
                sLog.debug("In NavigateSummaryPagesAction");
		form2 = (ViewGeneralSummaryActionForm)form;
		sLog.debug(form2.toString());
                 page = form2.getPage();
                sLog.debug("Selected Page="+page);                   
                try
                {
                    sLog.debug("Navigating pages !");
                    viewSummaryList = (SummaryCursor) request.getSession().getAttribute("summaryCollection");
                    //build page map
                    Map map = new LinkedHashMap();
                    int numPages = viewSummaryList.getNumPages();
                    sLog.debug("Num pages:"+numPages);
                    for (int i = 0; i < numPages; i++)
                        map.put(pageLabel(Integer.toString(i), i, page), new Integer(i).toString());
                    request.setAttribute("pageMap",map);    

                    Collection rowsInPage = viewSummaryList.getPage(page);
                    sLog.debug("Page:"+page+" has "+rowsInPage.size()+" rows");
                    request.setAttribute("rowList",rowsInPage);
		}
		catch(Exception ex)
		{
			sLog.error("problem", ex);
                        String errorMessage="Failed to load SummaryQueryForm !"+ex.getMessage();
			request.setAttribute("errorMessage",errorMessage);
                        return mapping.findForward("failure");
		}
               
                
                return mapping.findForward("success");

	}
         private static String getMessagePatternName(Integer pPatternId) throws javax.ejb.FinderException,java.rmi.RemoteException
        {
            MessagePatternData data = messagePatternCRUD.retrieveByPrimaryKey(pPatternId.intValue());
            return data.getName();
        }
        private String pageLabel(String label, int page, int currentPage)
	{
		StringBuffer buffer = new StringBuffer();
		if (page == currentPage)
			buffer.append("<strong><font color=red>");		// change to style sheets later
		buffer.append(label);
		if (page == currentPage)
			buffer.append("</font></strong>");
		return buffer.toString();
	}
        //private Collection prepareSummaryList(Collection pSummaryList,Collection pSeverityList,Collection pFacilityList)
        public static SummaryCursor prepareSummaryList(SummaryCursor pSummaryList,Collection pSeverityList,Collection pFacilityList) throws Exception
        {
            SummaryCursor viewCursor=null;
            ArrayList viewSummaryList = new ArrayList();
            String patternName=null;
            
            Iterator it = pSummaryList.iterator();
            while(it.hasNext())
            {
                 
                 Collection facList = new ArrayList();
                 Collection sevList = new ArrayList();
            
                SummaryRecord record = (SummaryRecord)it.next();
                Map summaryMap=record.getMap();
                String hostName = (String) summaryMap.get(ViewerGlobals.HostName);
                sLog.debug("HostName:"+hostName);
                String domainName= (String)summaryMap.get(ViewerGlobals.DomainName);
                sLog.debug("DomainName:"+domainName);               
                 //String vendorName= (String)summaryMap.get(ViewerGlobals.VendorName);
                //sLog.debug("VendorName:"+vendorName);
                //String deviceType=(String)summaryMap.get(ViewerGlobals.DeviceType);
                //sLog.debug("deviceType:"+deviceType);
                String startDate=((Timestamp)summaryMap.get(ViewerGlobals.StartDate)).toString();
                sLog.debug("StartDate:"+startDate);
                String endDate=((Timestamp)summaryMap.get(ViewerGlobals.EndDate)).toString();
                sLog.debug("EndDate:"+endDate);                
                
                Integer patternId= (Integer)summaryMap.get(ViewerGlobals.MessagePatternId);
                if(patternId!=null)
                {
                    try
                    {
                        patternName= getMessagePatternName(patternId);
                    }
                    catch(javax.ejb.ObjectNotFoundException excp)
                    {
                        sLog.error("Did not find MessagePatter:"+ patternId);                        
                    }
                }
                sLog.debug("MessagePattern:"+patternName);
                                    
                Iterator fit = pFacilityList.iterator();
                while(fit.hasNext())
                {
                    String facVal = ((Integer)fit.next()).toString();
                    
                    if(facVal.equals(RPConstants.Kernel))
                    {
                        Integer fac0=(Integer)summaryMap.get(ViewerGlobals.fac0);
                        if(fac0!=null)
                        {     
                        sLog.debug("Got Kernel/Fac0:"+fac0.toString());
                        facList.add(fac0.toString());   
                        }
                    }
                    if(facVal.equals(RPConstants.User))
                    {
                         Integer fac1= (Integer)summaryMap.get(ViewerGlobals.fac1);
                        if(fac1!=null)
                        {
                            sLog.debug("Got User/Fac1:"+fac1.toString());
                            facList.add(fac1.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Mail))
                    {
                         Integer fac2= (Integer)summaryMap.get(ViewerGlobals.fac2);
                        if(fac2!=null)
                        {
                            sLog.debug("Got Mail/fac2:"+fac2.toString());
                            facList.add(fac2.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Daemon))
                    {
                        Integer fac3= (Integer)summaryMap.get(ViewerGlobals.fac3);
                        if(fac3!=null)
                        {
                            sLog.debug("Got Daemon/Fac3:"+fac3.toString());
                            facList.add(fac3.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Security))
                    {                         
                        Integer fac4= (Integer)summaryMap.get(ViewerGlobals.fac4);
                        if(fac4!=null)
                        {
                            sLog.debug("Got Security/Fac4:"+fac4.toString());
                            facList.add(fac4.toString());
                        }
                     }
                    if(facVal.equals(RPConstants.Syslog))
                    { 
                        Integer fac5= (Integer)summaryMap.get(ViewerGlobals.fac5);
                        if(fac5!=null)
                        {
                            sLog.debug("Got Syslog/Fac5:"+fac5.toString());
                            facList.add(fac5.toString());
                        }
                     }
                     if(facVal.equals(RPConstants.Lpr))
                    { 
                        Integer fac6= (Integer)summaryMap.get(ViewerGlobals.fac6);
                        if(fac6!=null)
                        {
                            sLog.debug("Got Lpr/Fac6:"+fac6.toString());
                            facList.add(fac6.toString());
                        }
                     }
                     if(facVal.equals(RPConstants.News))
                    {
                         Integer fac7= (Integer)summaryMap.get(ViewerGlobals.fac7);
                        if(fac7!=null)
                        {
                            sLog.debug("Got News/Fac7:"+fac7.toString());
                            facList.add(fac7.toString());
                        }
                     }
                    if(facVal.equals(RPConstants.UUCP))
                    {
                         Integer fac8= (Integer)summaryMap.get(ViewerGlobals.fac8);
                         if(fac8!=null)
                        {
                            sLog.debug("Got UUCP/Fac8:"+fac8.toString());
                            facList.add(fac8.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Crond))
                    {
                        Integer fac9= (Integer)summaryMap.get(ViewerGlobals.fac9);
                        if(fac9!=null)
                        {
                            sLog.debug("Got Crond/Fac9:"+fac9.toString());
                            facList.add(fac9.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Authority))
                    {
                         Integer fac10= (Integer)summaryMap.get(ViewerGlobals.fac10);
                         if(fac10!=null)
                         {
                            sLog.debug("Got Authority/Fac10:"+fac10.toString());
                            facList.add(fac10.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.FTP))
                    {
                        Integer fac11= (Integer)summaryMap.get(ViewerGlobals.fac11);
                        if(fac11!=null)
                        {
                            sLog.debug("Got FTP/Fac11:"+fac11.toString());
                            facList.add(fac11.toString());
                        }
                    }
                     if(facVal.equals(RPConstants.NTP))
                    {
                         Integer fac12= (Integer)summaryMap.get(ViewerGlobals.fac12);
                        if(fac12!=null)
                        {
                            sLog.debug("Got NTP/Fac12:"+fac12.toString());
                            facList.add(fac12.toString());
                        }
                     }
                    if(facVal.equals(RPConstants.Audit))
                    {
                        Integer fac13= (Integer)summaryMap.get(ViewerGlobals.fac13);
                        if(fac13!=null)
                        {
                            sLog.debug("Got Audit/Fac13:"+fac13.toString());
                            facList.add(fac13.toString());
                        }
                    }
                     if(facVal.equals(RPConstants.Alert))
                    {
                        Integer fac14= (Integer)summaryMap.get(ViewerGlobals.fac14);
                        if(fac14!=null)
                        {
                            sLog.debug("Got Alert/Fac14:"+fac14.toString());
                            facList.add(fac14.toString());
                        }
                     }
                    if(facVal.equals(RPConstants._Crond))
                    {
                        Integer fac15= (Integer)summaryMap.get(ViewerGlobals.fac15);
                        if(fac15!=null)
                        {
                            sLog.debug("Got Crond2/Fac15:"+fac15.toString());
                            facList.add(fac15.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local0))
                    {
                        Integer fac16= (Integer)summaryMap.get(ViewerGlobals.fac16);
                        if(fac16!=null)
                        {
                            sLog.debug("Got Local0/Fac16:"+fac16.toString());
                            facList.add(fac16.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local1))
                    {
                        Integer fac17= (Integer)summaryMap.get(ViewerGlobals.fac17);
                        if(fac17!=null)
                        {
                            sLog.debug("Got Local1/Fac17:"+fac17.toString());
                            facList.add(fac17.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local2))
                    {
                        Integer fac18= (Integer)summaryMap.get(ViewerGlobals.fac18);
                        if(fac18!=null)
                        {
                            sLog.debug("Got Local2/Fac18:"+fac18.toString());
                            facList.add(fac18.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local3))
                    {
                        Integer fac19= (Integer)summaryMap.get(ViewerGlobals.fac19);
                        if(fac19!=null)
                        {
                            sLog.debug("Got Local3/Fac19:"+fac19.toString());
                            facList.add(fac19.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local4))
                    {
                        Integer fac20= (Integer)summaryMap.get(ViewerGlobals.fac20);
                        if(fac20!=null)
                        {
                            sLog.debug("Got Fac20:"+fac20.toString());
                            facList.add(fac20.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local5))
                    {
                        Integer fac21= (Integer)summaryMap.get(ViewerGlobals.fac21);
                        if(fac21!=null)
                        {
                            sLog.debug("GotLocal5/ Fac21:"+fac21.toString());
                            facList.add(fac21.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local6))
                    {
                        Integer fac22= (Integer)summaryMap.get(ViewerGlobals.fac22);
                        if(fac22!=null)
                        {
                            sLog.debug("Got Local6/Fac22:"+fac22.toString());
                            facList.add(fac22.toString());
                        }
                    }
                    if(facVal.equals(RPConstants.Local7))
                    {
                        Integer fac23= (Integer)summaryMap.get(ViewerGlobals.fac23);
                        if(fac23!=null)
                        {
                             sLog.debug("Got Local7/Fac23:"+fac23.toString());
                            facList.add(fac23.toString());
                        }
                    }         
                }
                
                Iterator sit = pSeverityList.iterator();
                while(sit.hasNext())
                {
                     String sevVal = ((Integer)sit.next()).toString();
                    
                    if(sevVal.equals(RPConstants.Emergency))
                    {
                        Integer sev0= (Integer)summaryMap.get(ViewerGlobals.sev0);
                        if(sev0!=null)
                        {
                            sLog.debug("Got Emergency/Sev0:"+sev0.toString());
                            sevList.add(sev0.toString());
                        }
                    }
                     if(sevVal.equals(RPConstants.Severity_Alert))
                    {
                        Integer sev1= (Integer)summaryMap.get(ViewerGlobals.sev1);
                        if(sev1!=null)
                        {
                            sLog.debug("Got Alert/Sev1:"+sev1.toString());
                            sevList.add(sev1.toString());
                         }
                     }
                      if(sevVal.equals(RPConstants.Critical))
                    {
                        Integer sev2= (Integer)summaryMap.get(ViewerGlobals.sev2);
                        if(sev2!=null)
                        {
                         sLog.debug("Got Critical/Sev2:"+sev2.toString());
                            sevList.add(sev2.toString());
                        }
                      }
                     if(sevVal.equals(RPConstants.Error))
                    {
                        Integer sev3= (Integer)summaryMap.get(ViewerGlobals.sev3);
                        if(sev3!=null)
                        {
                            sLog.debug("Got Error/Sev3:"+sev3.toString());
                            sevList.add(sev3.toString());
                         }
                     }
                      if(sevVal.equals(RPConstants.Warning))
                    {
                        Integer sev4= (Integer)summaryMap.get(ViewerGlobals.sev4);
                        if(sev4!=null)
                        {
                            sLog.debug("Got Warning/Sev4:"+sev4.toString());
                            sevList.add(sev4.toString());
                        }
                      }
                      if(sevVal.equals(RPConstants.Notice))
                    {
                        Integer sev5= (Integer)summaryMap.get(ViewerGlobals.sev5);
                        if(sev5!=null)
                        {
                            sLog.debug("Got Notice/Sev5:"+sev5.toString());
                            sevList.add(sev5.toString());
                        }
                      }
                     if(sevVal.equals(RPConstants.Info))
                    {
                        Integer sev6= (Integer)summaryMap.get(ViewerGlobals.sev6);
                        if(sev6!=null)
                        {
                            sLog.debug("Got Info/Sev6:"+sev6.toString());
                            sevList.add(sev6.toString());
                        }
                     }
                     if(sevVal.equals(RPConstants.Debug))
                    {
                        Integer sev7= (Integer)summaryMap.get(ViewerGlobals.sev7);
                        if(sev7!=null)
                        {
                            sLog.debug("Got Debug/Sev7:"+sev7.toString());
                            sevList.add(sev7.toString());
                        }
                     }                
                }
                
                sLog.debug("creating - SummaryRecordVO");
             
                //SummaryRecordVO vo = new SummaryRecordVO(hostName,domainName,vendorName,deviceType,
                  //                              startDate,endDate,facList,sevList);
                SummaryRecordVO vo = new SummaryRecordVO(hostName,domainName,patternName,
                                                startDate,endDate,facList,sevList);
    
                     viewSummaryList.add(vo);
            }
            
            viewCursor = new SummaryCursor(SummaryQueryEngine.getPageSize(),viewSummaryList); 
            
            return viewCursor;
            
        }
        public static  String [] mapSeverities(String [] pSelectedSeverities) 
        {
         if(pSelectedSeverities!=null)
         {
                int listlen = pSelectedSeverities.length;
                String [] mappedSevs = new String [listlen];
            
            for(int i=0;i<listlen;i++)
            {
                String sev = pSelectedSeverities[i];
                
                if(sev.equals("0"))
                      mappedSevs[i]=ViewerGlobals.sev0;
                if(sev.equals("1"))
                           mappedSevs[i]=ViewerGlobals.sev1;                          
                if(sev.equals("2"))
                           mappedSevs[i]=ViewerGlobals.sev2;
                if(sev.equals("3"))
                           mappedSevs[i]=ViewerGlobals.sev3;
                if(sev.equals("4"))
                           mappedSevs[i]=ViewerGlobals.sev4;
                if(sev.equals("5"))
                           mappedSevs[i]=ViewerGlobals.sev5;
                if(sev.equals("6"))
                           mappedSevs[i]=ViewerGlobals.sev6;  
                 if(sev.equals("7"))
                           mappedSevs[i]=ViewerGlobals.sev7;  
            }
                return mappedSevs;
         }
            return null;
            
        }
        public static  String [] mapFacilities(String [] pSelectedFacilities)
        {
          if(pSelectedFacilities!=null)
          {
            int listlen = pSelectedFacilities.length;
            String [] mappedFacs = new String [listlen];
           
            for(int i=0;i<listlen;i++)
            {
                String fac = pSelectedFacilities[i];
                
                if(fac.equals("0"))
                      mappedFacs[i]=ViewerGlobals.fac0;
                if(fac.equals("1"))
                      mappedFacs[i]=ViewerGlobals.fac1;
                if(fac.equals("2"))
                      mappedFacs[i]=ViewerGlobals.fac2;
                if(fac.equals("3"))
                      mappedFacs[i]=ViewerGlobals.fac3;
                if(fac.equals("4"))
                      mappedFacs[i]=ViewerGlobals.fac4;
                if(fac.equals("5"))
                      mappedFacs[i]=ViewerGlobals.fac5;
                if(fac.equals("6"))
                      mappedFacs[i]=ViewerGlobals.fac6;
                if(fac.equals("7"))
                      mappedFacs[i]=ViewerGlobals.fac7;
                if(fac.equals("8"))
                      mappedFacs[i]=ViewerGlobals.fac8;
                if(fac.equals("9"))
                      mappedFacs[i]=ViewerGlobals.fac9;
                if(fac.equals("10"))
                      mappedFacs[i]=ViewerGlobals.fac10;
                if(fac.equals("11"))
                      mappedFacs[i]=ViewerGlobals.fac11;
                if(fac.equals("12"))
                      mappedFacs[i]=ViewerGlobals.fac12;
                if(fac.equals("13"))
                      mappedFacs[i]=ViewerGlobals.fac13;
                if(fac.equals("14"))
                      mappedFacs[i]=ViewerGlobals.fac14;
                if(fac.equals("15"))
                      mappedFacs[i]=ViewerGlobals.fac15;
                if(fac.equals("16"))
                      mappedFacs[i]=ViewerGlobals.fac16;
                if(fac.equals("17"))
                      mappedFacs[i]=ViewerGlobals.fac17;
                if(fac.equals("18"))
                      mappedFacs[i]=ViewerGlobals.fac18;
                if(fac.equals("19"))
                      mappedFacs[i]=ViewerGlobals.fac19;
                if(fac.equals("20"))
                      mappedFacs[i]=ViewerGlobals.fac20;
                if(fac.equals("21"))
                      mappedFacs[i]=ViewerGlobals.fac21;
                if(fac.equals("22"))
                      mappedFacs[i]=ViewerGlobals.fac22;
                if(fac.equals("23"))
                      mappedFacs[i]=ViewerGlobals.fac23;
            }
            return mappedFacs;
          }
           return null;
        }
	
}



