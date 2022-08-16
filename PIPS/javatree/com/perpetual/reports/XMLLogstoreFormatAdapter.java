/*
 * LogstoreFormatAdapter.java
 *
 * Created on October 2, 2003, 5:03 PM
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
public class XMLLogstoreFormatAdapter extends XMLFormatAdapter
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(XMLLogstoreFormatAdapter.class);
                 
    
    /** Creates a new instance of LogstoreFormatAdapter */
    public XMLLogstoreFormatAdapter() 
    {
        super();
        //setOutputFile();
    }
           
    public void writePage(RecordSet pRecordSet,Header pHeader,int pPageNumber,int pPageSize)
    {
        String sPage = new Integer(pPageNumber).toString();
                
        sLog.debug("In XMLLogStore Format Adapter - writePage....");
        try
        {
            Element report = new Element("Report");
            report.setAttribute("Name",pHeader.getName());
            report.setAttribute("StartDate",pHeader.getStartDate().toString());
            report.setAttribute("EndDate",pHeader.getEndDate().toString());
            Element hosts = new Element("Hosts");
            hosts.setAttribute("list",getFormattedList(pHeader.getHosts()));
            report.addContent(hosts);
            Element sList = new Element("Severities");
            sList.setAttribute("list",getFormattedList(pHeader.getSeverities()));
            report.addContent(sList);
            Element fList = new Element("Facilities");
            fList.setAttribute("list",getFormattedList(pHeader.getFacilities()));
            report.addContent(fList);
            Element messages = new Element("MessagePatterns");
            messages.setAttribute("list",getFormattedList(pHeader.getMessagePatterns()));
            report.addContent(messages);

            Element pageElem = new Element("page");
            pageElem.setAttribute("page", Integer.toString(pPageNumber));
            pageElem.setAttribute("firstRow", Integer.toString(pPageNumber*pPageSize));        

            Collection lines = pRecordSet.getRecordCollection();
            Iterator i = lines.iterator();
            while (i.hasNext())
            {
                            Element lineElem = new Element("record");
                            Record line = (Record)i.next();
                            Collection fieldList = line.getFieldList();
                            Iterator fit = fieldList.iterator();                        
                            while (fit.hasNext())
                            {
                                    Field field = (Field) fit.next();
                                    String name = field.getFieldName(), value = field.getValue();
                                    if (fit.hasNext())
                                            lineElem.setAttribute(name, value);
                                    else
                                    {
                                            // last one is the message and needs to be in a cdata
                                            //
                                            lineElem.addContent(new CDATA(value));
                                    }
                            }
                            pageElem.addContent(lineElem);
                    }
                    pageElem.setAttribute("lastRow", Integer.toString(((pPageSize*pPageNumber)+pPageSize) - 1));
                    report.addContent(pageElem);
                    File dir = new File(outputDir, MessageFormat.format(logSystem.getParam("LogReportDirFormat"),new Object[] { pHeader.getName()+"_xml", pHeader.getWhenDate(), pHeader.getStartDate(), pHeader.getEndDate() }));
                    dir.mkdirs();                    
                    File file = new File(dir, MessageFormat.format(logSystem.getParam("LogReportFileFormat"),new Object[] { pHeader.getName(), pHeader.getWhenDate(), pHeader.getStartDate(), pHeader.getEndDate(),sPage}));
                    OutputStream os = new FileOutputStream(file);
                    os.write(xmlHeader.getBytes());
                    os.write(new String("\n").getBytes());
                    try
                    {
                            XMLOutputter xmlOutputter = new XMLOutputter("\011", true);
                            xmlOutputter.setLineSeparator("\n");
                            //		xmlOutputter.setTextNormalize(false);
                            xmlOutputter.setTextTrim(true);
                            xmlOutputter.output(report, os);
                            //xmlOutputter.output(pageElem, os);
                    }
                    finally
                    {
                            os.close();
                    }
        }
        catch(Exception excp)
        {
            String msg = "could not export Log Report !";
            sLog.error(msg);
            excp.printStackTrace();
        }
        
    }
    
}
