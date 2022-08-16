/*
 * SummaryQueryEngine.java
 *
 * Created on July 22, 2003, 9:13 PM
 */

package com.perpetual.rp.control.query;

import com.perpetual.exception.BasePerpetualException;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.log.LogFilter;

import com.perpetual.rp.control.RPConfigManager;
import com.perpetual.rp.control.RecordProcessorManager;
import com.perpetual.rp.control.RecordProcessor;
import com.perpetual.rp.control.SyslogRecordProcessor;
import com.perpetual.rp.control.RecordProcessorServer;
import com.perpetual.rp.util.navigator.INavigator; 
import com.perpetual.rp.control.DomainCriteriaMap;


import java.util.List;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author  brunob
 */
public class SummaryQueryEngine implements ISummaryCollectionQuery
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( SummaryQueryEngine.class );    
    
    private RPConfigManager lConfigManager=null;
    private RecordProcessorManager lRPManager=null;
    private RecordProcessor lRP=null;
    private INavigator lNavigator=null;
    private DomainCriteriaMap lCriteriaMap=null;
    private static SummaryQueryEngine lInstance=null;
    
    /** Creates a new instance of SummaryQueryEngine */
    private  SummaryQueryEngine() 
    {
    }
    public static SummaryQueryEngine getInstance()
    {
        if(lInstance==null)
            lInstance = new SummaryQueryEngine();
        return lInstance;
    }
    
    public List getSummaryRecords(SummaryQueryTemplate pTemplate, boolean pLogStore) throws BasePerpetualException 
    {
        List recordList=null;
        sLog.debug("getting SummaryRecords");
        LogFilter  filter =null;
        HashMap pQueryMap = new HashMap();
       
           pQueryMap.put("startTime",(Date) pTemplate.getStartDate()); 
           pQueryMap.put("maxTime:",(Date) pTemplate.getEndDate());
           pQueryMap.put("Facility", (List) pTemplate.getFacilityList());
           pQueryMap.put("Severity", (List) pTemplate.getSeverityList());
           pQueryMap.put("Host",(List) pTemplate.getHostList());
           if(pLogStore)
           {
                //pQueryMap.put("types", new Object[] { "archive" });
                filter = new LogFilter(pQueryMap);                
                recordList = getRecordsFromLogstore(pTemplate.getStartDate(),pTemplate.getEndDate());
           }    
           else
           {
                filter = new LogFilter(pQueryMap);                
                recordList = getRecordsFromDB(filter,pTemplate.getVendorTemplate());
           }  
            
        
            return recordList;
        
    }
    private List getRecordsFromLogstore(Date pStartDate,Date pEndDate)
    {
        List recordVoList=null;
        sLog.debug("About to Query the Logstore !");
        sLog.debug(" getting ConfigManager !");        
        lConfigManager = RecordProcessorServer.getStaticConfigManager();
        sLog.debug("Initialisating Navigator");
        lNavigator = RecordProcessorServer.getStaticNavigator();
         
        sLog.debug("Initiaiting Record Processor Manager");
        lRPManager = new RecordProcessorManager(null,lConfigManager,lNavigator);
        recordVoList =  lRPManager.collectSummaryRecordVOs(pStartDate,pEndDate);
        
        return recordVoList;
    }
    private List getRecordsFromDB(LogFilter pFilter,VendorQueryTemplate pVendorTemplate)
    {
        sLog.debug("About to hit the DB for records !!!");
        return null;
    }
}


