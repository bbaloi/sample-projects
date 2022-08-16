/*
 * ScheduleExecMDB.java
 *
 * Created on December 22, 2003, 1:50 PM
 */

package com.perpetual.logreport.scheduler;

import javax.ejb.EJBObject;
import javax.ejb.MessageDrivenBean;
import javax.ejb.MessageDrivenContext;
import javax.ejb.EJBException;

import javax.jms.MessageListener;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import com.perpetual.logreport.model.ScheduleData;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.Constants;
import javax.ejb.CreateException;
import java.rmi.RemoteException;

/**
 *
 * @author  brunob
 */

/*
 *@ejb.bean name="ScheduleMessageBean" destination-type=javax.jms.Queue subscription-durability=Durable
 *@ejb.transaction="NotSupported"
 *@jboss.destination-jndi-name name="queue/ScheduleQueue" 
*/

public class ScheduleMessageBean implements MessageDrivenBean, MessageListener
{
    private PerpetualC2Logger sLog = new PerpetualC2Logger(ScheduleMessageBean.class);
    private MessageDrivenContext ctx=null;
    private static int jobCounter=0;
    /** Creates a new instance of ScheduleExecMDB */
    public ScheduleMessageBean() 
    {
    }
    //public void ejbCreate() throws CreateException, RemoteException
    public void ejbCreate() 
    {
        jobCounter++;
        sLog.debug("MDB #:"+jobCounter+" currently running !");
    }
    //public void ejbRemove() throws javax.ejb.EJBException 
    public void ejbRemove() 
    {
        sLog.debug("Removing ScheduleMDB !");
    }    
    
    public void onMessage(javax.jms.Message message) 
    {
        try
        {
            sLog.debug("In ScheduleExecMDB - got a Schedule Request !");
            ObjectMessage objMsg = (ObjectMessage) message;
            SchedulePayload payload = (SchedulePayload) objMsg.getObject();
            EJBObject sessionObject = (EJBObject)payload.getSessionObject();      
            ScheduleTask task = (ScheduleTask)sessionObject;   
            ScheduleData scheduleData = (ScheduleData) payload.getScheduleData();
            sLog.debug("In MDB #"+jobCounter+" - Executing task:"+scheduleData);
            if(task !=null && scheduleData != null)
            {
                task.execute(scheduleData, scheduleData.getReferenceId());
                sLog.debug("Finished executing task, exiting (MDB #"+jobCounter+"); for task:"+scheduleData);
            }
            else
            {
                sLog.error("Not task required to Execute !....Empty MDB Call !");
            }
        }
        catch(Exception excp)
        {
            String excpStr = "Failed in Executing Scheduled Task !";
            sLog.error(excpStr);
            excp.printStackTrace();
        }
                  
    }
    
    public void setMessageDrivenContext(javax.ejb.MessageDrivenContext messageDrivenContext) throws javax.ejb.EJBException 
    {
        this.ctx=messageDrivenContext;
    }
    
}
