/*
 * XMLSummaryAdapter.java
 *
 * Created on October 2, 2003, 5:04 PM
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
public class XMLSummaryFormatAdapter extends XMLFormatAdapter
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(XMLSummaryFormatAdapter.class);
       
    /** Creates a new instance of XMLSummaryAdapter */
    public XMLSummaryFormatAdapter()
    {
         super();
        //setOutputFile();
    }
    public void writePage(RecordSet pRecordSet,Header pHeader,int pPageNumber,int pPageSize)
    {
        String sPage = new Integer(pPageNumber).toString();    
        sLog.debug("In XMLSummaryFormatAdapter - writePage....");
       
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
                       
            Iterator rowIterator = lines.iterator();
            while(rowIterator.hasNext())
            {
                Record record  = (Record) rowIterator.next();
                Element lineElem = new Element("record");
                Element sevsElem = new Element("Severities");
                Element facsElem = new Element("Facilitites");
                Collection fieldList = record.getFieldList();
                Iterator fit = fieldList.iterator();                        
                while (fit.hasNext())
                {
                        Field field = (Field) fit.next();
                        String name = field.getFieldName(), value = field.getValue(); 
                        
                        if(name.equals("Kernel") || name.equals("User") || name.equals("Mail") || name.equals("Daemon") ||
                            name.equals("Security") || name.equals("Syslog") || name.equals("Lpr") || name.equals("News") ||
                            name.equals("UUCP")||name.equals("Crond")||name.equals("Authority")||name.equals("FTP")||
                            name.equals("NTP")||name.equals("Audit")||name.equals("Alert")||name.equals("Crond2")||
                            name.equals("Local0")||name.equals("Local1")||name.equals("Local2")||name.equals("Local3")||
                            name.equals("Local4")||name.equals("Local5")||name.equals("Local6")||name.equals("Local7"))
                            facsElem.setAttribute(name,value);
                        else if(name.equals("Emergency") ||name.equals("Alert")||name.equals("Critical")||name.equals("Error")||
                            name.equals("Warning") || name.equals("Notice")||name.equals("Info")||name.equals("Debug"))
                           sevsElem.setAttribute(name,value);
                       else              
                        lineElem.setAttribute(name, value);
                }
                    lineElem.addContent(sevsElem);
                    lineElem.addContent(facsElem);
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
            }
            finally
            {
                    os.close();
            }

     }
     catch(Exception excp)
     {
         String errMsg="Error trying to write query result to report file !";
         sLog.error(errMsg,excp);
         excp.printStackTrace();
     }
}
    
}
