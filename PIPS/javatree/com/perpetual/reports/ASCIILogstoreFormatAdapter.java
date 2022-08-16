/*
 * ASCIILogstoreFormatAdapter.java
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
import com.perpetual.logserver.LogSystem;
import java.util.Iterator;
import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.text.MessageFormat;

/**
 *
 * @author  brunob
 */
public class ASCIILogstoreFormatAdapter extends ASCIIFormatAdapter
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(ASCIILogstoreFormatAdapter.class);
    /** Creates a new instance of ASCIILogstoreFormatAdapter */
    public ASCIILogstoreFormatAdapter() 
    {
         super();
        setOutputFile();
    }
    public void writePage(RecordSet pRecordSet,Header pHeader,int pPageNumber,int pPageSize)
    {
        String sPage = new Integer(pPageNumber).toString();
                
        sLog.debug("In ASCIILogStore Format Adapter - writePage....");
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
            Iterator i = lines.iterator();
            while (i.hasNext())
            {
               
                            String record = new String();
                            Record line = (Record)i.next();
                            Collection fieldList = line.getFieldList();
                            Iterator fit = fieldList.iterator();                        
                            while (fit.hasNext())
                            {
                                    Field field = (Field) fit.next();
                                    String name = field.getFieldName(), value = field.getValue();
                                     
                                    if (fit.hasNext())
                                    {
                                        if(columnTitlePass==false)
                                              columnHeader+=name+"|";
                                         record+=value+",";
                                    }
                                    else
                                    {
                                            // last one is the message and needs to be in a cdata
                                            //
                                            if(columnTitlePass==false)
                                            {
                                                  columnHeader+=name+"\n";
                                                  columnTitlePass=true;
                                                  os.write(columnHeader.getBytes());
                                            }
                                            record+=value+"\n";
                                    }
                            }
                            os.write(record.getBytes());
                    }
                    os.close();
        }      
        catch(Exception excp)
        {
            String msg = "could not export Log Report !";
            sLog.error(msg);
            excp.printStackTrace();
        }
        
    }
}
