/*
 * ScheduleQueueHelper.java
 *
 * Created on December 22, 2003, 8:30 PM
 */

package com.perpetual.logreport.scheduler;

import com.perpetual.util.Constants;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueConnection;
import javax.jms.QueueSession;
import javax.jms.Queue;
import javax.jms.QueueSender;
import javax.jms.Session;
import javax.jms.JMSException;
import javax.jms.ObjectMessage;
import javax.naming.*;
import com.perpetual.logreport.model.ScheduleData;
import com.perpetual.util.PerpetualC2Logger;
import javax.ejb.EJBObject;
/**
 *
 * @author  brunob
 */
public class ScheduleQueueHelper 
{    
    private PerpetualC2Logger sLog = new PerpetualC2Logger(ScheduleQueueHelper.class);
    
    private Context jndiContext=null;
    private QueueConnectionFactory queueConnectionFactory=null;
    private QueueConnection         queueConnection = null;
    private QueueSession            queueSession = null;
    private Queue                   scheduleQueue = null;
    private QueueSender             queueSender = null;
    private ObjectMessage           message=null;
    /** Creates a new instance of ScheduleQueueHelper */
    public ScheduleQueueHelper() 
    {
        init();
    }
    private void init()
    {
           try 
           {
               sLog.debug("Getting jndi InitialContext ");
                jndiContext = new InitialContext();
                sLog.debug("JNDIContext:"+jndiContext);
            } 
           catch (NamingException e) 
           {
                sLog.error("Could not create JNDI API " +"context: " + e.toString()); 
                e.printStackTrace();
            }
        
            try 
            {
                sLog.debug("Getting QueueConnectionFactory");
                queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup(Constants.QUEUECONFAC);
            }
            catch (NamingException e) 
            {
                sLog.error("JNDI API lookup failed: " + e.toString());
                e.printStackTrace();
            }
           try 
           {
               sLog.debug("getting QueueConnection");
               queueConnection = queueConnectionFactory.createQueueConnection();
               sLog.debug("getting QueueSession");
               //queueSession =  queueConnection.createQueueSession(true,Session.AUTO_ACKNOWLEDGE);
               queueSession =  queueConnection.createQueueSession(false,Session.AUTO_ACKNOWLEDGE);
               sLog.debug("getting handle to ScheduleQueue !");
               scheduleQueue = (Queue) jndiContext.lookup(Constants.SCHEDULE_QUEUE);
               sLog.debug("Creating Queue Sender !");
               queueSender = queueSession.createSender(scheduleQueue);
            } 
            catch (Exception e) 
            {
                sLog.error("Queue Connection problem !");
                e.printStackTrace();
                if (queueConnection != null)
                {
                    try 
                    {
                        queueConnection.close();
                    } 
                    catch (JMSException ee) 
                    {
                        sLog.error("Error in initialising the Schedule Queue Connection ! ");
                        ee.printStackTrace();
                    }
                }
           
            } 
    }
    
    public void enqueueSchedule(EJBObject pObject,ScheduleData pScheduleData) throws Exception
    {
        
        sLog.debug("Adding Task to Execution Queue !");
         //1) Construct Message from EJBObject
           //-add EJBObject + ScheduleData
        message = queueSession.createObjectMessage();
        message.setObject(new SchedulePayload(pObject,pScheduleData));
           //2) Publish Message on Queue
       sLog.debug("Sending synchronized message to " +queueSender.getQueue().getQueueName());
       queueSender.send(message); 
       //queueSender.send(scheduleQueue,message); 
       sLog.debug("Enqueued message !");
    }
    protected void finalize()
    {
        try
        {
           queueSession.close();
           queueConnection.close();
           sLog.debug("CLosed connection to JMS !");
        }
        catch(Exception excp)
        {
            sLog.error("Error in colosing JMS connection !");
            excp.printStackTrace();
        }
           
    }
          
        
}
