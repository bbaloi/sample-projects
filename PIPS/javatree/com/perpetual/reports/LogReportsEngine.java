/*
 * LogReportTemplate.java
 *
 * Created on September 28, 2003, 7:24 PM
 */

package com.perpetual.reports;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.*;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ResourceLoader;

import com.perpetual.util.Environment;



/**
 *
 * @author  brunob
 */
public class LogReportsEngine extends ReportsEngine
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(LogReportsEngine.class);  
    private static Element exports=null;
     private static List lAdapterList=null;
   //protected static  Adapter Loader - you construct only once
    
    /** Creates a new instance of LogReportTemplate */
    private LogReportsEngine() 
    {
        super();
        //load up the appropriate classes for processing & fomratting
    }
    protected List getAdapterList()
    {
        return lAdapterList;
    }
    public static ReportsEngine getInstance()
    {
        if(instance==null)
            instance = new LogReportsEngine();
        else
            refresh();
        
        return instance;
    }
        
    protected static void refresh()
    {
       sLog.info("Re-Initialising Log Reports Engine adapter list !");
       
       try
        {
        lAdapterList.clear();
        exports = ResourceLoader.loadResourceAsJdomElement(configFile);
        Element exportFormatAdapters = exports.getChild("ExportFormatAdapters");
        sLog.debug("got ExportFormatAdapters");
        Iterator groupsIt = exportFormatAdapters.getChildren("Group").iterator();
        sLog.debug("Got Groups !");
        while(groupsIt.hasNext())
        {
            Element group = (Element)groupsIt.next();
            String name = group.getAttributeValue("name");
            if(name.equals("Logstore"))
            {
                sLog.debug("Found Logstore group of adapters");
                Iterator adaptersIt = group.getChildren("Adapter").iterator();
                while(adaptersIt.hasNext())
                {
                     Element adapter = (Element) adaptersIt.next();
                     lAdapterList.add(adapter);
                }
                break;
            }
        }
        }
        catch(Exception excp)
        {
            String msg = "Could not load config file for Exports !";
            sLog.error(msg);
            excp.printStackTrace();
        }
       
   }
       
   protected void init()
   {
       sLog.info("Initialising Log Reports Engine !");
       lAdapterList = new ArrayList();
       try
        {
        configFile = Environment.getExportsConfigPath();
        exports = ResourceLoader.loadResourceAsJdomElement(configFile);
        Element exportFormatAdapters = exports.getChild("ExportFormatAdapters");
        sLog.debug("got ExportFormatAdapters");
        Iterator groupsIt = exportFormatAdapters.getChildren("Group").iterator();
        sLog.debug("Got Groups !");
        while(groupsIt.hasNext())
        {
            Element group = (Element)groupsIt.next();
            String name = group.getAttributeValue("name");
            if(name.equals("Logstore"))
            {
                sLog.debug("Found Logstore group of adapters");
                Iterator adaptersIt = group.getChildren("Adapter").iterator();
                while(adaptersIt.hasNext())
                {
                     Element adapter = (Element) adaptersIt.next();
                     lAdapterList.add(adapter);
                }
                break;
            }
        }
        }
        catch(Exception excp)
        {
            String msg = "Could not load config file for Exports !";
            sLog.error(msg);
            excp.printStackTrace();
        }
       
    }
    
}
