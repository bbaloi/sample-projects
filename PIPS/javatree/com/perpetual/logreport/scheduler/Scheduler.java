package com.perpetual.logreport.scheduler;

/**
 * @author Mike Pot http://www.mikepot.com 
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.*;
import javax.ejb.*;

import org.apache.log4j.Logger;

import com.perpetual.util.EJBLoader;
//import com.perpetual.log.LogSystem;
import com.perpetual.logserver.LogSystem;
import com.perpetual.logreport.control.ScheduleCRUD;
import com.perpetual.logreport.control.ScheduleCRUDHome;
import com.perpetual.logreport.model.ScheduleData;
import com.perpetual.util.SchedulerMutex;
import java.util.Date; 
import com.perpetual.util.Environment;
import org.jdom.*;
import com.perpetual.util.ResourceLoader;
import com.perpetual.util.Constants;

/**
   Background processing system, executing scheduled task with the option of recurring schedules.
*/
public class Scheduler
{
    public Date startTaskTime=null;
    public Date endTaskTime=null;
    private int MaxConcurrentJobs=1;
    private static int jobsCounter=0;
    int x=0;
    private ScheduleQueueHelper queueHelper=null;
    private static SchedulerMutex mutex=null;
    
             
	public Scheduler() throws Exception
	{
		m_scheduleCRUD = (ScheduleCRUD)EJBLoader.createEJBObject(ScheduleCRUDHome.JNDI_NAME, ScheduleCRUDHome.class);
		m_logSystem = LogSystem.getDefault();
                getMaxConcurrentJobs();
                queueHelper = new ScheduleQueueHelper();
                
        }
        private void getMaxConcurrentJobs()
        {
            try
            {
            String configFile = Environment.getExportsConfigPath();
            Element exports = ResourceLoader.loadResourceAsJdomElement(configFile);
            Element exportFormatAdapters = exports.getChild("Params");
            sLog.debug("got init Params");
            Iterator paramsIt = exportFormatAdapters.getChildren("Param").iterator();
            while(paramsIt.hasNext())
            {
                Element param = (Element)paramsIt.next();
                String name = param.getAttributeValue("name");
                if(name.equals("MaxConcurrentJobs"))
                {                    
                    MaxConcurrentJobs = new Integer(param.getAttributeValue("value")).intValue(); 
                    sLog.debug("Found Max Concurrent Jobs:"+MaxConcurrentJobs);
                    break;
                }
              }
            }
            catch(Exception excp)
            {
                String msg = "Could not load config file for Exports !";
                sLog.error(msg);
                excp.printStackTrace();
            }
        }
        public static void removeJob()
        {
            sLog.debug("finished Export Jobbie !");            
            mutex = SchedulerMutex.getInstance();
            sLog.debug("In remove job-Waiting to get lock on Job Counter!");
            mutex.getLock();
            {
                    sLog.debug("Got lock, removing Export Job from schedule!");
                     if(jobsCounter>0)
                        jobsCounter--;
                    
            }
            mutex.releaseLock();
            sLog.debug("Released Job Counter Lock");

            sLog.debug("Number remaining concurrent jobs:"+jobsCounter);            
            
        }
	public void start() throws Exception
	{
		sLog.info("Scheduler is told to start");
		m_thread = new MyThread();
                //m_thread.setDaemon(true);
		m_thread.start();
		sLog.info("Scheduler has started");
	}

	public void stop() throws Exception
	{
		sLog.info("Scheduler is told to stop");
		if (m_thread != null)
		{
			//m_thread.interrupt();
			//m_thread.join();
                       m_thread.destroy();
		}
                sLog.info("Scheduler has stopped");
                
                
	}

	/**
		Execute the scheduled task only, nothing more.
	*/
	private void performSchedule(ScheduleData scheduleData) throws Exception
	{
                startTaskTime= new Date();
		sLog.info("Performing schedule " + scheduleData);
		sLog.info("whenDate=" + new Date(scheduleData.getWhenDate()));
		sLog.info("startDate=" + new Date(scheduleData.getStartDate()));
		sLog.info("endDate=" + new Date(scheduleData.getEndDate()));
		EJBObject sessionObject = EJBLoader.createEJBObject(scheduleData.getSessionBeanName(), EJBHome.class);
		try
		{     
                            queueHelper.enqueueSchedule(sessionObject,scheduleData);
                            //ScheduleTask task = (ScheduleTask)sessionObject;   
                            //task.execute(scheduleData, scheduleData.getReferenceId());
                            endTaskTime = new Date();                        
                        //mutex.releaseLock();
                        
		} 
                catch(Exception excp)
                {
                    sLog.error("Error in running Scheduler");
                    excp.printStackTrace();
                }
		finally
		{
                    if(taskRanOverTime())
                    {
                        sLog.debug("Task ran over time, running GC !");
                        System.gc();     
                    }
			//sessionObject.remove();
		}
	}        
        private boolean taskRanOverTime()
        {
            long start = startTaskTime.getTime();
            long end = endTaskTime.getTime();
            if ((end-start) > 60000)
                return true;
            else 
                return false;
        }
	/**
		Called when a scheduled task has been executed.  We have to either disable it, or reschedule it if it's a recurring schedule.
	*/
	private void reschedule(ScheduleData scheduleData) throws Exception
	{
		if (scheduleData.getRepeat() == 0)
		{
			// single shot, disable it and be done with it.
			//
			sLog.info("Scheduler is disabling Scheduled Task");
			scheduleData.setEnabled(0);
			m_scheduleCRUD.setSchedule(scheduleData);	// that's it, it's amazing...
		}
		else
		{
			// reschedule, it is one of those rinse and repeat thingies.
			//
			sLog.info("Scheduler is rescheduling Scheduled Task");

			// magic starts here ***************************
			//
			long whenDate = scheduleData.getWhenDate(), startDate = scheduleData.getStartDate(), endDate = scheduleData.getEndDate();
			TimeZone timeZone = TimeZone.getTimeZone(scheduleData.getTimeZone());
			Calendar cal = Calendar.getInstance(timeZone);
			cal.setTimeInMillis(startDate);
			StringTokenizer tokenizer = new StringTokenizer(scheduleData.getIncDate());
			cal.add(Calendar.YEAR, Integer.parseInt(tokenizer.nextToken()));
			cal.add(Calendar.MONTH, Integer.parseInt(tokenizer.nextToken()));
			cal.add(Calendar.DAY_OF_MONTH, Integer.parseInt(tokenizer.nextToken()));
			cal.add(Calendar.HOUR_OF_DAY, Integer.parseInt(tokenizer.nextToken()));
			cal.add(Calendar.MINUTE, Integer.parseInt(tokenizer.nextToken()));
			long newStartDate = cal.getTimeInMillis();
			if (newStartDate != startDate)
			{
				scheduleData.setWhenDate(whenDate + newStartDate - startDate);
				scheduleData.setEndDate(endDate + newStartDate - startDate);
				scheduleData.setStartDate(newStartDate);
			}
			else
			{
				// increment didn't increment anything, let's increment it by the time interval
				//
				long differential = endDate - startDate;
				scheduleData.setWhenDate(whenDate + differential);
				scheduleData.setStartDate(startDate + differential);
				scheduleData.setEndDate(endDate + differential);
			}
			sLog.info("Scheduler is setting when/start/end dates to: " + new Date(scheduleData.getWhenDate()) + " / " + new Date(scheduleData.getStartDate()) + " / " + new Date(scheduleData.getEndDate()));
			m_scheduleCRUD.setSchedule(scheduleData);	// badaboom badabang
			//
			// magic ends here ***************************
		}
	}
        protected void finalize()
        {
            m_thread.destroy();
        }
	private class MyThread extends Thread
	{
             
		public void run()
		{
                    SchedulerMutex mutex=null;
			try
			{
				while (true)
				{
					Thread.sleep(10000);	// wait 20 seconds
                                        long curTime = System.currentTimeMillis();
                                        sLog.debug("Current time:"+new Date(curTime));  
                                        
                                           Collection scheduleDatas = m_scheduleCRUD.retrieveOutstandingSchedules(curTime);
                                            
                                            for (Iterator i = scheduleDatas.iterator(); i.hasNext(); )
                                            {
                                                sLog.debug("Schedule:"+x);x++;
						ScheduleData scheduleData = (ScheduleData)i.next();
                                                
						if (scheduleData.getWhenDate() > System.currentTimeMillis())
                                                {
                                                    sLog.debug("Not time yet to run schedule:"+scheduleData.getSessionBeanName());
									// list is sorted, if this next item is in the future, the other ones are too, so break out
                                                }
                                                try
						{
                                                            
                                                    if(jobsCounter<=MaxConcurrentJobs)
                                                    {
                                                        mutex = SchedulerMutex.getInstance();
                                                        sLog.debug("Waiting to get lock on Job Counter!");
                                                        mutex.getLock();
                                                            {
                                                                sLog.debug("Got lock, adding Export Job !");
                                                                jobsCounter++;
                                                            }
                                                        mutex.releaseLock();
                                                        sLog.debug("Released Job Counter Lock");
                                                            
                                                        sLog.debug("Number concurrent jobs:"+jobsCounter);
                                                           
                                                            //mutex = SchedulerMutex.getInstance();
                                                            //sLog.debug("Waiting to get Scheduler lock !");
                                                            //mutex.getLock();    
                                                            //{
                                                                //sLog.debug("Got scheduler lock !");
                                                                performSchedule(scheduleData);                                                       
                                                                // no error occurred, we can now mark it either as processed (disable it), or reschedule it in case of a repeat
                                                                reschedule(scheduleData);                                                                
                                                            //}
                                                            //mutex.releaseLock();
                                                            //sLog.debug("Released Scheduler Lock");
                                                     }
                                                                                                              
						}
						catch (Throwable ex)
						{
							sLog.error("Problem handling schedule " + scheduleData, ex);
                                                        mutex.releaseLock();
                                                        sLog.debug("Released Scheduler Lock");
						
                                                }
                                                    
                                                
						//break;	// do just one, because the schedule above might have to be generated again, so we can't iterate to the next schedule in the list - the list has changed, reschedule changed it, must break
					}
                                     }
				
			}
			catch (InterruptedException ex)
			{
				// this is ok, we're simply asked to stop doing stuff, and exit the hell outta here
				sLog.info("Scheduler thread interrupted, thread exiting");
			}
			catch (Throwable ex)
			{
				sLog.error("Scheduler had an error and will stop working", ex);
			}
		}
	}

	private Thread m_thread;
	private ScheduleCRUD m_scheduleCRUD;
	private LogSystem m_logSystem;

	private static Logger sLog = Logger.getLogger(Scheduler.class);
}


