package com.tibco.stax.action.jms;

import javax.jms.*;
import com.tibco.tibjms.*;

/**
 * <p>Title: MessageProducer</p>
 * <p>Description: This class is used to publish messages to
 * a JMS queue. It uses JNDI connection factory to connect to EMS.</p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: TIBCO Software Inc.</p>
 * @author Jorge A. Flores
 * @version 1.0
 */

public class MessageProducer {
  private static String SERVER_URL = null;
  private static String USERNAME = null;
  private static String PASSWORD = null;
  private static QueueConnectionFactory queueFactory=null;
  private static QueueConnection queueConnection = null;
  private static QueueSession queueSession = null;
  private static TopicConnectionFactory topicFactory=null;
  private static TopicConnection topicConnection = null;
  private static TopicSession topicSession = null;

  //private static String QUEUE_NAME = null;

  public MessageProducer(String providerURL, String userName,
                         String password) 
  {
    SERVER_URL = providerURL;
    USERNAME = userName;
    PASSWORD = password;
  
    
    //QUEUE_NAME = queueName;

    initJNDI();
  }

  public void initJNDI() {
    try {
    	//System.out.println("MsgProdInit:url"+SERVER_URL+",user:"+USERNAME+",pass:"+PASSWORD);
      tibjmsUtilities.initJNDI(SERVER_URL, USERNAME, PASSWORD);
    }
    catch (javax.naming.NamingException ex) {
      ex.printStackTrace();
    }

  }

  public void createJmsQueueMessage(String message,String pDestination) {
    /*
     * Lookup queue connection factory which must exist in the
     * factories config file.
     */
    try {
    	if(queueFactory==null)
    		//queueFactory = (QueueConnectionFactory) tibjmsUtilities.lookup("QueueConnectionFactory");
    		queueFactory = new com.tibco.tibjms.TibjmsQueueConnectionFactory(SERVER_URL);
    	
      //System.out.println("OK - successfully did lookup QueueConnectionFactory");

      /*
       * Let's create a connection to the server and a session to
       * verify the server is running so we can continue with our lookup
       * operations.     */
      if(queueConnection==null)
      queueConnection = queueFactory.createQueueConnection(USERNAME, PASSWORD);

      //System.out.println("queueConnection" + queueConnection.toString());
     if(queueSession==null)
      queueSession = queueConnection.createQueueSession(false,
          javax.jms.Session.AUTO_ACKNOWLEDGE);
      //System.out.println("queueSession" + queueSession.toString());

      produceJmsQueueMessage(queueConnection, queueSession, message,pDestination);
    }
   /* catch (javax.naming.NamingException ex) {
      ex.printStackTrace();
    }*/
    catch (JMSException ex) {
      ex.printStackTrace();
    }

  }
  public void createJmsTopicMessage(String message,String pDestination) {
	    /*
	     * Lookup queue connection factory which must exist in the
	     * factories config file.
	     */
	    try {
	    	if(topicFactory==null)
	    		//queueFactory = (QueueConnectionFactory) tibjmsUtilities.lookup("QueueConnectionFactory");
	    		topicFactory = new com.tibco.tibjms.TibjmsTopicConnectionFactory(SERVER_URL);
	    	
	      if(topicConnection==null)
	      topicConnection = topicFactory.createTopicConnection(USERNAME, PASSWORD);

	      //System.out.println("queueConnection" + queueConnection.toString());
	     if(topicSession==null)
	      topicSession = topicConnection.createTopicSession(false,
	          javax.jms.Session.AUTO_ACKNOWLEDGE);
	      //System.out.println("queueSession" + queueSession.toString());

	      produceJmsTopicMessage(topicConnection, topicSession, message,pDestination);
	    }
	   /* catch (javax.naming.NamingException ex) {
	      ex.printStackTrace();
	    }*/
	    catch (JMSException ex) {
	      ex.printStackTrace();
	    }

	  }

  
  public void produceJmsQueueMessage(QueueConnection queueConnection,
                                     QueueSession queueSession,
                                     String message,String pDest) {
    /*
     * Lookup queue QUEUE_NAME which must exist in the
     * queues configuration file, if not successfull then it
     * does not exist...
     */
    javax.jms.Queue jmsQueue = null;
    javax.jms.MessageProducer msgProducer = null;
    javax.jms.TextMessage msg = null;
      javax.jms.Destination destination = null;

    try {
      jmsQueue =
          (javax.jms.Queue) tibjmsUtilities.lookup(pDest);
      //System.out.println("OK - successfully did lookup queue " +
        //                 jmsQueue.getQueueName());

      /* create the producer */
      msgProducer = queueSession.createProducer(jmsQueue);

      /* publish message */
      msg = queueSession.createTextMessage();

      /* set message text */
      msg.setText(message);

      /* publish message */
      msgProducer.send(msg);

      //System.out.println("Published message: " + message);

      /* close the connection */
     //queueSession.close();
      //queueConnection.close();
    }
    catch (javax.naming.NamingException ex) {
      ex.printStackTrace();
    }
    catch (JMSException ex) {
      ex.printStackTrace();
    }
  }
  public void produceJmsTopicMessage(TopicConnection topicConnection,
          TopicSession topicSession,
          String message,String pDest) {
	/*
	* Lookup queue TOPIC_NAME which must exist in the
	* queues configuration file, if not successfull then it
	* does not exist...
	*/
	javax.jms.Topic jmsTopic = null;
	javax.jms.MessageProducer msgProducer = null;
	javax.jms.TextMessage msg = null;
	javax.jms.Destination destination = null;
	
	try 
	{
		jmsTopic =
		(javax.jms.Topic) tibjmsUtilities.lookup(pDest);
		//System.out.println("OK - successfully did lookup queue " +
		//                 jmsQueue.getQueueName());
		
		/* create the producer */
		msgProducer = topicSession.createProducer(jmsTopic);
		
		/* publish message */
		msg = topicSession.createTextMessage();
		
		/* set message text */
		msg.setText(message);
		
		/* publish message */
		msgProducer.send(msg);
		
		//System.out.println("Published message: " + message);
		
		/* close the connection */
		//queueSession.close();
		//queueConnection.close();
	}
	catch (javax.naming.NamingException ex) {
	ex.printStackTrace();
	}
	catch (JMSException ex) {
	ex.printStackTrace();
	}
  }

  /*-----------------------------------------------------------------------
   * main
   *----------------------------------------------------------------------*/
  public static void main(String[] args) {
    String url = "tcp://localhost:7222";
    String userName = "tibco";
    String password = "tibco";
    String queueName = "testing.queue";
    MessageProducer msgPruducer = new MessageProducer(url, userName, password);
    msgPruducer.createJmsQueueMessage("StaxMsg","default.queue");

  }

}