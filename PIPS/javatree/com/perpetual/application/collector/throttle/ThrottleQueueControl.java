/*
 * Created on 23-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.throttle;

import org.apache.log4j.Logger;

import com.perpetual.util.queue.QueueWithGarbageCollection;

/**
 * @author simon
 * 
 * Throttle back the rate at which messages are processed.
 * 
 * There are various reasons to do this:
 * 	- licensing (collector will only function at a
 * 		rate that is compatible with currently installed license)
 * 	- network manageability (want to control the number of outgoing syslog messages)
 */
public class ThrottleQueueControl {

	private static final Logger LOGGER = Logger.getLogger(
			ThrottleQueueControl.class);

	private static int TIME_WINDOW = 1000;
	
	public boolean isProcessable (QueueWithGarbageCollection queue,
			IQueueEntry entry, int epsRating, double tolerance)
	{
		boolean result = false;
		
		if (epsRating == 0) {
			// special case - always pass
			result = true;
		} else {
			// get a count of packets whose status=PROCESSED over preceding 1s interval
			// if over EPS rating, then discard
	
			long nowTimestamp = entry.getTimeStamp();
			long currentTimestamp = nowTimestamp;
			int processedPacketCount = 0;
			
			queue.lock();		// prevent GC from getting to it
			
			int  currentIndex = queue.getIndexOf((Object) entry);
				
			// search backwards starting at previous element
			
			for (int i = currentIndex - 1;
					withinEPS(processedPacketCount, epsRating, tolerance)
					&& nowTimestamp - currentTimestamp <= TIME_WINDOW
					&& i >= 0;
					i--) {
				IQueueEntry currentEntry = (IQueueEntry) queue.getElementAt(i);

				// only consider processed entries
				if (currentEntry.getStatus() == IQueueEntry.PROCESSED) {
					processedPacketCount++;
				}
				
				currentTimestamp = currentEntry.getTimeStamp();
			}
			
			queue.unlock();
			
			result = withinEPS(processedPacketCount, epsRating, tolerance);
		}
		
		return result;
	}
	
	private boolean withinEPS(int packetCount, int epsRating, double tolerance)
	{
		// the -1 is so that we don't include the current packet
		return packetCount <= epsRating * (1 + tolerance) - 1;
	}
}
