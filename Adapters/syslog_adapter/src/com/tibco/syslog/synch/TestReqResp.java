package com.tibco.syslog.synch;
import java.net.InetAddress;
import java.util.*;
import java.util.logging.Level;

import org.apache.log4j.Logger;

// Copyright 2002 - TIBCO Software Inc.
// ALL RIGHTS RESERVED

import com.tibco.sdk.metadata.*;
import com.tibco.sdk.*;
import com.tibco.sdk.rpc.*;


public class TestReqResp extends MOperationImpl 
{   
	private static final String OPERATION = "testHelloWorld";
	public static final String OPERATION_CLASS  = "/tibco/public/class/ae/SyslogSchema/TestReqReplyInterface";
    public static final String SERVER_NAME     	= "TestReqRespServiceEndPoint";
    private static final String OMNICARE_RETURN_CLASS = "/tibco/public/class/ae/SyslogSchema/Result";
	
    
	private MApp lMapp;
	private MClassRegistry lClassRegistry;	
	
	private static final Logger logger = Logger.getLogger( "com.tibco.syslog.synch.TestReqResp");

	    	    
    public TestReqResp(MApp app) throws Exception
    {
        super(app,OPERATION_CLASS,OPERATION,SERVER_NAME);   
        lMapp = app;
        if(logger.isDebugEnabled())
			logger.debug("---Instantiated ReqResp service----");
    }
    
    public synchronized void onInvoke(MServerRequest request, MServerReply   reply)
	{
    	if(logger.isDebugEnabled())
    		logger.debug("+++invoking TestReqResp+++");    
       
        try
        {
	        String msg  = (String)request.get("pWorld");
	         
	          
	        MInstance resultClass = lClassRegistry.getDataFactory().newInstance(OMNICARE_RETURN_CLASS);
			resultClass.set("resultMessage","Hello you loose - "+msg);
			  
	        
	        reply.setReturnValue(resultClass); 	        
	        
	       
	    	reply.reply();
	    	   
        }
        catch(Exception pExcp)
        {
        	pExcp.printStackTrace();
        }          
    	
	}
      
        
    
	
} // end of class DBRequestImpl
