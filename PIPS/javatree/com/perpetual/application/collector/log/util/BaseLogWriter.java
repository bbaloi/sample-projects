package com.perpetual.application.collector.log.util;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.exception.PerpetualException;

public abstract class BaseLogWriter extends Thread {

	private static final Logger LOGGER = Logger.getLogger(BaseLogWriter.class);
	
	private int writerThreadPriority;
	protected Vector writeQueue;
	
	private Thread writerThread = null;
	private boolean stopped = false;
	
	private Object suspendedMutex = new Object();
	private boolean suspended = false;
	
	private Object writingMutex = new Object();
	private boolean writing = false;
	private boolean logReset = true;
	
	
	public BaseLogWriter (int writerThreadPriority)
	{
		this.writeQueue = new Vector();
		this.writerThreadPriority = writerThreadPriority;
		
		this.logReset = true;
	}
	
	public void addToWriteQueue (ILoggableMessage message) {
		
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("adding message to writer queue for host " + message.getHost());
		}
				
		if (!this.stopped) {
			synchronized (this.writeQueue) {
				this.writeQueue.add(message);
				this.writeQueue.notifyAll();
			}
		}
	}
	
	public abstract void processWriteQueue () throws PerpetualException;

	private void waitIfSuspended()
	{
		try {
			synchronized (this.suspendedMutex)
			{
				if (this.suspended) {  // i.e. suspend == true
					this.suspendedMutex.wait();
				}
			}	
		} catch (InterruptedException e) {
		}
	}
	
	public void suspendWriting (boolean resetCurrentLogs)
	{
		synchronized (this.suspendedMutex) {
			this.suspended = true;
		}
		waitForWritingToFinish();
		
		this.logReset = resetCurrentLogs;
	}
	
	public void resumeWriting ()
	{
		synchronized (this.suspendedMutex) {
			this.suspended = false;
			this.suspendedMutex.notifyAll();
		}
	}
	
	public void shutdown() 
	{
		this.stopped = true;
		waitForWritingToFinish();
		this.writerThread.interrupt(); // should cause the thread to terminate gracefully
	}
	
	public void setWriting(boolean writing)
	{
		synchronized (this.writingMutex)
		{
			this.writing = writing;
			this.writingMutex.notifyAll();
		} 
	}
	
	private void waitForWritingToFinish ()
	{
		synchronized (this.writingMutex)
		{
			while (this.writing) {
				try {
					this.writingMutex.wait();
				} catch (InterruptedException e) {
				}
			}
		} 		
	}
	
	public void run ()
	{
		this.writerThread = Thread.currentThread();
		
		try {			
			setPriority(this.getWriterThreadPriority());
		} catch (IllegalArgumentException iae) {
			LOGGER.error("error when attempting to set writer thread priority: "
				 + iae);
		}
		
		while (!stopped) {
			waitIfSuspended();
			try {
				synchronized (this.writeQueue) {
					if (this.writeQueue.isEmpty()) {
						this.writeQueue.wait();
					}
				}
			} catch (InterruptedException e) {
			} finally {
				try {
					processWriteQueue();  // only call this from this thread!!
				} catch (Exception e1) {
					LOGGER.error("couldn't log message:" + e1);
				}
			}
		}
	}

		
	public int getWriterThreadPriority() {
		return writerThreadPriority;
	}

	
	public boolean isLogReset() {
		return logReset;
	}

	public void setLogReset(boolean b) {
		logReset = b;
	}

}