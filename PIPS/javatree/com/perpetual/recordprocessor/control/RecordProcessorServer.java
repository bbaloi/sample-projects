/*
 * RecordProcessor.java
 *
 * Created on July 15, 2003, 1:01 PM
 */

package com.perpetual.recordprocessor.control;

import java.util.Properties;

import com.perpetual.exception.BasePerpetualException;
import com.perpetual.rp.util.RPConstants;
import com.perpetual.rp.util.navigator.INavigator;
import com.perpetual.rp.util.navigator.Navigator;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.threadpool.IThreadManager;
import com.perpetual.util.threadpool.SimpleThreadPoolManager;

/**
 *
 * @author  brunob
 */
public class RecordProcessorServer implements IRPControll,IResourceAccess
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( RecordProcessorServer.class );    
       
    private static RPConfigManager lConfigManager=null;
    private static INavigator lNavigator=null;
    private static RecordProcessorServer lServer=null;
    private static RecordProcessorManager lProcessorMgr=null;
    private static IThreadManager lThreadPoolMgr=null;
    private Properties sPropeties=null;
    //private static RPConfig = null;
    /** Creates a new instance of RecordProcessor */
    public RecordProcessorServer() 
    {
    }
    
    public static  void main(String [] args)
    {
        sLog.debug("++++++Starting the Record Processor Server++++++++");
        RecordProcessorServer lServer = new RecordProcessorServer();
        initConfigManager();
        initThreadPoolMgr();
        initNavigator();
        try {
			initRecordProcessorMgr();
        } catch (Throwable t) {
        	sLog.fatal("Error starting up record processer " + t);
        	System.exit(1);
        }
    }
    private static void initNavigator()
    {
        sLog.debug("Initialisating Navigator");
        try
        {
            lNavigator= Navigator.getInstance();
        }
        catch(Exception excp)
        {
            sLog.error("Could not initialise the Logstore Navigator !");
            excp.printStackTrace();
        }
    }
    private static void initConfigManager()
    {
       sLog.debug("initConfigManager !");        
        lConfigManager = new RPConfigManager();
    }
    public static void startRecordProcessor() throws BasePerpetualException
    {
        lServer = new RecordProcessorServer();
        initConfigManager();
        initThreadPoolMgr();
        initNavigator();
		try {
		  initRecordProcessorMgr();
		  } catch (Throwable t) {
			  sLog.fatal("Error starting up record processer " + t);
			  System.exit(1);
		  }
        
    }
    private static void initThreadPoolMgr()
    {
        
         sLog.debug("Initialising ThreadPoolManager !");
        try
        {
            lThreadPoolMgr = new SimpleThreadPoolManager(RPConstants.ThreadPoolInitSize, 
                                        RPConstants.ThreadPoolMaxSize);
        }
        catch(BasePerpetualException excp)
        {
           sLog.error("Error initialising the Thread Pool Manager !",excp);
        }
    }
   
    private static void initRecordProcessorMgr() throws Throwable
    {
        sLog.debug("Initiaiting Record Processor Manager");
       lProcessorMgr = new RecordProcessorManager(lThreadPoolMgr,lConfigManager,lNavigator);
    }
    public void reloadLogstore() throws BasePerpetualException
    {
    }
    public void navigateLogstore() throws BasePerpetualException
    {
    }
    
    public static INavigator getStaticNavigator()
    {        
        if(lNavigator==null)
            initNavigator();
            
        return lNavigator;
    }
    public INavigator getNavigator() 
    {
         return lNavigator;
    }
   
    public static RPConfigManager getStaticConfigManager()
    {
         if(lConfigManager==null)
            initConfigManager();
        return lConfigManager;
    }
    
    public RPConfigManager getConfigManager() 
    {
        return lConfigManager;
    }
    
    public RecordProcessorManager getRecordProcessor() 
    {
         return lProcessorMgr;
    }
    
}

