/**
 * @author simon
 */

package com.perpetual.application.collector.action.forward;

import org.apache.log4j.Logger;

import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.application.collector.throttle.ThrottleQueue;

public class ForwardQueue extends ThrottleQueue {

	private static final Logger LOGGER = Logger.getLogger(ForwardQueue.class);
	
	public ForwardQueue (int initialCapacity, int capacityIncrement,
			long garbageCollectionInterval)
	{
		super(initialCapacity, capacityIncrement, garbageCollectionInterval);
	}
	
	// runs in each listener thread - therefore synchronized
	public synchronized void enqueueSyslogMessage (SyslogMessage message)
	{
		QueueEntry entry = new QueueEntry(message);
				
		addToTail(entry);
	}
}