/*
 * Created on 9-Aug-2003
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
 */
package com.perpetual.application.collector.tests;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.StringReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import junit.framework.TestCase;

import com.perpetual.application.collector.Controller;
import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.log.util.LogFile;
import com.perpetual.application.collector.log.util.LogStoreConstants;
import com.perpetual.util.FileSystemUtilities;
import com.perpetual.util.GMTFormatter;

/**
 * @author simon
 *
 */
public class TestForwardingCollector extends TestCase {

	private static String LOG_ROOT1 = System.getProperty("java.io.tmpdir")
			+ File.separator + "logstore1";
	private static String LOG_ROOT2 = System.getProperty("java.io.tmpdir")
			+ File.separator + "logstore2";		
	private static String SEPARATOR = System.getProperty("line.separator");
	
	private static String PROPERTIES = "" 
		+ SEPARATOR + "sherlock.collector.EpsRating=0" 
		+ SEPARATOR + "sherlock.collector.EpsRatingTolerance=.00"
		+ SEPARATOR + "sherlock.collector.listener.NumberOfListenerThreads=50"
		+ SEPARATOR + "sherlock.collector.listener.ThreadPriority=MAX_PRIORITY"
		+ SEPARATOR + "sherlock.collector.message.queue.InitialCapacity=20000"
		+ SEPARATOR + "sherlock.collector.message.queue.CapacityIncrement=1000"
		+ SEPARATOR + "sherlock.collector.message.queue.GarbageCollectionInterval=2000"
		+ SEPARATOR + "sherlock.collector.message.queue.ProcessorIdleTime=500"
		+ SEPARATOR + "sherlock.collector.log.WriterThreadPriority=NORM_PRIORITY"
		+ SEPARATOR;
	
	private static String FORWARDING_FILE = "*.*		@localhost:6000";

	public class CollectorThread extends Thread
	{
		private Controller controller;

		public CollectorThread (Configuration configuration) {
			this.controller = new Controller(configuration);	
		}
		
		public void run() {
			try {
				controller.start();
			} catch (InterruptedException e) {
			} catch (NullPointerException e) {
				System.err.println("error starting collector ");
				 e.printStackTrace();
			} catch (Exception e) {
				System.err.println("error starting collector " + e);
			}
		}
		
		public void shutdown() {
			controller.shutdown();
		}
	
		public Controller getController() {
			return controller;
		}

	}

	private DatagramSocket socket;

	public TestForwardingCollector(String arg0) {
		super(arg0);
	}
	
	public void tearDown() {
		FileSystemUtilities.deleteRecursively(new File(LOG_ROOT1));
		FileSystemUtilities.deleteRecursively(new File(LOG_ROOT2));
	}
	
	public void testForwardingToAnotherCollectorOperation() throws Exception
	{
		// set up receiving collector
		Configuration receiverConfiguration = new Configuration();
		receiverConfiguration.setDefaults();
		
		receiverConfiguration.setLogStoreRootPath(LOG_ROOT2);
		receiverConfiguration.setRolloverInterval(0);  // no rollover
		receiverConfiguration.setListenerPort(6000);
		receiverConfiguration.loadPropertiesFrom(PROPERTIES);
		
		CollectorThread receiverCollectorThread = new CollectorThread(
			receiverConfiguration);
				
		receiverCollectorThread.start();
		
		// set up forwarding collector
		Configuration forwardConfiguration = new Configuration();
		forwardConfiguration.setDefaults();
		forwardConfiguration.setConfigurationFileReader(
				new BufferedReader(new StringReader(FORWARDING_FILE)));
		
		forwardConfiguration.setLogStoreRootPath(LOG_ROOT1);
		forwardConfiguration.setRolloverInterval(0);  // no rollover
		forwardConfiguration.loadPropertiesFrom(PROPERTIES);
		
		CollectorThread forwardingCollectorThread = new CollectorThread(
				forwardConfiguration);
				
		forwardingCollectorThread.start();
		Thread.sleep(2000);
		
		createSocket();
		
		// these values may need to be tweaked for a particular platform
		int numberOfMessages = 2000;
		int burstSize = 100;
		long interval = 100;
		
		sendSyslogMessages(numberOfMessages, burstSize, interval,  
				forwardConfiguration.getListenerPort());
		
		Thread.sleep(2000 + 5*numberOfMessages);
		
		assertEquals("got all messages", numberOfMessages,
				forwardingCollectorThread.getController().getPacketCount());
		
		checkLogStore(forwardingCollectorThread.getController(), numberOfMessages);
		checkRelayLogStore(forwardingCollectorThread.getController(),
			numberOfMessages);
		checkLogStore(receiverCollectorThread.getController(), numberOfMessages);
		
		receiverCollectorThread.shutdown();
		forwardingCollectorThread.shutdown();
	}
	
	private void createSocket() throws Exception {
		this.socket = new DatagramSocket();
	}
	
	private void sendSyslogMessages (int numberOfMessages, int burstSize,
			long interval, int port) throws Exception
	{
		String rawMessage = "<1>Sep 22 14:45:23 hostname "
				+ "processName[95]: this is a test message";
				
		byte[] messageBytes = rawMessage.getBytes();
		int length = messageBytes.length;
		
		InetAddress destinationAddress = InetAddress.getByName("localhost");
		
		int numberOfBursts = numberOfMessages / burstSize;
		int remainder = numberOfMessages % burstSize;
		
		
		for (int i = 0; i < numberOfBursts; i++) {

			for (int j = 0; j < burstSize; j++) {
				DatagramPacket packet = new DatagramPacket(
						messageBytes, length, destinationAddress, port);
				
				this.socket.send(packet);
			}
			
			if (interval > 0) {
				Thread.sleep(interval);
			}
		}
		
		for (int i = 0; i < remainder; i++) {
			DatagramPacket packet = new DatagramPacket(
					messageBytes, length, destinationAddress, port);
				
			this.socket.send(packet);			
		}
	}
	
	public void checkLogStore(Controller controller, int numberOfMessages) 
			throws Exception
	{
		GMTFormatter formatter = new GMTFormatter();
		
		File[] files = controller.getLogStore().getCurrentLogDirectory().listFiles();
		
		assertEquals("we have the right number of files",
			2, files.length);
	
		// check each one has the correct number of messages
	
		BufferedReader reader = null;
	
		try {
			for (int i = 0; i < files.length; i++) {
				reader = new BufferedReader(new FileReader(files[i]));
			
				int nlines = 0;
				long previousTimeStamp = -1;
				String line;
			
				while ((line = reader.readLine()) != null) {
					nlines++;
		
					if (!LogFile.isRawLogFile(files[i].getName())) {					
						int firstSpace = line.indexOf(LogStoreConstants.DELIMITER);
						int nextSpace = line.substring(firstSpace + 1)
								.indexOf(LogStoreConstants.DELIMITER);
							
						long timeStamp = formatter.convertToMsFromEpoch(
								line.substring(firstSpace + 1,
								firstSpace + 1 + nextSpace));
					
						assertTrue("lines are written in timestamp order (line = + "
								+ nlines + ")", timeStamp >= previousTimeStamp);			
						
						previousTimeStamp = timeStamp;
					}		
				}
			
				reader.close();
			
				assertEquals("have all the messages in each file",
							numberOfMessages, nlines);
			}
		} catch (Exception e) {
			throw(e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	public void checkRelayLogStore(Controller controller, int numberOfMessages) 
				throws Exception
	{
		GMTFormatter formatter = new GMTFormatter();
	
		File relayLog = new File(
				controller.getLogStore().getLogRootDirectory().getAbsolutePath()
				+ File.separator + "relay" + File.separator + "current");
				
		File[] files = relayLog.listFiles();
	
		assertEquals("we have the right number of files",
			1, files.length);

		// check each one has the correct number of messages

		BufferedReader reader = null;

		try {
			for (int i = 0; i < files.length; i++) {
				reader = new BufferedReader(new FileReader(files[i]));
		
				int nlines = 0;
				long previousTimeStamp = -1;
				String line;
		
				while ((line = reader.readLine()) != null) {
					nlines++;
	
					if (!LogFile.isRawLogFile(files[i].getName())) {					
						int firstSpace = line.indexOf(LogStoreConstants.DELIMITER);
						int nextSpace = line.substring(firstSpace + 1)
								.indexOf(LogStoreConstants.DELIMITER);
						
						long timeStamp = formatter.convertToMsFromEpoch(
								line.substring(firstSpace + 1,
								firstSpace + 1 + nextSpace));
				
						assertTrue("lines are written in timestamp order (line = + "
								+ nlines + ")", timeStamp >= previousTimeStamp);			
					
						previousTimeStamp = timeStamp;
					}		
				}
		
				reader.close();
		
				assertEquals("have all the messages in each file",
							numberOfMessages, nlines);
			}
		} catch (Exception e) {
			throw(e);
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
}
