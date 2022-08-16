package com.perpetual.util.queue;

import org.apache.log4j.Logger;


// "Private" class - clients should use the interface

class GarbageCollector extends Thread {

	private static final Logger LOGGER =
				Logger.getLogger(GarbageCollector.class);

	private QueueWithGarbageCollection queue;
	private long interval;
	private boolean collecting = true;
	private IGarbageCollectorFilter filter;
	private Thread currentThread;

	public GarbageCollector (QueueWithGarbageCollection queue, long interval,
			IGarbageCollectorFilter filter)
	{
		this.queue = queue;
		this.interval = interval;
		this.filter = filter;
		this.collecting = true;
	}
	
	public void run () {

		LOGGER.info("Starting queue garbage collector ...");
		
		this.currentThread = Thread.currentThread();
		
		while (this.collecting) {
		
			try {
				sleep(interval);
				
				this.queue.lock();
				
				int numberCollected = 0;
				
				// do some collection from the head of the queue
				// will stop at first non-collectable item

				while (!this.queue.isEmpty() && 
						this.filter.isCollectable(this.queue.getHeadElement())) {
					numberCollected++;		 
					this.queue.removeFromHead();
				}					
				
				if (numberCollected > 0) {
					LOGGER.info("Garbage collected " + numberCollected + " entries.");
				}		
			} catch (InterruptedException e) {
			} finally {
				this.queue.unlock();
			}
		}
	}
	
	public void shutdown () {
		this.collecting = false;
		this.currentThread.interrupt();
	}
}