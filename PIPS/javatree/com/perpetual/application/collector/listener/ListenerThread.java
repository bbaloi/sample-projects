package com.perpetual.application.collector.listener;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.message.MessageQueue;

public class ListenerThread extends Thread {

	private static final Logger LOGGER = Logger.getLogger(ListenerThread.class);

	private int id;
	private MessageQueue queue;
	private boolean running;
	private int threadPriority;
	private Thread currentThread;
	
	private byte[] datagramBuffer = null;
	private int datagramBufferSize = -1;
	private DatagramPacket datagramPacket = null;
	
	private DatagramSocket datagramSocket = null;
	

	public ListenerThread (int id, MessageQueue queue, DatagramSocket socket,
			int datagramBufferSize, int threadPriority)
	{
		this.id = id;
		this.queue = queue;
		this.datagramSocket = socket;
		this.datagramBufferSize = datagramBufferSize;
		this.threadPriority = threadPriority;
		this.running = true;
		
		LOGGER.debug("Listener thread " + this.id + ": creating a datagram buffer.");
		this.datagramBuffer = new byte[this.datagramBufferSize];
		
		LOGGER.debug("Listener thread " + this.id + ": creating a datagram packet.");
		this.datagramPacket = new DatagramPacket(this.datagramBuffer,
				this.datagramBufferSize);
	}
	
	public void run () {

		LOGGER.debug("Listener thread " + this.id
				+ " starting up with priority = " + this.threadPriority);
	
		this.currentThread = Thread.currentThread();
		setPriority(this.threadPriority);
		
		while (this.running) {
			try {			
				
				this.datagramSocket.receive(this.datagramPacket);	
					
				String rawMessage = new String(this.datagramPacket.getData(), 0,
							this.datagramPacket.getLength());

				if (LOGGER.isEnabledFor(Priority.DEBUG)) {
					LOGGER.debug("Listener thread "
						+ this.id
						+ " got packet:"
						+ rawMessage);
				}
				
				String sourceAddress = this.datagramPacket.getAddress().toString();
				
				// for some reason getAddress sticks preceding slash on the address
				if (sourceAddress.charAt(0) == '/') {
					sourceAddress = sourceAddress.substring(1);
				}
				
				this.queue.enqueueRawMessage(rawMessage, sourceAddress);
				
			} catch (IOException e) {
				if ("socket closed".equals(e.getMessage())) {
					// listening is closing down - normal - do nothing
				} else {
					LOGGER.error("got exception while listening: " + e);
				}				
			}
		}		
	}
	
	public void shutdown ()
	{
		this.running = false;
		this.currentThread.interrupt();
	}
}
