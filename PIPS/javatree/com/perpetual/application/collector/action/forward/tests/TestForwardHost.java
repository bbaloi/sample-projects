/*
 * Created on 25-Jul-2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package com.perpetual.application.collector.action.forward.tests;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import junit.framework.TestCase;

import com.perpetual.application.collector.action.forward.ForwardHost;
import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.listener.ListenerConstants;
import com.perpetual.application.collector.message.QueueEntry;
import com.perpetual.application.collector.message.SyslogMessage;

/**
 * @author simon
 *
 */
public class TestForwardHost extends TestCase {

	private class UDPPortListener extends Thread
	{
		
		private byte[] datagramBuffer = null;
		private int datagramBufferSize = -1;
		private DatagramPacket datagramPacket = null;
		private Thread currentThread = null;
		private boolean listening = true;
		private String lastMessage;
		private int port;
		private int numberReceived;
	
		private DatagramSocket datagramSocket = null;
		
		public UDPPortListener(int port) throws Exception 
		{
			this.datagramBufferSize = ListenerConstants.DATAGRAM_BUFFER_SIZE;
			this.datagramBuffer = new byte[this.datagramBufferSize];
			
			this.datagramPacket = new DatagramPacket(this.datagramBuffer,
					this.datagramBufferSize);
					
			this.datagramSocket = new DatagramSocket(ListenerConstants.SERVER_PORT);
			
			this.port = port;
			this.listening = true;
			this.numberReceived = 0;
		}
		
		public void shutdown()
		{
			this.datagramSocket.close();
			this.listening = false;
			this.currentThread.interrupt();
			this.numberReceived = 0;	
		}
		
		public void run()
		{
			this.currentThread = Thread.currentThread();
				
			while (this.listening) {
				try {
					this.datagramSocket.receive(this.datagramPacket);	
		
					this.lastMessage = new String(this.datagramPacket.getData(), 0,
												this.datagramPacket.getLength());
					this.numberReceived++;														
												
				} catch (IOException e) {
					shutdown();
				}					
			}
		}

		public String getLastMessage() {
			return lastMessage;
		}

		public int getNumberReceived() {
			return numberReceived;
		}

	}
	private static String TIMESTAMP = "Jun 21 14:45:14";
	private static String DEVICE_HOST_IP = "192.168.2.21";
	private static String COLLECTOR_HOST_IP = "192.168.2.5";
	private static String STRING_HOST = "ahost";
	private static String INVALID_HOST = "ahost:";
	private static String TAG_SPACE = "tag ";
	private static String TAG_PROCESS_PID = "processname[456]:";	
	private static String TAG_COLON = "tag:";
	private static String NON_ALPHA = "@";
	private static String CONTENT = "this is some content";
	private static String NO_SPACE_CONTENT = "this_is_some_content";
	
	private static String PRI_0_0 = "<0>";
	private static String PRI_20_5 = "<165>";
	
	private static String NICE_MESSAGE = PRI_0_0
			+ TIMESTAMP + " " + STRING_HOST + " " + TAG_PROCESS_PID + CONTENT;
	private UDPPortListener listener;

	/**
	 * Constructor for TestForwardAction.
	 * @param arg0
	 */
	public TestForwardHost(String arg0) {
		super(arg0);
	}
	
	public void setUp () throws Exception
	{
		this.listener = new UDPPortListener(ListenerConstants.SERVER_PORT);
		this.listener.start();
		
		Thread.sleep(1000);
	}
	
	public void tearDown () throws Exception
	{
		this.listener.shutdown();
	}
	
	public void testBasicForwardAgainstLocalHost() throws Exception
	{
		ForwardHost host = new ForwardHost("localhost",
				ListenerConstants.SERVER_PORT, 0, new Configuration());
		
		host.initialize();
		
		InetAddress localAddress = InetAddress.getByName("localhost");
		
		assertEquals("address was resolved", localAddress, host.getAddress());

		QueueEntry entry = new QueueEntry(NICE_MESSAGE, "localhost",
						System.currentTimeMillis(), 1);		
		
		SyslogMessage message = new SyslogMessage(entry);
		
		host.forward(message);
		
		// give it a chance to receive
		Thread.sleep(1000);
		
		assertEquals("received message", message.getStringifiedMessage(),
			this.listener.getLastMessage());
	}
	
	public void testForwardWithEPSAgainstLocalHost() throws Exception
	{
		int epsRating = 10;
		
		ForwardHost host = new ForwardHost("localhost",
				ListenerConstants.SERVER_PORT, epsRating, new Configuration());
		
		host.initialize();
		
		InetAddress localAddress = InetAddress.getByName("localhost");
		
		assertEquals("address was resolved", localAddress, host.getAddress());

		QueueEntry entry = new QueueEntry(NICE_MESSAGE, "localhost",
						System.currentTimeMillis(), 1);		
		
		SyslogMessage message = new SyslogMessage(entry);
		
		for (int i = 0; i < 1000; i++) {
			host.forward(message);		
		}
		
		// give it a chance to receive
		Thread.sleep(1500);
		
		assertEquals("received messages", epsRating,
			this.listener.getNumberReceived());
	}
}
