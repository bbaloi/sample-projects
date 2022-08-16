package com.perpetual.application.collector.throttle;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.util.queue.IGarbageCollectorFilter;

public class MessageGarbageCollector implements IGarbageCollectorFilter {

	private static final Logger LOGGER =
			Logger.getLogger(MessageGarbageCollector.class);

	public boolean isCollectable (Object element)
	{
		boolean collectable = false;
	
		long currentTimeStamp = System.currentTimeMillis();
		
		IQueueEntry entry = (IQueueEntry) element;
		
		// this is where we check status of packet, must be != UNPROCESSED
		// and it's timestamp must back further than 1s
		
		collectable = entry.getStatus() != IQueueEntry.UNPROCESSED
			&& currentTimeStamp - entry.getTimeStamp() > 1000;
		
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			if (collectable) {
				LOGGER.debug("Garbage collecting entry containing packet # = "
					+ entry.getId());
			} else {
				LOGGER.debug("NOT Garbage collecting entry containing packet # = "
									+ entry.getId());	
			}
		}
			
		return collectable;	 	
	}
}
