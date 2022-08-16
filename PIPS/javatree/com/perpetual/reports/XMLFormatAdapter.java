/*
 * XMLFormatAdapter.java
 *
 * Created on October 2, 2003, 5:01 PM
 */

package com.perpetual.reports;

import java.io.File;
import java.io.OutputStream;
import java.io.FileOutputStream;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.logserver.LogSystem;
import java.util.Iterator;

/**
 *
 * @author  brunob
 */
public abstract class XMLFormatAdapter implements ExportFormatAdapter
{
    protected String xmlHeader="<?xml version='1.0' encoding='iso-8859-1'?>";
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(XMLFormatAdapter.class);
    protected static File outputDir=null;
    protected static LogSystem logSystem=null;
    /** Creates a new instance of XMLFormatAdapter */
    public XMLFormatAdapter()
    {
       setOutputFile();   
    }
    public void writePage(RecordSet pRecordSet, Header pHeader, int pPageNumber, int pPageSize)
    {
    }
    protected String  getFormattedList(String [] pArray)
    {        
        String formattedList =new String("/");
        int length = pArray.length;
        
        for(int x=0;x<length;x++)
        {
           if(length==1)
                formattedList+= (String) pArray[x];
           else
           {               
            if(x+1<length)
                formattedList+= (String) pArray[x]+"/"; 
	    if(x+1==length)
                formattedList+= (String) pArray[x]; 
           }
        }
                   
        return formattedList;
    }
    protected void setOutputFile()
    {
        sLog.debug("setting Output Report Dir");
        try
        {
            if(logSystem==null)
            {
                logSystem = LogSystem.getDefault();
                if(outputDir==null)
                {
                    String reportDir = System.getProperty("reportdir");
		    outputDir = reportDir != null ? new File(reportDir) : new File(logSystem.getParam("LogReportOutputDir"));
		   //outputDir = new File(logSystem.getParam("LogReportOutputDir"));
                }
            }
        }
        catch(Exception excp)
        {
            sLog.error("Could not get instance of LogSystem config file !");
            excp.printStackTrace();
        }
        
    }
}
