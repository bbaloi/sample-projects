/*
 * SummaryReportTemplate.java
 *
 * Created on September 28, 2003, 7:24 PM
 */

package com.perpetual.reports;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ResourceLoader;

import com.perpetual.util.Environment;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import org.jdom.*;
/**
 *
 * @author  brunob
 */
public class SummaryReportsEngine extends ReportsEngine
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(SummaryReportsEngine.class);    
    private static Element exports=null;
    private static List lAdapterList=null;
    
   
    /** Creates a new instance of SummaryReportTemplate */
    public SummaryReportsEngine() 
    {
        super();
    }
    protected List getAdapterList()
    {
        return lAdapterList;
    }
    protected void init()
    {
        sLog.info("Initialising Summary Reports Engine !");
        lAdapterList= new ArrayList();
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
            if(name.equals("Summary"))
            {
                sLog.debug("Found Summary group of adapters");
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
        
        //sLog.debug("Adding default XML Adapter !");
        //lAdapterList.add(new XMLSummaryFormatAdapter());
    }
  
    public static ReportsEngine getInstance()
    {
        if(instance==null)
            instance = new SummaryReportsEngine();
        else
           refresh();
        
        return instance;
    }
    protected static void refresh()
    {
        sLog.info("Re-Initialising Summary Reports Engine adapter list!");
        try
        {
            if(lAdapterList!=null)
                lAdapterList.clear();
            else 
                lAdapterList= new ArrayList();
        exports = ResourceLoader.loadResourceAsJdomElement(configFile);
        Element exportFormatAdapters = exports.getChild("ExportFormatAdapters");
        sLog.debug("got ExportFormatAdapters");
        Iterator groupsIt = exportFormatAdapters.getChildren("Group").iterator();
        sLog.debug("Got Groups !");
        while(groupsIt.hasNext())
        {
            Element group = (Element)groupsIt.next();
            String name = group.getAttributeValue("name");
            if(name.equals("Summary"))
            {
                sLog.debug("Found Summary group of adapters");
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
        
        //sLog.debug("Adding default XML Adapter !");
        //lAdapterList.add(new XMLSummaryFormatAdapter());
    }
    
}
