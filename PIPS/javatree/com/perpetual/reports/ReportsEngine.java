/*
 * ReportTemplate.java
 *
 * Created on September 26, 2003, 3:41 PM
 */

package com.perpetual.reports;

import java.util.Collection;
import java.util.List;
import java.util.Iterator;

import com.perpetual.util.PerpetualC2Logger;
import org.jdom.*;
/**
 *
 * @author  brunob
 */
public abstract class ReportsEngine implements ExportFormatAdapter
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(ReportsEngine.class);    
   
     protected Iterator lAdapterIterator=null;
     protected static ReportsEngine instance=null;
     protected static String configFile=null;
    
    /** Creates a new instance of ReportTemplate */
    public ReportsEngine()
    {        
        init();        
    }       
    protected void init()
    {        
    }
    protected abstract List getAdapterList();
    
    protected static void refresh()
    {
    }
    public void writePage(RecordSet pRecordSet, Header pHeader, int pPageNumber, int pPageSize)
    {
        ExportFormatAdapter formatAdapter=null;
        sLog.debug("In ReportsEngine - Writing Page"+this);
        lAdapterIterator=getAdapterList().iterator();
        while(lAdapterIterator.hasNext())
        {
            Element adapter = (Element) lAdapterIterator.next();
            String active = adapter.getAttributeValue("active");
            if(active.equals("true"))
            {
                String classname = adapter.getAttributeValue("classname");
                sLog.debug("Loading Adapter:"+classname);
                try
                {
                    Class  cls = Class.forName(classname);
                    Object obj = cls.newInstance();
                    formatAdapter = (ExportFormatAdapter) obj;
                }
                catch(Exception excp)
                {                    
                    String msg = "Could not load ExportFormatAdapter for:"+classname;
                    sLog.error(msg);
                    excp.printStackTrace();
                }
                formatAdapter.writePage(pRecordSet,pHeader,pPageNumber,pPageSize);
            }
            //ExportFormatAdapter formatAdapter = (ExportFormatAdapter) lAdapterIterator.next();
            //formatAdapter.writePage(pRecordSet,pHeader,pPageNumber,pPageSize);
        }
    }

    
}
