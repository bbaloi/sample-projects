package com.perpetual.util.threadpool;

/*****************************************************************************
 *	Title:			ManagedThread
<p>
 *	Description:	Thread that runs in ThreadPoolManager 
<p>
 *	@author			b2
<P>
******************************************************************************/
import com.perpetual.util.PerpetualC2Logger;

public class ManagedThread extends Thread
{
	private static  PerpetualC2Logger sLog = new PerpetualC2Logger( ManagedThread.class );

	private Runnable load;
	private boolean run=true;

	protected ManagedThread( String pThreadName,Runnable pRunnable)
	{
		super( pThreadName );
		load = pRunnable;
	}


	public void run()
	{    
		while(run)
		{

			//                while(load==null);//no load available - wait

			try
			{
				Runnable thisLoad;	// MP - adding wait/notify to not have to busy wait
				synchronized (this)
				{
					for (; (thisLoad = load) == null; )
						wait();				// during this wait, the other half can synchronized on 'this'!
				}

				sLog.debug("Running thread -" + getName() + " !");

				load.run();
			}
			catch( Throwable e )
			{
				sLog.error( "Caught exception:  " + e.toString() + " while running:  " + load.toString(), e );
			}
			load = null;
		}
		sLog.debug("Shutting down thread:" + getName());
	}


	public void shutdown()
	{
		run = false;
                sLog.info("Thread "+this.getName()+" exiting...");// MP - Shouldn't we maybe wait for things to end.
		interrupt();		// MP - This will wake things up, and prevent any further waits
	}

	public void addLoad(String loadName,Runnable pRunnable)
	{
		synchronized (this)
		{
			if(load==null)
			{// no load yet, or previous load finished
				sLog.debug("adding a new Load !");
				load = pRunnable;
			}
			else //replace current load with a new one
			{
				sLog.debug("---tricky !!!!---replacing a  Load !");
				IManagedThread mgm = (IManagedThread) load;
				mgm.stop();
				load = pRunnable;                
			}
			if(loadName != null)
				setName(loadName);

			notifyAll();	// this will wake up the 'wait' above
		}
	}
	public void removeLoad()
	{
		sLog.debug("removing the thread's load !");
		synchronized (this)
		{
			IManagedThread mgm = (IManagedThread) load;
			if (mgm != null)
				mgm.stop();                  
		}
	}
        public boolean hasLoad()
        {
            if(load==null)
                return false;
            else
                return true;
        }

}



