/*
 * SummaryQueryEngine.java
 *
 * Created on August 3, 2003, 5:57 PM
 */

package com.perpetual.viewer.control.query;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Timestamp;
import java.util.Collection;
import com.perpetual.exception.BasePerpetualException;
import com.perpetual.util.Environment;
import com.perpetual.util.PerpetualPropertiesHandler;
import com.perpetual.util.Constants;
import java.util.Properties;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.Constants;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.perpetual.viewer.util.ViewerGlobals;
import java.sql.PreparedStatement;

/**
 *
 * @author  brunob
 */
public class SummaryQueryEngine implements IQueryEngine
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(SummaryQueryEngine.class);
	
    private static Connection lConnection=null;
    private static SummaryQueryEngine lInstance = null;
    private   PreparedStatement lStatement=null;
   // private String URL = "jdbc:mysql://localhost:3306/perpetual";
    private  String URL = null;
    private  String username=null;
    private  String password=null;
    private  int  pageSize=0;
    // The queryString
    private String queryString = null;
    private SummaryCursor lSummaryCursor=null;
      
     //static 
    /** Creates a new instance of SummaryQueryEngine */
    private SummaryQueryEngine() 
    {
         try
        {
            sLog.debug("Getting DB Properties !");
            //get variables from proerty file user,password,URL
            Properties dbProps = PerpetualPropertiesHandler.getPropertyFile( Environment.getDBPath() );
            URL = (String) dbProps.getProperty(Constants.DB_URL);
            username = (String) dbProps.getProperty(Constants.UserName);
            password = (String) dbProps.getProperty(Constants.Password);
            pageSize = new Integer((String)dbProps.getProperty(Constants.PageSize)).intValue();
            initConnection();
        }
        catch(Exception excp)
        {
            String msg="DB Properties readin' failure !";
            sLog.error(msg);
            excp.printStackTrace();
            //throw new BasePerpetualException();
        }
                
    }
    private void initConnection()
    {
        sLog.debug("Initialising JDBC Connection !");
        try {
	    Class.forName("org.gjt.mm.mysql.Driver");
	    // Establish Connection to the database at URL with usename and password
	    lConnection = DriverManager.getConnection(URL, username, password);
	    sLog.debug ("Ok, connection to the DB is working.");
	} 
        catch (Exception e) // (ClassNotFoundException and SQLException)
	{
		sLog.error("Sorry! The Database didn't load!");
                e.printStackTrace();
                
	}
    }
    public static IQueryEngine getInstance()
    {
        if(lInstance==null)
            lInstance = new SummaryQueryEngine();
        return lInstance;
    }
    public static int getPageSize()
    {
        int pageSize=0;
        
        try
        {
            Properties dbProps = PerpetualPropertiesHandler.getPropertyFile( Environment.getDBPath() );
            pageSize = new Integer((String)dbProps.getProperty(Constants.PageSize)).intValue();
        }
          catch(Exception excp)
        {
            String msg="DB Properties readin' failure !";
            sLog.error(msg);
            excp.printStackTrace();
            //throw new BasePerpetualException();
        }
           return pageSize;
    }
    //public synchronized Collection executeQuery(String pQuery,Map pMap) throws BasePerpetualException 
    public synchronized SummaryCursor executeQuery(String pQuery,Map pMap) throws BasePerpetualException 
    {
        ArrayList finalResultSet = new ArrayList();
        int numRecords=0;
        
        sLog.debug("About to execute the query:"+pQuery);
        try{
             lStatement = lConnection.prepareStatement(pQuery);
             lStatement.setTimestamp(1,(Timestamp)pMap.get(Constants.StartDate));
             lStatement.setTimestamp(2,(Timestamp)pMap.get(Constants.EndDate));
             sLog.debug("Created Statement,executing Query");
                ResultSet rs = lStatement.executeQuery();
                sLog.debug("Got Result Set!");
                if(rs!=null)
                {
		    ResultSetMetaData rsMeta = rs.getMetaData();
		    // Get the N of Cols in the ResultSet
		    int noCols = rsMeta.getColumnCount();
                    boolean b = rsMeta.isSearchable(1);
                    sLog.debug("#of columns:"+noCols);
                                       
                    while (rs.next()) 
                    {          
                        finalResultSet.add(addRecord(rs,noCols,rsMeta));                        
		    }
                }
        }
        catch(java.sql.SQLException excp)
        {
            String msg="Database Error could not create statement or execute query !";
            sLog.error(msg);
            excp.printStackTrace();
            throw new BasePerpetualException(msg,excp);
        }
        
         numRecords=finalResultSet.size();
         sLog.debug("Got back "+numRecords+" records");
         lSummaryCursor= new SummaryCursor(numRecords,pageSize,finalResultSet);
         
        //return finalResultSet;
         return lSummaryCursor;
    }
     private SummaryRecord addRecord(ResultSet pSet,int pNumColumns,ResultSetMetaData pRsMeta) throws java.sql.SQLException
     {
         HashMap recordMap = new HashMap();       
         
           for (int c=1; c<=pNumColumns; c++)
           {
                 String columnName = pRsMeta.getColumnName(c);
                  //sLog.debug("column name:"+columnName);
           
                   if(columnName.equals(ViewerGlobals.Id))
                   {
                        recordMap.put(columnName,new Integer(pSet.getInt(c))); 
                        //sLog.debug("ID:"+pSet.getInt(c));
                   }                                                 
                   if(columnName.equals(ViewerGlobals.StartDate))
                                recordMap.put(columnName,pSet.getTimestamp(c));
                  if(columnName.equals(ViewerGlobals.EndDate))
                                recordMap.put(columnName,pSet.getTimestamp(c));
                  if(columnName.equals(ViewerGlobals.HostName))
                  {
                                recordMap.put(columnName,pSet.getString(c));
                               //sLog.debug("HostName:"+pSet.getString(c));
                  }
                  if(columnName.equals(ViewerGlobals.DomainName))
                  {
                                recordMap.put(columnName,pSet.getString(c));
                                //sLog.debug("DomainName:"+pSet.getString(c));
                  }
                   if(columnName.equals(ViewerGlobals.VendorName))
                                recordMap.put(columnName,pSet.getString(c));
                   if(columnName.equals(ViewerGlobals.DeviceType))
                                recordMap.put(columnName,pSet.getString(c));
                  if(columnName.equals(ViewerGlobals.MessagePatternId))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                   if(columnName.equals(ViewerGlobals.fac0))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac1))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac2))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac3))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac4))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac5))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac6))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac7))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac8))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac9))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac10))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac11))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac12))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac13))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac14))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac15))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac16))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac17))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac18))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac19))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac20))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac21))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac22))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.fac23))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.sev0))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.sev1))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.sev2))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.sev3))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.sev4))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.sev5))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.sev6))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                  if(columnName.equals(ViewerGlobals.sev7))
                                recordMap.put(columnName,new Integer(pSet.getInt(c)));
                            
           }
         
         return new SummaryRecord(recordMap);
     }
     public synchronized QueryResult parseQuery(String pQuery) 
    {
        return null;
     }
    
    
}
