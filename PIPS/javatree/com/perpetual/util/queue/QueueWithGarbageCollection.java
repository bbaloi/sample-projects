package com.perpetual.util.queue;

import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * @author simon
 */
public class QueueWithGarbageCollection {

	private static final Logger LOGGER = Logger.getLogger(QueueWithGarbageCollection.class);

	private Vector queue;
	private GarbageCollector garbageCollector;
	private Object lockMutex = new Object();
	private boolean locked = false;
	
	public QueueWithGarbageCollection (int initialCapacity, int capacityIncrement,
			long garbageCollectionInterval, IGarbageCollectorFilter filter)
	{	
		
		LOGGER.info("Initializing message queue ...");
		LOGGER.info("Initial Capacity = " + initialCapacity
				+ ", Capacity Increment = " + capacityIncrement 
				+ ", Garbage Collection Interval = " + garbageCollectionInterval);
			
		queue = new Vector(initialCapacity, capacityIncrement);	

		if (filter == null) {
			// no garbage collection
			this.garbageCollector = null;				
		}
		else {
			this.garbageCollector = new GarbageCollector(this,
					garbageCollectionInterval, filter);
		}
	}
	
	public void initialize ()
	{
		if (this.garbageCollector != null) {
			this.garbageCollector.start();
		}
	}

	public void addToTail (Object entry)
	{
		queue.add(entry);		
	}
		
	public Object removeFromHead ()
	{
		Object result = (Object) queue.elementAt(0);
		queue.removeElementAt(0);
		
		return result;
	}

	public Object getHeadElement ()
	{
		return queue.elementAt(0);		
	}
	
	public boolean isEmpty ()
	{
		return queue.isEmpty();
	}
	
	public int size ()
	{
		return queue.size();
	}
	
	public Object getElementAt (int index)
	{
		return queue.elementAt(index);	
	}
	
	public int getIndexOf (Object element)
	{
		return queue.indexOf(element);
	}
	
	public void lock ()
	{
		synchronized (this.lockMutex) {
			while (this.locked) {
				try {
					this.lockMutex.wait();
				} catch (InterruptedException e) {
				}
			}	
			this.locked = true;
		}
	}
	
	public void unlock ()
	{
		synchronized (this.lockMutex) {
			this.locked = false;
			this.lockMutex.notifyAll();
		}
	}
}