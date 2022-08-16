/*
 * Sherlock syslog collector.
 * by Perpetual IP Systems GmbH
 * Copyright (c) 2003 Perpetual IP Systems GmbH
 * Web: www.perpetual-ip.com
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 * 
 * This product includes software developed by the
 * Apache Software Foundation (http://www.apache.org/).
 *   
 * NO WARRANTY  
 * -----------  
 * THIS SOFTWARE IS PROVIDED "AS-IS"  WITH ABSOLUTELY NO WARRANTY OF  
 * ANY KIND, EITHER IMPLIED OR EXPRESSED, INCLUDING, BUT NOT LIMITED  
 * TO, THE IMPLIED WARRANTIES OF FITNESS FOR ANY PURPOSE
 * AND MERCHANTABILITY.  
 *
 *    
 * NO LIABILITY  
 * ------------  
 * THE AUTHORS ASSUME ABSOLUTELY NO RESPONSIBILITY FOR ANY  
 * DAMAGES THAT MAY RESULT FROM THE USE,  
 * MODIFICATION OR REDISTRIBUTION OF THIS SOFTWARE, INCLUDING,  
 * BUT NOT LIMITED TO, LOSS OF DATA, CORRUPTION OR DAMAGE TO DATA,  
 * LOSS OF REVENUE, LOSS OF PROFITS, LOSS OF SAVINGS,  
 * BUSINESS INTERRUPTION OR FAILURE TO INTEROPERATE WITH  
 * OTHER SOFTWARE, EVEN IF SUCH DAMAGES ARE FORESEEABLE.  
 *
 */

package com.perpetual.application.collector.listener;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.apache.log4j.Logger;

import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.message.MessageQueue;
import com.perpetual.util.resource.ResourceLoader;

public class Listener {

	private static final Logger LOGGER = Logger.getLogger(Listener.class);

	private int datagramBufferSize;
	private String address;
	private int port;
	private int numberOfListenerThreads;
	private int listenerThreadPriority;
	
	private byte[] datagramBuffer = null;
	private DatagramPacket datagramPacket = null;
	private DatagramSocket datagramSocket = null;
	
	private Configuration configuration = null;
	private MessageQueue messageQueue = null;
	private ListenerPool listenerPool = null;

	public Listener (MessageQueue messageQueue,
			int listenerThreadPriority,
			Configuration configuration)
	{
		this.configuration = configuration;
		this.address = this.configuration.getBindingAddress();
		this.port = this.configuration.getListenerPort();
		this.numberOfListenerThreads =
				ResourceLoader.getIntProperty(this.configuration.getProperties(),
					"sherlock.collector.listener.NumberOfListenerThreads",
					ListenerConstants.NUMBER_OF_LISTENER_THREADS);
						
		this.messageQueue = messageQueue;
		this.listenerThreadPriority = listenerThreadPriority;
	}
	
	public void start () {
		// set up socket, buffer sizes, etc..
		
		LOGGER.info("Using port = " + this.port);
		
		String bufferSizeString;
		
		bufferSizeString = this.configuration.getProperties()
				.getProperty("listener.datagram.buffer.size");
		
		if (bufferSizeString == null) {
			this.datagramBufferSize = ListenerConstants.DATAGRAM_BUFFER_SIZE;
			LOGGER.info("Using datagram buffer size = " + this.datagramBufferSize);
		} else {
			this.datagramBufferSize = (new Integer(datagramBufferSize)).intValue();
			LOGGER.info("Using property-specified datagram buffer size = " 
					+ this.datagramBufferSize);
		}
	
		LOGGER.info("Creating a datagram socket.");
		
		try {
			
			this.datagramSocket = new DatagramSocket(this.port,
					InetAddress.getByName(this.address));
			
		} catch (SocketException e) {
			LOGGER.fatal("Cannot create a listener socket on port "
					 + this.port + ":" + e);
			System.exit(1);
		} catch (UnknownHostException e) {
			LOGGER.fatal("Local binding address is invalid "
					 + this.address + ":" + e);
			System.exit(1);
		}		
		
		this.listenerPool = new ListenerPool(this.messageQueue, this.datagramSocket, 
				this.numberOfListenerThreads, this.datagramBufferSize,
					this.listenerThreadPriority);
				
		this.listenerPool.start();		
	}
	
	public void shutdown () {
		this.listenerPool.shutdown();
		this.datagramSocket.close();
	}
}
