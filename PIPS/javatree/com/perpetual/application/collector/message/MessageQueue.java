/**
 * @author simon
 */

package com.perpetual.application.collector.message;

import org.apache.log4j.Logger;

import com.perpetual.application.collector.throttle.ThrottleQueue;

public class MessageQueue extends ThrottleQueue {

	private static final Logger LOGGER = Logger.getLogger(MessageQueue.class);

	private int packetCount;
	
	public MessageQueue (int initialCapacity, int capacityIncrement,
			long garbageCollectionInterval)
	{
		super(initialCapacity, capacityIncrement, garbageCollectionInterval);
						
		this.packetCount = 0;	
	}
	
	// runs in each listener thread - therefore synchronized
	public synchronized void enqueueRawMessage (String rawMessage, String sourceAddress)
	{
		this.packetCount++;
		long timeStamp = System.currentTimeMillis();
		
		QueueEntry entry = new QueueEntry(rawMessage,
			sourceAddress, timeStamp, this.packetCount);
				
		addToTail(entry);
	}
	
	public int getPacketCount() {
		return packetCount;
	}

}