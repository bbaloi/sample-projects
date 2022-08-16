/**
 * @author simon
 */

package com.perpetual.application.collector.throttle;

import org.apache.log4j.Logger;

import com.perpetual.util.queue.QueueWithGarbageCollection;

public class ThrottleQueue extends QueueWithGarbageCollection {

	private static final Logger LOGGER = Logger.getLogger(ThrottleQueue.class);

	private int packetCount;
	
	public ThrottleQueue (int initialCapacity, int capacityIncrement,
			long garbageCollectionInterval)
	{
		super(initialCapacity, capacityIncrement, garbageCollectionInterval,
			new MessageGarbageCollector());	
	}
	
	public int getIndexOf (IQueueEntry entry)
	{
		return getIndexOf((Object) entry);
	}
	
	public boolean hasUnprocessedEntries ()
	{
		lock();
		
		boolean found = false;
		int n = this.size();
		
		// start at head - most likely to find a match
		for (int i = 0; !found && i < n; i++) {
			IQueueEntry entry;
			
			entry = (IQueueEntry) this.getElementAt(i);
			found = (entry.getStatus() == IQueueEntry.UNPROCESSED);					

		}
		
		unlock();
		
		return found;
	}
	
	public IQueueEntry getNextUnprocessedItem ()
	{
		lock();
		
		IQueueEntry result = null;
		int n = this.size();
		
		// start at head - most likely to find a match
		for (int i = 0; result == null && i < n; i++) {
						
			IQueueEntry entry = (IQueueEntry) this.getElementAt(i);

			if (entry.getStatus() == IQueueEntry.UNPROCESSED) {					
				result = entry;
			}
		}
		
		unlock();
		
		return result;
	}
	
	public IQueueEntry getEntyryAt (int index)
	{
		return (IQueueEntry) getElementAt(index);
	}
}