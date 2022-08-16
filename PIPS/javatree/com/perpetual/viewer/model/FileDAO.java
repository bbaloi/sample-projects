/*
 * FileDAO.java
 *
 * Created on May 16, 2003, 3:52 PM
 */

package com.perpetual.viewer.model;

import java.io.File;
import java.util.Collection;
import java.util.Vector;
import com.perpetual.exception.BasePerpetualException;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.control.query.BaseQueryEngine;
import com.perpetual.viewer.control.query.FileQueryResult;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import com.perpetual.viewer.model.vo.SyslogRecordVO;

/**
 *
 * @author  brunob
 */
public class FileDAO extends DaoImpl
{
    
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( FileDAO.class );
   
    private File lLogFile=null;
    private TextConverter lConverter=null;
    private Vector lSyslogRecords=null; 
    private SyslogRecordVO lSyslogRecord=null;
    private BufferedReader fileIn=null;
    /** Creates a new instance of FileDAO */
    public FileDAO() 
    {
        lSyslogRecords = new Vector();
        lConverter = new TextConverter();
        //this can be any specialised interpreter
        lQueryEngine = new BaseQueryEngine();
     }
    public Collection getData(String pQuery) throws BasePerpetualException
    { 
        sLog.debug("interpreting the query !");
        //parse the query and run it.
        FileQueryResult result = (FileQueryResult) lQueryEngine.parseQuery(pQuery);
        
        try
        {
             //1)open File
            lLogFile = new File(result.SyslogFileName);
        }
        catch(Exception ex)
        {
            sLog.error("Couldn't open Log file indicated:");
            ex.printStackTrace();
        }
        sLog.debug("Reading file & building list");     
                      
                
        return  getLogFileContents(lLogFile);
    } 
    
    private Collection getLogFileContents(File pFile) throws BasePerpetualException
    {
        Vector resultSet= new Vector();
        try
        {
            sLog.debug("Opening Log File for reading!");
            fileIn = new BufferedReader(new FileReader(pFile));
        }
        catch(FileNotFoundException ex)
        {
            sLog.error("File not Found error");
            throw new BasePerpetualException("Log File not foud !",ex);
        }
        
        String line=null;
        int recCount=0;
        
        try
        {
            line = fileIn.readLine();
        }
        catch(java.io.IOException ex)
        {
            sLog.error("Couldn't read line !");
            throw new BasePerpetualException("Couldn't read line!",ex);
        }
        while(line!=null)
        {
            recCount++;
           //2) read lines in the lines in the file commesurate to what the query recommends
          //3)For every line construct a SYslogRecordVO
            lSyslogRecord  = lConverter.convertRecord(line);
            
        //4) add VO to  a vector.
            resultSet.add(lSyslogRecord);
            try
            {
                line = fileIn.readLine();
            }
            catch(java.io.IOException ex)
            {
                sLog.error("Couldn't read line !");
                throw new BasePerpetualException("Couldn't read line!",ex);
            }
            
        }
           
        sLog.debug("Read "+recCount+" syslog records; closing files");
        try
        {
            fileIn.close();
        }
         catch(java.io.IOException ex)
            {
                sLog.error("Couldn't close Log File !");
                throw new BasePerpetualException("Couldn't close Log File!",ex);
            }
        return resultSet;
    }
}
