package com.perpetual.application.collector.action.forward;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;

import com.perpetual.application.collector.configuration.Configuration;
import com.perpetual.application.collector.log.relay.RelayLogStoreManager;
import com.perpetual.application.collector.log.util.LogStoreManagerException;
import com.perpetual.application.collector.message.SyslogMessage;
import com.perpetual.application.collector.throttle.ThrottleQueueControl;
import com.perpetual.exception.PerpetualException;
import com.perpetual.util.resource.ResourceLoader;

public class ForwardQueueProcessor extends Thread {

	private static final Logger LOGGER =
			Logger.getLogger(ForwardQueueProcessor.class);	
	// used by all queue processors
	private static RelayLogStoreManager relayLogStore = null;
	
	private ForwardHost forwardHost;
	private Configuration configuration;
	private ForwardQueue messageQueue;
	private int epsRating;
	private long sleepTime;
	private ThrottleQueueControl filter;

	public ForwardQueueProcessor (ForwardHost forwardHost,
			ForwardQueue queue, int epsRating, Configuration configuration)
	{
		this.configuration = configuration;
		this.messageQueue = queue;
		this.forwardHost = forwardHost;
		this.epsRating = epsRating;
		
		this.sleepTime = ResourceLoader.getLongProperty(
			this.configuration.getProperties(),
			"sherlock.collector.action.forward.queue.ProcessorIdleTime",
			ForwardConstants.FORWARD_QUEUE_PROCESSOR_IDLE_TIME);

		this.filter = new ThrottleQueueControl();
	}
	
	public void initialize() throws PerpetualException
	{
		if (ForwardQueueProcessor.relayLogStore == null
				&& this.configuration.getLogStoreRootPath() != null) {
			ForwardQueueProcessor.relayLogStore = new RelayLogStoreManager(
					this.configuration.getLogStoreRootPath()
							+ File.separator + "relay",
							this.configuration.getRolloverInterval());
			ForwardQueueProcessor.relayLogStore.initialize();
		}
	}
		
	public void run () {
		
		LOGGER.info("Starting forward queue processor ...");
		LOGGER.info("Idle time = " + this.sleepTime);
		
		while (true) {
			if (!this.messageQueue.hasUnprocessedEntries()) {
				try {					
					Thread.sleep(this.sleepTime);
				} catch (InterruptedException e) {
				}
			}
			else {
				QueueEntry entry;
				while ((entry = (QueueEntry) this.messageQueue.getNextUnprocessedItem()) != null) {
			
					if (this.filter.isProcessable(this.messageQueue, entry,
							this.epsRating /* EPS Rating for host */,
							0 /* Tolerance (could be by host) */)) {
						if (LOGGER.isEnabledFor(Priority.DEBUG)) {
							LOGGER.debug("Forwarding packet #"
									+ entry.getSyslogMessage().getPacketId());
							// could write out stats - processed/discarded
						}
		
						forwardMessage(entry);
						entry.setStatus(QueueEntry.PROCESSED);
					} else {
						if (LOGGER.isEnabledFor(Priority.DEBUG)) {
							LOGGER.debug("Discarding packet #"
									+ entry.getSyslogMessage().getPacketId());
						}
						entry.setStatus(QueueEntry.DISCARDED);
					}						
				}
			}
			
		}
	}
	
	private void forwardMessage(QueueEntry entry)
	{
		SyslogMessage message = entry.getSyslogMessage();
		
		if (LOGGER.isEnabledFor(Priority.DEBUG)) {
			LOGGER.debug("forwarding packet id " + message.getPacketId()
				+ " host:port " + this.forwardHost.getHostName()
				+ ":" + this.forwardHost.getPort());
		}

		// convert message to a byte[]
		String forwardedString = message.getStringifiedMessage();
		byte[] messageBytes = forwardedString.getBytes();
		int length = messageBytes.length;
		
		DatagramPacket packet = new DatagramPacket(
				forwardedString.getBytes(),
				length, this.forwardHost.getAddress(),
				this.forwardHost.getPort());
			
		try {
			this.forwardHost.getSocket().send(packet);
				
			// log to the relay log
			if (ForwardQueueProcessor.relayLogStore != null) {
				ForwardQueueProcessor.relayLogStore.logForwardedMessage(
						this.forwardHost, message);
			}
			
		} catch (IOException e) {
			LOGGER.error("Couldn't forward syslog message, packet id = "
				+ message.getPacketId());
		} catch (LogStoreManagerException e) {
			LOGGER.error("Couldn't log the forwarding of syslog message, packet id = "
							+ message.getPacketId());
		}
	}
}	