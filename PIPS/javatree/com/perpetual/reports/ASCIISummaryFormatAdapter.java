/*
 * ASCIISummaryFormatAdapter.java
 *
 * Created on November 3, 2003, 9:41 PM
 */

package com.perpetual.reports;

import org.jdom.*;
import org.jdom.output.*;

import com.perpetual.util.PerpetualC2Logger;

import java.util.Collection;
import org.apache.log4j.Logger;
import org.jdom.*;
import org.jdom.output.*;

import com.perpetual.reports.Record;
import com.perpetual.reports.RecordSet;
import com.perpetual.reports.Header;

//import com.perpetual.log.LogSystem;
import com.perpetual.log.LogSystem;
import java.util.Iterator;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.text.MessageFormat;

/**
 *
 * @author  brunob
 */
public class ASCIISummaryFormatAdapter extends ASCIIFormatAdapter
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(ASCIISummaryFormatAdapter.class);
    
    
    /** Creates a new instance of ASCIISummaryFormatAdapter */
    public ASCIISummaryFormatAdapter() 
    {
        super();
        setOutputFile();
    }
    public void writePage(RecordSet pRecordSet,Header pHeader,int pPageNumber,int pPageSize)
    {
         String sPage = new Integer(pPageNumber).toString();    
        sLog.debug("In ASCIISummaryFormatAdapter - writePage....");
       
        try
        {
            
            sLog.debug("---Opening file for writing---");
            File dir = new File(outputDir, MessageFormat.format(logSystem.getParam("LogReportDirFormat"),new Object[] { pHeader.getName()+"_ascii", pHeader.getWhenDate(), pHeader.getStartDate(), pHeader.getEndDate() }));
            dir.mkdirs();                    
            File file = new File(dir, MessageFormat.format(logSystem.getParam("LogReportFileFormat"),new Object[] { pHeader.getName(), pHeader.getWhenDate(), pHeader.getStartDate(), pHeader.getEndDate(),sPage}));
            OutputStream os = new FileOutputStream(file);         
            sLog.debug("Writing header info");
            String reportName   = "#Report Name: "+pHeader.getName()+"\n";
            String startDate    ="#Start Date: "+pHeader.getStartDate().toString()+"\n";
            String endDate      ="#End Date: "+pHeader.getEndDate().toString()+"\n";
            String hostList     ="#Hosts: "+getFormattedList(pHeader.getHosts())+"\n";
            String severityList ="#Severities: "+getFormattedList(pHeader.getSeverities())+"\n";
            String facilityList ="#Facilities: "+getFormattedList(pHeader.getFacilities())+"\n";
            String messagePatterns="#Message Patterns: "+getFormattedList(pHeader.getMessagePatterns())+"\n";
            String page   ="#Page: "+Integer.toString(pPageNumber);
            page+=" firstRow="+Integer.toString(pPageNumber*pPageSize);   
            page+=" lastRow="+ Integer.toString(((pPageSize*pPageNumber)+pPageSize) - 1)+"\n";
                    
            String columnHeader ="#";
              
            os.write(reportName.getBytes());
            os.write(startDate.getBytes());
            os.write(endDate.getBytes());
            os.write(hostList.getBytes());
            os.write(severityList.getBytes());
            os.write(facilityList.getBytes());
            os.write(messagePatterns.getBytes());
            os.write(page.getBytes());
            
            Collection lines = pRecordSet.getRecordCollection();                       
            Iterator rowIterator = lines.iterator();
            
            while(rowIterator.hasNext())
            {
                String record = new String();
                String severities= new String();
                String facilities = new String();
                Record line = (Record)rowIterator.next();
                Collection fieldList = line.getFieldList();
                Iterator fit = fieldList.iterator();         
                
                while (fit.hasNext())
                {
                        Field field = (Field) fit.next();
                        String name = field.getFieldName(), value = field.getValue(); 
                        if(columnTitlePass==false)
                                   columnHeader+=name+"|";
                                       
                        if(fit.hasNext())
                        {                        
                                if(name.equals("Kernel") || name.equals("User") || name.equals("Mail") || name.equals("Daemon") ||
                                    name.equals("Security") || name.equals("Syslog") || name.equals("Lpr") || name.equals("News") ||
                                    name.equals("UUCP")||name.equals("Crond")||name.equals("Authority")||name.equals("FTP")||
                                    name.equals("NTP")||name.equals("Audit")||name.equals("Alert")||name.equals("Crond2")||
                                    name.equals("Local0")||name.equals("Local1")||name.equals("Local2")||name.equals("Local3")||
                                    name.equals("Local4")||name.equals("Local5")||name.equals("Local6")||name.equals("Local7"))
                                {
                                    facilities+=value+",";
                                }
                                else if(name.equals("Emergency") ||name.equals("Alert")||name.equals("Critical")||name.equals("Error")||
                                    name.equals("Warning") || name.equals("Notice")||name.equals("Info")||name.equals("Debug"))
                                {
                                    severities+=value+",";
                                }
                               else              
                               {
                                record+=value+",";
                               }
                        }
                        else
                        {
                             if(name.equals("Kernel") || name.equals("User") || name.equals("Mail") || name.equals("Daemon") ||
                                    name.equals("Security") || name.equals("Syslog") || name.equals("Lpr") || name.equals("News") ||
                                    name.equals("UUCP")||name.equals("Crond")||name.equals("Authority")||name.equals("FTP")||
                                    name.equals("NTP")||name.equals("Audit")||name.equals("Alert")||name.equals("Crond2")||
                                    name.equals("Local0")||name.equals("Local1")||name.equals("Local2")||name.equals("Local3")||
                                    name.equals("Local4")||name.equals("Local5")||name.equals("Local6")||name.equals("Local7"))
                                {
                                    facilities+=value;
                                }
                                else if(name.equals("Emergency") ||name.equals("Alert")||name.equals("Critical")||name.equals("Error")||
                                    name.equals("Warning") || name.equals("Notice")||name.equals("Info")||name.equals("Debug"))
                                {
                                    severities+=value;
                                }
                               else              
                               {
                                record+=value;
                               }
                             
                             if(columnTitlePass==false)
                             {
                                     columnHeader+=name+"\n";
                                     columnTitlePass=true;
                                      os.write(columnHeader.getBytes());
                             }
                                            
                        }
                }
                   record+=facilities+severities+"\n";                               
                   
                   os.write(record.getBytes());
            }
                            
             os.close();
     }
     catch(Exception excp)
     {
         String errMsg="Error trying to write query result to report file !";
         sLog.error(errMsg,excp);
         excp.printStackTrace();
     }
    }
}
