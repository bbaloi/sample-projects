/*
 * Created on 31-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.throttle.tests;

import junit.framework.TestCase;

import com.perpetual.application.collector.message.MessageQueue;
import com.perpetual.application.collector.throttle.IQueueEntry;
import com.perpetual.application.collector.throttle.ThrottleQueueControl;

/**
 * @author simon
 *
 */
public class TestMessageProcessorThrottleControl extends TestCase {

	private class QueueEntry implements IQueueEntry
	{
		int status;
		String id;
		long timeStamp;
		
		public QueueEntry(String id, long timeStamp)
		{
			this.status = UNPROCESSED;
			this.id = id;
			this.timeStamp = timeStamp;
		}
		
		public String getId() {
			return id;
		}

		public int getStatus() {
			return status;
		}

		public long getTimeStamp() {
			return timeStamp;
		}

		public void setId(String string) {
			id = string;
		}

		public void setStatus(int i) {
			status = i;
		}

		public void setTimeStamp(long l) {
			timeStamp = l;
		}

	}

	public TestMessageProcessorThrottleControl(String arg0) {
		super(arg0);
	}
	
	public void testWithinEPS () throws Exception
	{
		QueueEntry entry = new QueueEntry("", System.currentTimeMillis());

		MessageQueue queue = populateQueueWithAllProcessed(500,
				entry.getTimeStamp(), 1000);  // 500 entries in last 1s
		ThrottleQueueControl throttle = new ThrottleQueueControl();
		
		queue.addToTail(entry);
			
		assertTrue("processable", throttle.isProcessable(queue, entry, 1000, 0));
	}
	
	public void testNotWithinEPS () throws Exception
	{
		QueueEntry entry = new QueueEntry("", System.currentTimeMillis());
	
		MessageQueue queue = populateQueueWithAllProcessed(2000,
				entry.getTimeStamp(), 1000);  // 2000 entries in last 1s
		ThrottleQueueControl throttle =
				new ThrottleQueueControl();  // EPS = 1000
		
			
		queue.addToTail(entry);
			
		assertFalse("processable", throttle.isProcessable(queue, entry, 1000, 0));
	}
	
	public void testWithinEPSIgnoreDiscarded () throws Exception
	{
		QueueEntry entry = new QueueEntry("", System.currentTimeMillis());
		
		MessageQueue queue = populateQueueWithAlternatingProcessedDiscarded(1998,
				entry.getTimeStamp(), 1000);  // 2000 entries in last 1s
		ThrottleQueueControl throttle =
				new ThrottleQueueControl();  // EPS = 1000 - no grace
	
		queue.addToTail(entry);
			
		assertTrue("processable", throttle.isProcessable(queue, entry, 1000, 0));
		
		MessageQueue queue2 = populateQueueWithAlternatingProcessedDiscarded(2003,
				entry.getTimeStamp(), 1000);  // 2000 entries in last 1s
	
		queue2.addToTail(entry);
	
		// should be just over the limit		
		assertFalse("processable", throttle.isProcessable(queue2, entry, 1000, 0));
	}
	
	public void testWithinTolerance () throws Exception
	{
		QueueEntry entry = new QueueEntry("", System.currentTimeMillis());
		
		MessageQueue queue = populateQueueWithAllProcessed(1000,
				entry.getTimeStamp(), 1000);  // 1000 entries in last 1s
		ThrottleQueueControl throttle =
				new ThrottleQueueControl();  // EPS = 1000
	
		queue.addToTail(entry);
			
		assertTrue("processable", throttle.isProcessable(queue, entry, 1000, .20));
		
		MessageQueue queue2 = populateQueueWithAllProcessed(1199,
				entry.getTimeStamp(), 1000);  // 2000 entries in last 1s
	
		queue2.addToTail(entry);
	
		// should be just over the limit - that's OK - within tolerance		
		assertTrue("processable", throttle.isProcessable(queue2, entry, 1000, .20));
		
		MessageQueue queue3 = populateQueueWithAllProcessed(1201,
					entry.getTimeStamp(), 1000);  // 2000 entries in last 1s
	
		queue3.addToTail(entry);

		// should be just over the limit of EPS and tolerance		
		assertFalse("processable", throttle.isProcessable(queue3, entry, 1000, .20));
	}
	

	private MessageQueue populateQueueWithAllProcessed (int numberOfEntries,
			long startTime, int timeWindowBack)
	{
		MessageQueue queue = new MessageQueue(1000, 1000, 60*60*100);
		
		long currentTime = System.currentTimeMillis();
		
		for (int i = 0; i < numberOfEntries; i++) {
			long timestamp = startTime - timeWindowBack/numberOfEntries;
			
			QueueEntry entry = new QueueEntry("" + (numberOfEntries - i - 1),
				timestamp);
			
			entry.setStatus(QueueEntry.PROCESSED);
			
			queue.addToTail(entry);
		}
		
		return queue;
	}
	
	private MessageQueue populateQueueWithAlternatingProcessedDiscarded (
			int numberOfEntries, long startTime, int timeWindowBack)
	{
		MessageQueue queue = new MessageQueue(1000, 1000, 60*60*100);
		
		long currentTime = System.currentTimeMillis();
		
		for (int i = 0; i < numberOfEntries; i++) {
			long timestamp = startTime - i*timeWindowBack/numberOfEntries;
			
			QueueEntry entry = new QueueEntry("" + (numberOfEntries - i - 1),
				timestamp);
		
			if (i % 2 == 0) {	
				entry.setStatus(QueueEntry.PROCESSED);
			} else {
				entry.setStatus(QueueEntry.DISCARDED);
			}
			
			queue.addToTail(entry);
		}
		
		return queue;
	}
}
