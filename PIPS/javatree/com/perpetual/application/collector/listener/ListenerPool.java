/*
 * Created on 21-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.listener;

import java.net.DatagramSocket;

import org.apache.log4j.Logger;

import com.perpetual.application.collector.message.MessageQueue;


public class ListenerPool {

	private static final Logger LOGGER = Logger.getLogger(ListenerPool.class);
	
	private int size; 
	private ListenerThread[] threads = null;
	
	
	public ListenerPool (MessageQueue queue, DatagramSocket socket,
			int size, int bufferSize, int listenerThreadPriority)
	{
		LOGGER.info("Creating listening thread pool of size =  " + size);
		
		this.threads = new ListenerThread[size];
		this.size = size;
		
		for (int i = 0; i < this.size; i++) {
			this.threads[i] = new ListenerThread(i, queue, socket, bufferSize,
					listenerThreadPriority);
		}
	}
	
	public void start () {
		LOGGER.info("Starting listening threads ...");
		
		for (int i = 0; i < this.size; i++) {			
			this.threads[i].start();
		}
	}
	
	public void shutdown() {
		LOGGER.info("Shutting down listener pool ...");

		for (int i = 0; i < size; i++) {
			this.threads[i].shutdown();
		}
	}
}
