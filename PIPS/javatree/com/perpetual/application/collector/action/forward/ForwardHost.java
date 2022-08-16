/*
 * Created on 24-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action.forward;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.exception.PerpetualException;
import com.perpetual.util.resource.ResourceLoader;

/**
 * @author deque
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ForwardHost {

	private static final Logger LOGGER =
			Logger.getLogger(ForwardHost.class);
	
	private String hostName;
	private int port;
	private int epsRating;
	private ForwardQueue queue;
	private ForwardQueueProcessor queueProcessor;
	private Configuration configuration;
	private InetAddress address = null;
	private DatagramSocket socket = null;
	
	public ForwardHost (String host, int port, int epsRating,
			Configuration configuration)
	{
		this.hostName = host;
		this.port = port;
		this.configuration = configuration;
		this.epsRating = epsRating;
		
		// create the queue here
		int initialCapacity = ResourceLoader.getIntProperty(
								this.configuration.getProperties(),
								"sherlock.collector.action.forward.queue.InitialCapacity",
								ForwardConstants.FORWARD_QUEUE_INITIAL_CAPACITY);
								
		int capacityIncrement = ResourceLoader.getIntProperty(
							this.configuration.getProperties(),
							"sherlock.collector.action.forward.queue.CapacityIncrement",
							ForwardConstants.FORWARD_QUEUE_CAPACITY_INCREMENT);
		
		int gcInterval = ResourceLoader.getIntProperty(
								this.configuration.getProperties(),
								"sherlock.collector.action.forward.queue.CapacityIncrement",
								ForwardConstants.FORWARD_QUEUE_GARBAGE_COLLECTION_INTERVAL);
																								
		this.queue = new ForwardQueue(initialCapacity, capacityIncrement, gcInterval);
		this.queueProcessor = new ForwardQueueProcessor(this,
				this.queue, this.epsRating, this.configuration);
	}
	
	// do a DNS lookup and cache the result
	public void initialize() throws UnknownHostException, SocketException,
			PerpetualException
	{
		LOGGER.info("Attempting lookup of " + this.hostName);
		try {

			this.socket = new DatagramSocket();
			
			this.address = InetAddress.getByName(this.hostName);
			
			// check for possible loopback
			checkForLoopback();
			
		} catch (SocketException se) {
			LOGGER.error("Cannot create a UDP forwarding socket: " + se);
			throw(se);
		} catch (UnknownHostException e) {
			LOGGER.error("Cannot resolve host " + this.hostName);
			this.address = null;
			
			throw(e);		
		}
		
		this.queueProcessor.initialize();
		// start up forward queue processor
		this.queueProcessor.start();
	}
	
	// forward to another syslog server (relay or collector)
	public void forward (SyslogMessage message) throws IOException
	{
		// plop onto queue - queue processor will pick it up
		
		this.queue.enqueueSyslogMessage(message);
	}
	
	private void checkForLoopback ()
	{
	}

	public InetAddress getAddress() {
		return address;
	}


	public String getHostName() {
		return hostName;
	}


	public void setHostName(String string) {
		hostName = string;
	}

	
	public int getPort() {
		return port;
	}

	
	public DatagramSocket getSocket() {
		return socket;
	}

	public int getEpsRating() {
		return epsRating;
	}

}
