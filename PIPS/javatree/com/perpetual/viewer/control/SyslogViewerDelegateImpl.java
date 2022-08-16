/*
 * SyslogViewerImpl.java
 *
 * Created on May 16, 2003, 3:37 PM
 */

package com.perpetual.viewer.control;

import com.perpetual.viewer.model.SyslogDAO;
import java.util.Collection;
import com.perpetual.viewer.util.ViewerGlobals;
import com.perpetual.viewer.model.DAOFactory;
import com.perpetual.util.PerpetualC2Logger;
import java.util.HashMap;

import com.perpetual.viewer.control.query.AbstractQueryBuilder;
import com.perpetual.viewer.control.query.ConcreteQueryBuilder;
/**
 *
 * @author  brunob
 */
public class SyslogViewerDelegateImpl implements SyslogViewerDelegate
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( SyslogViewerDelegateImpl.class );
    private AbstractQueryBuilder lQueryBuilder=null;
    
   
    private SyslogDAO lDAO=null;
    /** Creates a new instance of SyslogViewerImpl */
    public SyslogViewerDelegateImpl() 
    {
        lQueryBuilder = new ConcreteQueryBuilder();
    }
      
    /** Creates a new instance of SyslogViewerDelegate  */
    public Collection readSyslog(String pFileName, HashMap pQueryParameters) throws Exception 
    {
        //determine what type of data source this is:
        //i.e. RawLog or XML - probably based on the extension        
        sLog.debug("getting appropriate Resource Tye");
         lDAO = DAOFactory.getDAO(ViewerGlobals.FILE);
         sLog.debug("This is a File.");
         
         //prepare Query to get Log
         sLog.debug("building Query");
         String query = lQueryBuilder.constructQuery(pFileName,pQueryParameters);
       return lDAO.getData(query);        
    }
    
   
}
