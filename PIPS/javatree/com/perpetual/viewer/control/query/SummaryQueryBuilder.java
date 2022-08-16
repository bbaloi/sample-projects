/*
 * SummaryQueryBuilder.java
 *
 * Created on August 2, 2003, 6:11 PM
 */

package com.perpetual.viewer.control.query;


import java.util.Map;

import com.perpetual.util.Constants;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.model.vo.VendorCriterionVO;
import com.perpetual.viewer.util.ViewerGlobals;
import com.perpetual.viewer.control.SummaryLogicDescriptor;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.sql.Timestamp;

import java.util.Collection;
import java.util.Iterator;

import com.perpetual.exception.BasePerpetualException;
/**
 *
 * @author  brunob
 */
public class SummaryQueryBuilder extends AbstractQueryBuilder
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(SummaryQueryBuilder.class);

    /** Creates a new instance of SummaryQueryBuilder */
    public SummaryQueryBuilder() 
    {
    }
    
    public String constructQuery(Map pMap,Map pParameters) throws BasePerpetualException
    {
        //String finalQuery="SELECT id,startdate,enddate,hostname,domainname,vendorname,devicetype";
        String finalQuery="SELECT id,startdate,enddate,hostname,domainname,messagepatternid";
        String view=null;
        String columns=null;
        String facilityViewList=null,severityViewList=null,severityWhereList=null,facilityWhereList=null;
        String  messagePatternList=null,hostsWhereList=null;
        String severities [], facilities [];
        boolean sevsEmpty=true,facsEmpty=true,msgEmpty=true,hostsEmpty=true;
        
        
        String from = " FROM summary ";
                
        sLog.debug("Constructing the Query !");
        //Construct query from parameters
        Timestamp startDate = formatSqlTimestamp((Date) pMap.get(Constants.StartDate));
        Timestamp  endDate = formatSqlTimestamp((Date) pMap.get(Constants.EndDate));        
        pParameters.put(Constants.StartDate,startDate);
        pParameters.put(Constants.EndDate,endDate);
        sLog.debug("-----------------------------------------");
        sLog.debug("StartSqlTimestamp:"+startDate.toString());
        sLog.debug("EndSqlTimestamp:"+endDate.toString());     
        sLog.debug("-----------------------------------------");
        SummaryLogicDescriptor logicDescriptor = (SummaryLogicDescriptor) pMap.get(Constants.SummaryLogicDescriptor);
        boolean AndFacilities = logicDescriptor.getFacilitiesAnd();
        boolean AndSeverities = logicDescriptor.getSeveritiesAnd();
        //boolean AndMessagePatterns = logicDescriptor.getMessagePatternsAnd();
        boolean FacilitiesOrSeverities = logicDescriptor.getFacilitesOrSeverities();
        boolean FacilitiesOrMessages = logicDescriptor.getFacilitiesOrMessages();
        boolean SeveritiesOrMessages = logicDescriptor.getSeveritiesOrMessages();         
        
        Object hostsObj = pMap.get(Constants.HostList);
        if(hostsObj!=null)
        {
            String [] hosts = (String []) hostsObj;
            if(hosts.length>0)
            {
                hostsEmpty=false;                
                hostsWhereList=constructHostWhereList(hosts);  
            }
        }
        else
            throw new BasePerpetualException("You need to select a host !!!");
        
        Object facilitiesObj=pMap.get(Constants.FacilityList);
        if(facilitiesObj!=null)
        {
               facilities = (String []) facilitiesObj;
               if(facilities.length>0)
               {
                  facsEmpty=false;
                facilityViewList =  constructFacilityViewList(facilities);
                facilityWhereList = constructFacilityWhereList(facilities,AndFacilities);
               }
        }
       Object severitiesObj = pMap.get(Constants.SeverityList);
       if(severitiesObj !=null)
       {           
            severities = (String [])severitiesObj;  
            if(severities.length >0)
           {
               sevsEmpty=false;
            severityViewList = constructSeverityViewList(severities);
            severityWhereList = constructSeverityWhereList(severities,AndSeverities);
           }
       }
       Object messagePatternObj=pMap.get(Constants.MessagePattern);
       if(messagePatternObj!=null)
       {
           msgEmpty=false;
           Collection msgPatterns = (Collection) messagePatternObj;
           messagePatternList = constructWhereMessagePatternList(msgPatterns);
       }       
       
       if(facilityViewList!=null)
            finalQuery+=","+facilityViewList;
       if(severityViewList!=null)
            finalQuery+=","+severityViewList;       
        finalQuery+=from+"WHERE startdate>=? AND enddate<=?";
        //String hostWhereList = constructHostWhereList(hosts);
        if(hostsWhereList!=null)
            finalQuery+=" AND ("+hostsWhereList+")";
        
        //if(facilityWhereList!=null && severityWhereList!=null)
        if(facsEmpty && sevsEmpty && msgEmpty)
        {
            sLog.debug("facs=null & sevs=null & msg=null");
        }        
        else if(!facsEmpty && !sevsEmpty && !msgEmpty)
        {
            sLog.debug("facs=!null & sevs=!null & msgs!=null");
             finalQuery+=" AND (("+facilityWhereList+")";               
             if(!FacilitiesOrSeverities)
                finalQuery+=" AND ";
            else
                finalQuery+=" OR ";
            finalQuery+="("+severityWhereList+")";
            if(!SeveritiesOrMessages)
                finalQuery+=" AND ";
            else
                finalQuery+=" OR ";
            
                finalQuery+="("+messagePatternList+"))";
        }              
        else if(facsEmpty && !sevsEmpty && !msgEmpty)
         {
             sLog.debug("facs=null & sevs=!null & msgs!=null");
             finalQuery+=" AND ("+severityWhereList+")";
             if(!SeveritiesOrMessages)
                 finalQuery+=" And";
             else
                finalQuery+="OR ";
             
             finalQuery+="("+messagePatternList+"))";
             
         }
        else if(facsEmpty && sevsEmpty && !msgEmpty)         
        {
            sLog.debug("facs=null & sevs==null & msgs!=null");
            finalQuery+=" AND ("+messagePatternList+")";            
        }
        else if(!facsEmpty && sevsEmpty && msgEmpty)         
        {
            sLog.debug("facs!=null & sevs=null & msgs=null");
            finalQuery+=" AND ("+facilityWhereList+")";
        }
        else if(!facsEmpty && !sevsEmpty && msgEmpty)         
        {
            sLog.debug("facs=!null & sevs=!null & msgs=null");
             finalQuery+=" AND (("+facilityWhereList+") ";               
             if(!FacilitiesOrSeverities)
                finalQuery+="AND ";
            else
                finalQuery+="OR ";
            finalQuery+="("+severityWhereList+"))";
        }
        else if(!facsEmpty && sevsEmpty && !msgEmpty)         
        {
            sLog.debug("facs=!null & sevs=null & msgs!=null");
             finalQuery+=" AND (("+facilityWhereList+") ";               
             if(!FacilitiesOrMessages)
                finalQuery+="AND ";
            else
                finalQuery+="OR ";
            finalQuery+="("+messagePatternList+"))";
        }
        else if(facsEmpty && !sevsEmpty && msgEmpty)         
        {
            sLog.debug("facs=null & sevs=!null & msgs=null");
            finalQuery+=" AND (("+severityWhereList+")) "; 
        }
                
       /*                        
        if(hosts.length==0)
        {
            sLog.debug("This is a Vendor Criteria search");
            String [] domainList =  (String []) pMap.get(Constants.DomainList);
            VendorCriterionVO [] vendorCriterionList = (VendorCriterionVO []) pMap.get(Constants.VendorCriteria);
            sLog.debug("adding ");
        }
        else
        {
            String hostWhereList = constructHostWhereList(hosts);
            finalQuery+="AND ("+hostWhereList+")";
        }*/        
        //sLog.debug("Query Built:"+finalQuery);
        return finalQuery;
    }
    private Timestamp formatSqlTimestamp(Date pDate)
    {
        Timestamp tStamp=null;
        /*Calendar cal = Calendar.getInstance();
        cal.setTime(pDate);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month++;
        int day= cal.get(Calendar.DAY_OF_MONTH);
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        hour++;
        int minute = cal.get(Calendar.MINUTE);
        int second = cal.get(Calendar.SECOND);
        
        String tStamp=new Integer(year).toString()+"-"+new Integer(month).toString()+"-"+new Integer(day).toString()+" "+
                        new Integer(hour).toString()+":"+ new Integer(minute).toString()+":"+new Integer(second).toString();
         return Timestamp.valueOf(tStamp);
         */        
        tStamp = new Timestamp(pDate.getTime());
        return tStamp;
    }
    
    private String constructFacilityViewList(String [] pFacilities)
    {
        String view=new String();
        if(pFacilities !=null)
        {
            int listSize=pFacilities.length;
        
        for(int i=0;i<listSize;i++)
        {
            if((pFacilities[i]).equals(ViewerGlobals.fac0))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac1))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac2))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac3))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac4))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac5))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac6))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac7))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac8))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac9))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac10))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac11))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac12))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac13))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac14))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac15))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac16))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac17))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac18))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac19))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac20))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac21))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac22))
            {
                view+=pFacilities[i];
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac23))
            {
                view+=pFacilities[i];
            }
            
            if(i+1<listSize)
                view+=",";
        }
        }
        else
        {
            view="";
        }
        return view;        
    }
     private String constructSeverityViewList(String [] pSeverities)
    {
        String view=new String();
        
        if(pSeverities!=null)
        {
         int listSize = pSeverities.length;
        
        for(int i=0;i<listSize;i++)
        {
           if((pSeverities[i]).equals(ViewerGlobals.sev0))
           {
                view+=pSeverities[i];
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev1))
           {
                view+=pSeverities[i];
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev2))
           {
                view+=pSeverities[i];
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev3))
           {
                view+=pSeverities[i];
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev4))
           {
                view+=pSeverities[i];
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev5))
           {
                view+=pSeverities[i];
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev6))
           {
                view+=pSeverities[i];
            }
            if((pSeverities[i]).equals(ViewerGlobals.sev7))
           {
                view+=pSeverities[i];
            }
            
            if(i+1<listSize)
                view+=",";
        }
        }
        else
        {
            view="";
        }
        return view;        
    }
      private String constructFacilityWhereList(String [] pFacilities,boolean pAndFacilities)
    {
        String view=new String();
        if(pFacilities!=null)
        {
         int listSize=pFacilities.length;
        
        for(int i=0;i<listSize;i++)
        {
            if((pFacilities[i]).equals(ViewerGlobals.fac0))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac1))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac2))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac3))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac4))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac5))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac6))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac7))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac8))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac9))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac10))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac11))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac12))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac13))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac14))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac15))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac16))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac17))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac18))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac19))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac20))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac21))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac22))
            {
                view+=pFacilities[i]+">0";
            }
            if((pFacilities[i]).equals(ViewerGlobals.fac23))
            {
                view+=pFacilities[i]+">0";
            }
            
            if(i+1<listSize)
            {
                if(pAndFacilities)
                    view+=" AND ";
                else
                    view+=" OR ";
            }
        }
        }
        else
        {
            view="";
        }
        return view;        
    }
      private String constructSeverityWhereList(String [] pSeverities,boolean pAndSeverities)
    {
        String view=new String();
        if(pSeverities!=null)
        {
         int listSize = pSeverities.length;
        
        for(int i=0;i<listSize;i++)
        {
           if((pSeverities[i]).equals(ViewerGlobals.sev0))
           {
                view+=pSeverities[i]+">0";
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev1))
           {
                view+=pSeverities[i]+">0";
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev2))
           {
                view+=pSeverities[i]+">0";
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev3))
           {
                view+=pSeverities[i]+">0";
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev4))
           {
                view+=pSeverities[i]+">0";
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev5))
           {
                view+=pSeverities[i]+">0";
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev6))
           {
                view+=pSeverities[i]+">0";
            }
           if((pSeverities[i]).equals(ViewerGlobals.sev7))
           {
                view+=pSeverities[i]+">0";
            }
            
            if(i+1<listSize)
            {
                if(pAndSeverities)
                    view+=" AND ";
                else
                    view+=" OR ";
            }
        }
        }
        else
        {
            view="";
        }
        return view;        
    }
    private String constructHostWhereList(String [] pHosts)
    {
        String where=new String();
        int listSize=pHosts.length;
        
        for(int i=0;i<listSize;i++)
        {
            where+="hostname='"+pHosts[i]+"'";
            
             if(i+1<listSize)
                where+=" OR ";
        }
            return where;
    }
    private String constructWhereMessagePatternList(Collection msgPatterns)
    {
        String finalList=new String();
        int size = msgPatterns.size();
        Iterator it = msgPatterns.iterator();
        int cntr=0;
        while(it.hasNext())
        {
            Integer patternId = (Integer) it.next();
            finalList+="messagepatternid="+"'"+patternId.toString()+"'";
            cntr++;
            if(cntr<size)
                 finalList+=" OR ";
        }
        return finalList;
    }
    public Object[] getQueryParameters(Map pMap) 
    {
        return null;
    }
    
}
