package com.tibco.sip.tests;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Properties;

import javax.sip.RequestEvent;
import javax.sip.ResponseEvent;
import javax.sip.TimeoutEvent;
import javax.sip.address.Address;
import javax.sip.address.AddressFactory;
import javax.sip.address.URI;
import javax.sip.header.HeaderFactory;
import javax.sip.header.ViaHeader;
import javax.sip.message.MessageFactory;
import javax.sip.message.Request;
import javax.sip.message.Response;

import org.cafesip.sipunit.SipCall;
import org.cafesip.sipunit.SipPhone;
import org.cafesip.sipunit.SipStack;
import org.cafesip.sipunit.SipTestCase;
import org.cafesip.sipunit.SipTransaction;

public class TestSimpleSIPSession extends SipTestCase
{

    private SipStack sipStack;
    private SipPhone caller,callee,ua,ub;
    private int myPort=5060;
    private int destPort=5062;
    private int srcPort=5061;
    private String testProtocol="udp";
    private static String host = null;
    private String callerName="lwenger";
    private String calleeName="lwenger";
    private String callerAddress="sip:"+callerName+"@denver.com";
    private String calleeAddress="sip:"+calleeName+"@toronto.com";
    private String contactAddress="sip:mdarbyshire@london.com";
    
    private static final Properties defaultProperties = new Properties();
    static
    {
     
        try
        {
            host = InetAddress.getLocalHost().getHostAddress();
           // host="10.1.1.4";
            System.out.println("Host address:"+host);
        }
        catch (UnknownHostException e)
        {
            host = "localhost";
        }

        defaultProperties.setProperty("javax.sip.IP_ADDRESS", host);
        defaultProperties.setProperty("javax.sip.STACK_NAME", "testAgent");
        defaultProperties.setProperty("gov.nist.javax.sip.TRACE_LEVEL", "32");
        defaultProperties.setProperty("gov.nist.javax.sip.DEBUG_LOG",
                "testAgent_debug.txt");
        defaultProperties.setProperty("gov.nist.javax.sip.SERVER_LOG",
                "testAgent_log.txt");
        defaultProperties
                .setProperty("gov.nist.javax.sip.READ_TIMEOUT", "1000");
        defaultProperties.setProperty(
                "gov.nist.javax.sip.CACHE_SERVER_CONNECTIONS", "false");

        defaultProperties.setProperty("sipunit.trace", "true");
        defaultProperties.setProperty("sipunit.test.port", "5060");
        defaultProperties.setProperty("sipunit.test.protocol", "udp");
    }
    private Properties properties = new Properties(defaultProperties);
    
    public void setUp() throws Exception
    {
        try
        {
            sipStack = new SipStack(testProtocol, myPort, properties);
            SipStack.setTraceEnabled(properties.getProperty("sipunit.trace").equalsIgnoreCase("true")
                    || properties.getProperty("sipunit.trace")
                            .equalsIgnoreCase("on"));
                
            
        }
        catch (Exception ex)
        {
        	ex.printStackTrace();
            fail("Exception: " + ex.getClass().getName() + ": "
                    + ex.getMessage());
            
            throw ex;
        }        
        
    }
    public void tearDown() throws Exception
    {
       //callee.dispose();
       sipStack.dispose();
    }
    
    public void testSendInviteWithRouteHeader() // add a Route Header to the
    // INVITE myself
    {
        try
        {
        	//SipPhone ua = sipStack.createSipPhone("sip:amit@nist.gov");
        	//SipPhone ub = sipStack.createSipPhone("sip:becky@nist.gov");
        	//String destPhone="sip:becky@"+ ua.getStackAddress() + ':'+ myPort + '/' + testProtocol;
        	//System.out.println(destPhone);        	
        	//SipPhone ub = sipStack.createSipPhone(destPhone);
            
        	SipPhone ua = sipStack.createSipPhone("sip:amit@"+ host + ':'+ srcPort);
        	SipPhone ub = sipStack.createSipPhone("sip:becky@"+ host + ':'+ destPort);
        	//SipPhone ub = sipStack.createSipPhone(host,"udp",5061,"sip:becky@nist.gov");
            
        	ub.listenRequestMessage();
            Thread.sleep(100);

            AddressFactory addr_factory = ua.getParent().getAddressFactory();
            HeaderFactory hdr_factory = ua.getParent().getHeaderFactory();

           // Request invite = ua.getParent().getMessageFactory().createRequest("INVITE sip:becky@nist.gov SIP/2.0 ");

            Address to_address = addr_factory.createAddress("sip:bbaloi@"+ ub.getStackAddress() + ':'+ myPort + '/' + testProtocol);
            //Address to_address = addr_factory.createAddress(addr_factory.createURI("sip:becky@nist.gov"));
            Address from_address = addr_factory.createAddress(addr_factory.createURI("sip:amit@nist.gov"));
            
           /* Request invite=ua.getParent().getMessageFactory().createRequest(addr_factory.createURI("sip:becky@"+ ub.getStackAddress() + ':'+ myPort), "INVITE", ua.getParent().getSipProvider().getNewCallId(), 
 					hdr_factory.createCSeqHeader(1, "INVITE"), hdr_factory.createFromHeader(from_address, ua.generateNewTag()), 
 					hdr_factory.createToHeader(to_address, null), 
 					ua.getViaHeaders(), hdr_factory.createMaxForwardsHeader(5));
      */
            Request invite=ua.getParent().getMessageFactory().createRequest(addr_factory.createURI(to_address.toString()), "INVITE", ua.getParent().getSipProvider().getNewCallId(), 
 					hdr_factory.createCSeqHeader(1, "INVITE"), hdr_factory.createFromHeader(ua.getAddress(), ua.generateNewTag()), 
 					hdr_factory.createToHeader(ub.getAddress(), null), 
 					ua.getViaHeaders(), hdr_factory.createMaxForwardsHeader(5));
            
           // invite.addHeader(ua.getParent().getSipProvider().getNewCallId());
           // invite.addHeader(hdr_factory.createCSeqHeader((long)1, Request.INVITE));
            //invite.addHeader(hdr_factory.createFromHeader(ua.getAddress(), ua.generateNewTag()));

           
            invite.addHeader(hdr_factory.createToHeader(to_address, null));

            Address contact_address = addr_factory.createAddress("sip:amit@"+ properties.getProperty("javax.sip.IP_ADDRESS") + ':'+ myPort);
            invite.addHeader(hdr_factory.createContactHeader(contact_address));

            invite.addHeader(hdr_factory.createMaxForwardsHeader(5));
            ArrayList via_headers = ua.getViaHeaders();
            invite.addHeader((ViaHeader) via_headers.get(0));

            // create and add the Route Header
           // Address route_address = addr_factory.createAddress("sip:becky@"+ ub.getStackAddress() + ':'+ myPort + '/' + testProtocol);
            //invite.addHeader(hdr_factory.createRouteHeader(route_address));
            
            
            SipTransaction trans = ua.sendRequestWithTransaction(invite, false,
                    null);
            assertNotNull(ua.format(), trans);
            // call sent
            RequestEvent inc_req = ub.waitRequest(10000);
            assertNotNull(ub.format(), inc_req);
            // call received

            Response response = ub.getParent().getMessageFactory()
                    .createResponse(Response.TRYING, inc_req.getRequest());
            SipTransaction transb = ub.sendReply(inc_req, response);
            assertNotNull(ub.format(), transb);
            // trying response sent

            Thread.sleep(500);

            URI callee_contact = ub.getParent().getAddressFactory().createURI(
                    "sip:becky@"
                            + properties.getProperty("javax.sip.IP_ADDRESS")
                            + ':' + myPort);
            Address contact = ub.getParent().getAddressFactory().createAddress(
                    callee_contact);

            String to_tag = ub.generateNewTag();

            ub.sendReply(transb, Response.RINGING, null, to_tag, contact, -1);
            assertLastOperationSuccess(ub.format(), ub);
            // ringing response sent

            Thread.sleep(500);

            response = ub.getParent().getMessageFactory().createResponse(
                    Response.OK, inc_req.getRequest());
            response.addHeader(ub.getParent().getHeaderFactory()
                    .createContactHeader(contact));

            ub.sendReply(transb, response);
            assertLastOperationSuccess(ub.format(), ub);
            // answer response sent

            Thread.sleep(500);

            EventObject response_event = ua.waitResponse(trans, 10000);
            // wait for trying

            assertNotNull(ua.format(), response_event);
            assertFalse("Operation timed out",
                    response_event instanceof TimeoutEvent);

            assertEquals("Should have received TRYING", Response.TRYING,
                    ((ResponseEvent) response_event).getResponse()
                            .getStatusCode());
            // response(s) received, we're done

            ub.dispose();
        }
        catch (Exception e)
        {
        	e.printStackTrace();
            fail("Exception: " + e.getClass().getName() + ": " + e.getMessage());
        }

    }

/*    
    public void testSendInvite()
    {
    	Request invite=null;
    	try
        {
        	System.out.println("starting testSendInvite");
        	 caller = sipStack.createSipPhone(callerAddress);
             callee = sipStack.createSipPhone(calleeAddress);
             
             //-------------------Create INVITE Request--
             SipCall callA = caller.createSipCall();  
             SipCall callB = callee.createSipCall(); 
             
             callee.listenRequestMessage();
             Thread.sleep(100);
             
             // build the INVITE Request message
             AddressFactory addr_factory = caller.getParent().getAddressFactory();
             HeaderFactory hdr_factory = caller.getParent().getHeaderFactory();             
             MessageFactory msgFactory = caller.getParent().getMessageFactory();
            // Address to_address = addr_factory.createAddress(addr_factory.createURI(calleeAddress));
             Address to_address = addr_factory.createAddress("sip:bbaloi@"+ callee.getStackAddress() + ':'+ myPort + '/' + testProtocol);
             Address from_address = addr_factory.createAddress("sip:lwenger@"+ caller.getStackAddress() + ':'+ myPort + '/' + testProtocol);
             
             invite=msgFactory.createRequest(addr_factory.createURI(calleeAddress), "INVITE", caller.getParent().getSipProvider().getNewCallId(), 
            		 					hdr_factory.createCSeqHeader(1, "INVITE"), hdr_factory.createFromHeader(from_address, caller.generateNewTag()), 
            		 					hdr_factory.createToHeader(to_address, null), 
            		 					caller.getViaHeaders(), hdr_factory.createMaxForwardsHeader(5));
          
             Address contact_address = addr_factory.createAddress("sip:lwenger@"+ host + ":5060");          
             invite.addHeader(hdr_factory.createContactHeader(contact_address));
             
           //  Address route_address = addr_factory.createAddress("sip:bbaloi@"+ callee.getStackAddress() + ':'+ myPort + '/' + testProtocol);
            // invite.addHeader(hdr_factory.createRouteHeader(route_address)); 
             
             // send the Request message
             SipTransaction trans = caller.sendRequestWithTransaction(invite, false,null);
             assertNotNull(caller.format(), trans);
             //--------------Callee wait for Response---------------------------
            
        }
    	catch (Exception e)
        {
        	e.printStackTrace();
           // fail("Exception: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
  
    public void testSendInviteAndGetResponse()
    {
    	Request invite=null;
    	try
        {
        	System.out.println("starting testSendInviteAndGetResponse");
        	// caller = sipStack.createSipPhone(host,"udp",srcPort,"sip:lwenger@"+host);
            // callee = sipStack.createSipPhone(host,"udp",myPort,"sip:bbaloi@"+host);
             caller = sipStack.createSipPhone(callerAddress);
             callee = sipStack.createSipPhone(calleeAddress);
        	 //caller = sipStack.createSipPhone("sip:"+callerName+"@"+host);
             //callee = sipStack.createSipPhone("sip:"+calleeName+"@"+host);
             //-------------------Create INVITE Request--
             //SipCall callA = caller.createSipCall();  
             //SipCall callB = callee.createSipCall(); 
             
             callee.listenRequestMessage();
             Thread.sleep(100);
             
             // build the INVITE Request message
             AddressFactory addr_factory = caller.getParent().getAddressFactory();
             HeaderFactory hdr_factory = caller.getParent().getHeaderFactory();             
             MessageFactory msgFactory = caller.getParent().getMessageFactory();
             Address to_address = addr_factory.createAddress(addr_factory.createURI(calleeAddress));
             Address from_address = addr_factory.createAddress(addr_factory.createURI(callerAddress));
             
             // Address to_address = addr_factory.createAddress("sip:bbaloi@"+ callee.getStackAddress() + ':'+ destPort + '/' + testProtocol);
            // Address from_address = addr_factory.createAddress("sip:lwenger@"+ caller.getStackAddress() + ':'+ srcPort + '/' + testProtocol);
             
             invite=msgFactory.createRequest(addr_factory.createURI(calleeAddress), "INVITE", caller.getParent().getSipProvider().getNewCallId(), 
            		 					hdr_factory.createCSeqHeader(1, "INVITE"), hdr_factory.createFromHeader(from_address, caller.generateNewTag()), 
            		 					hdr_factory.createToHeader(to_address, null), 
            		 					caller.getViaHeaders(), hdr_factory.createMaxForwardsHeader(5));
          
             Address contact_address = addr_factory.createAddress("sip:lwenger@"+ host + ":5060");          
             invite.addHeader(hdr_factory.createContactHeader(contact_address));
             
            //Address route_address = addr_factory.createAddress("sip:bbaloi@"+ callee.getStackAddress() + ':'+ destPort + '/' + testProtocol);
            //invite.addHeader(hdr_factory.createRouteHeader(route_address)); 
             
             // send the Request message
             
            //callA.initiateOutgoingCall(calleeAddress,null);
            SipTransaction trans = caller.sendRequestWithTransaction(invite, false,null);
            //SipTransaction trans = caller.sendRequestWithTransaction(invite, true,null);
            assertNotNull(caller.format(), trans);           
              
             //--------------Callee wait for Response---------------------------             
              RequestEvent inc_req = callee.waitRequest(10000);
              assertNotNull(callee.format(), inc_req);
              
             // call received
             Response response = callee.getParent().getMessageFactory()
                     .createResponse(Response.TRYING, inc_req.getRequest());
             SipTransaction transb = callee.sendReply(inc_req, response);
             assertNotNull(callee.format(), transb);
             // trying response sent
             Thread.sleep(500);
             
             URI callee_contact = callee.getParent().getAddressFactory().createURI("sip:mdarbyshire@"
                             + properties.getProperty("javax.sip.IP_ADDRESS")
                             + ':' + myPort);
             Address contact = callee.getParent().getAddressFactory().createAddress(callee_contact);

             String to_tag = callee.generateNewTag();
             callee.sendReply(transb, Response.RINGING, null, to_tag, contact, -1);
             assertLastOperationSuccess(callee.format(), callee);
             // ringing response sent
             Thread.sleep(500);

             response = callee.getParent().getMessageFactory().createResponse(
                     Response.OK, inc_req.getRequest());
             response.addHeader(callee.getParent().getHeaderFactory()
                     .createContactHeader(contact));

             callee.sendReply(transb, response);
             assertLastOperationSuccess(callee.format(),callee);
             // answer response sent
             Thread.sleep(500);
 
             
             EventObject response_event = caller.waitResponse(trans, 10000);
             	
             // wait for trying

             assertNotNull(caller.format(), response_event);
             assertFalse("Operation timed out",
                     response_event instanceof TimeoutEvent);

             assertEquals("Should have received TRYING", Response.TRYING,
                     ((ResponseEvent) response_event).getResponse()
                             .getStatusCode());
             // response(s) received, we're done                   
              
             
        }
    	catch (Exception e)
        {
        	e.printStackTrace();
           // fail("Exception: " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
   */
}
