package com.perpetual.application.collector.throttle;

/**
 * @author simon
 *
 */
public interface IQueueEntry {

	public static final int UNPROCESSED = 0;
	public static final int DISCARDED = 1;
	public static final int PROCESSED = 2;


	public int getStatus();
	public void setStatus(int status);
	public long getTimeStamp();
	public String getId();
}
