/*
 * RPConfigManager.java
 *
 * Created on July 15, 2003, 1:23 PM
 */

package com.perpetual.rp.control;

import com.perpetual.rp.util.jaxb.RecordProcessorConfig;
import com.perpetual.rp.util.jaxb.SummaryCollection;
import com.perpetual.rp.util.jaxb.ServiceDomainList;
import com.perpetual.rp.util.jaxb.ServiceDomain;
import com.perpetual.rp.util.jaxb.CollectionIntervalValue;
import com.perpetual.rp.util.jaxb.CollectionCriteria;
import com.perpetual.rp.util.jaxb.HostList;
import com.perpetual.rp.util.jaxb.Host;
import com.perpetual.rp.util.jaxb.SeverityList;
import com.perpetual.rp.util.jaxb.FacilityList;
import com.perpetual.rp.util.jaxb.Severity;
import com.perpetual.rp.util.jaxb.Facility;
import com.perpetual.rp.util.jaxb.VendorCriteria;
import com.perpetual.rp.util.jaxb.VendorCriterion;
import com.perpetual.rp.util.jaxb.LastCollectionDate;


import java.io.File;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.text.StringCharacterIterator;
import java.util.List;
import java.util.Iterator;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.PerpetualResourceLoader;
import com.perpetual.exception.BasePerpetualException;
import javax.xml.marshal.XMLWriter;
import javax.xml.bind.Marshaller;
import java.io.FileOutputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 *
 * @author  brunob
 */
public class RPConfigManager 
{
    private PerpetualC2Logger sLog = new PerpetualC2Logger( RPConfigManager.class );    
   
    private String lTemplateFile=null;
    private RecordProcessorConfig lConfigMgr = null;
    private XMLWriter lWriter=null;
    private Marshaller lMarshaller=null;
    private Unmarshaller lUnmarshaller=null;
    private JAXBContext jc=null;
    /** Creates a new instance of RPConfigManager */
    public RPConfigManager(String pFileName)
    {
        sLog.debug("RPConfigManager constructor");
        lTemplateFile=pFileName;
        init();
    }
    private void init()
    {
        try
         {
           sLog.debug("Reading the RecordProcessor Collection Template !");
           // create a JAXBContext
            jc = JAXBContext.newInstance( "com.perpetual.rp.util.jaxb" );
            lMarshaller = jc.createMarshaller();
            lUnmarshaller = jc.createUnmarshaller();
            lConfigMgr = (RecordProcessorConfig) lUnmarshaller.unmarshal( PerpetualResourceLoader.getResourceAsStream(lTemplateFile));
          
            sLog.debug("got RecordProcessorConfig !");
            showTemplate();          
         }
         catch(Throwable excp)
         {
             excp.printStackTrace();
            //BasePerpetualException.handle("Error reading Collection Template File file !:",excp);
         }
     }
     public List getDomainList()
     {
         SummaryCollection summaryCollection = lConfigMgr.getSummaryCollection();
         sLog.debug("SummaryCollection:"+summaryCollection.getValue());
         ServiceDomainList lDomainList = summaryCollection.getServiceDomainList();
         List domainList = lDomainList.getServiceDomain();
         return domainList;
     }
     private void showTemplate()
     {
         sLog.debug("++++++++++Collection Template++++++++");
         SummaryCollection summaryCollection = lConfigMgr.getSummaryCollection();
         sLog.debug("SummaryCollection:"+summaryCollection.getValue());
         ServiceDomainList lDomainList = summaryCollection.getServiceDomainList();
         List domainList = lDomainList.getServiceDomain();
         Iterator it = domainList.iterator();
         sLog.debug("DomainList:");
         while(it.hasNext())
         {
             ServiceDomain domain = (ServiceDomain)it.next();
             CollectionIntervalValue collectionInterval = domain.getCollectionIntervalValue();
             sLog.debug("Collection Interval is:"+collectionInterval.getIntervalValue()+" "+
                                    collectionInterval.getTimeunit());
             CollectionCriteria collectionCriteria = domain.getCollectionCriteria();
             HostList lHostList=collectionCriteria.getHostList();
             SeverityList lSeverityList=collectionCriteria.getSeverityList();
             FacilityList lFacilityList=collectionCriteria.getFacilityList();
             
             sLog.debug("Host List:");
             List hostList = lHostList.getHost();
             Iterator hit = hostList.iterator();
             while(hit.hasNext())
             {
                 Host host = (Host) hit.next();
                 sLog.debug("+"+host.getHostName());
                 VendorCriteria vendorCriteria = (VendorCriteria) host.getVendorCriteria();
                 sLog.debug("++VendorCriteria:"+vendorCriteria.getName()+"-"+vendorCriteria.getType());
                                 
                 List criterionList = vendorCriteria.getVendorCriterion();
                 Iterator cit = criterionList.iterator();
                 while(cit.hasNext())
                 {
                    VendorCriterion vcx = (VendorCriterion) cit.next();
                    sLog.debug("+++"+vcx.getName()+"-"+vcx.getSelected());
                 }
             
             }   
              sLog.debug("Facility List:");
              List facilityList = lFacilityList.getFacility();
              Iterator fit = facilityList.iterator();
              while(fit.hasNext())
             {
                Facility facility = (Facility)fit.next();
                sLog.debug("+"+facility.getName()+"-"+facility.getSelected());
             }
               sLog.debug("Severity List:");
               List severityList = lSeverityList.getSeverity();
               Iterator sit = severityList.iterator();
               while(sit.hasNext())
             {
                Severity severity = (Severity)sit.next();
                sLog.debug("+"+severity.getName()+"-"+severity.getSelected());
             
             }
             
         }
     }
     public void resetLastCollectionTime(String pDomainName,String pCollectionTime)
     {
        List domainList = getDomainList();
        Iterator it = domainList.iterator();
        while(it.hasNext())
        {
            ServiceDomain domain = (ServiceDomain) it.next();
            if(pDomainName.equals(domain.getName()))
            {
                try
                {
                    LastCollectionDate lcd = domain.getLastCollectionDate();        
                    lcd.setContent(pCollectionTime);
                    domain.setLastCollectionDate(lcd);
                    lMarshaller.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE );
                    FileOutputStream stream = new FileOutputStream(lTemplateFile);
                    lMarshaller.marshal( lConfigMgr, stream);
                    //FileOutputStream stream = new FileOutputStream(lTemplateFile);
                    //lWriter =  new XMLWriter(stream,"US-ASCII",true);
                    //Marshaller marshaller = new Marshaller(lWriter);
                    //domain.marshal(marshaller);
                     stream.close();
                }
                catch(javax.xml.bind.PropertyException excp)
                {
                     sLog.error("JAXB setting property error");
                     excp.printStackTrace();
                }
                catch(javax.xml.bind.JAXBException excp)
                {
                    sLog.error("JAXB marshalling error");
                    excp.printStackTrace();
                }
                catch(java.io.IOException excp)
                {
                    sLog.error("Couldn't open rpcriteria.xml for writing !");
                    excp.printStackTrace();
                }
                break;
           }
        }
     }
}
