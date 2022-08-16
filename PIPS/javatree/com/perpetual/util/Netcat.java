/*
 * Created on 11-Aug-2003
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
package com.perpetual.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

/**
 * @author simon
 *
 */
public class Netcat {
	
	private static class Configuration
	{
		String inputFile;
		long interval = 1;
		int burstSize = 1;
		String host = "localhost";
		int port;			

		public int getBurstSize() {
			return burstSize;
		}

		public String getHost() {
			return host;
		}

		public String getInputFile() {
			return inputFile;
		}

		public long getInterval() {
			return interval;
		}

		public int getPort() {
			return port;
		}

		public void setBurstSize(int i) {
			burstSize = i;
		}

		public void setHost(String string) {
			host = string;
		}

		public void setInputFile(String string) {
			inputFile = string;
		}

		public void setInterval(long l) {
			interval = l;
		}

		public void setPort(int i) {
			port = i;
		}

	}
	
	Configuration configuration = null;
	
	public Netcat (Configuration configuration)
	{
		this.configuration = configuration;
	}
	
	public void start() throws Exception
	{
		File inputFile = new File(this.configuration.getInputFile());
		
		BufferedReader reader = new BufferedReader(new FileReader(inputFile));

		InetAddress destinationAddress = InetAddress
				.getByName(configuration.getHost());
		
		DatagramSocket socket = new DatagramSocket();
				
		String textLine;
		boolean done = false;
		int numberOfMessages = 0;
		
		while (!done) {	
			
			for (int j = 0; j < configuration.getBurstSize(); j++) {
				textLine = reader.readLine();
			
				if (textLine == null) {
					done = true;
				} else {
					numberOfMessages++;
					byte[] messageBytes = textLine.getBytes();
					int length = messageBytes.length;
		
					DatagramPacket packet = new DatagramPacket(
							messageBytes, length, destinationAddress,
							configuration.getPort());
				
					socket.send(packet);
				}
			}
			
			if (configuration.getInterval() > 0) {
				Thread.sleep(configuration.getInterval());
			}
		}
		
		System.out.println("Sent " + numberOfMessages + " messages.");
	}

	private static Configuration parseCommandLine(String [] args) {
		Options options = new Options();
		
		Option inputFileOption = new Option("f", true, "input file");
		Option intervalOption = new Option("i", true,
				"interval between bursts (in ms)");
		Option burstSizeOption = new Option("b", true,
				"burst size");
		Option hostOption = new Option("h", true,
				"destination host");
		Option portOption = new Option("p", true,
				"destination port");

		options.addOption(inputFileOption);
		options.addOption(intervalOption);
		options.addOption(burstSizeOption);
		options.addOption(hostOption);
		options.addOption(portOption);
		
		
		CommandLineParser parser = new PosixParser();
		
		CommandLine commandLine;
		String inputFilePath = null;
		long interval;
		int burstSize;
		String host;
		int port = 0;
		Configuration configuration = new Netcat.Configuration();
		
		try {
			commandLine = parser.parse( options, args );
			
			if (commandLine.hasOption(inputFileOption.getOpt())) {
				inputFilePath = commandLine.getOptionValue(
						inputFileOption.getOpt());
				configuration.setInputFile(inputFilePath);		
			} 

			if (commandLine.hasOption(intervalOption.getOpt())) {
				try {
					interval = Integer.parseInt(commandLine.getOptionValue(
							intervalOption.getOpt()));
				} catch (NumberFormatException e) {
					throw new ParseException("interval must be numeric");
				}
				configuration.setInterval(interval);
			} 

			if (commandLine.hasOption(burstSizeOption.getOpt())) {
				try {
					burstSize = Integer.parseInt(commandLine.getOptionValue(
							burstSizeOption.getOpt()));
				} catch (NumberFormatException e) {
					throw new ParseException("burst size must be numeric");
				}
				configuration.setBurstSize(burstSize);
			} 

			if (commandLine.hasOption(hostOption.getOpt())) {
				host = commandLine.getOptionValue(
						hostOption.getOpt());
				configuration.setHost(host);		
			} 

			if (commandLine.hasOption(portOption.getOpt())) {
				try {
					port = Integer.parseInt(commandLine.getOptionValue(
							portOption.getOpt()));
				} catch (NumberFormatException e) {
					throw new ParseException("port must be numeric");
				}
				configuration.setPort(port);
			}
			
			if (inputFilePath == null) {
				throw new ParseException("missing input file");
			}
						
		}
		catch( ParseException pe ) {
			HelpFormatter formatter = new HelpFormatter();

			formatter.printUsage(new PrintWriter(System.out),
					80, "netcat", options);

			System.exit(1);
		}

		return configuration;
	}


	public static void main (String[] args)
	{
		Configuration configuration = parseCommandLine(args);
		
		Netcat netcat = new Netcat(configuration);
		
		try {
			netcat.start();
		} catch (Exception e) {
			System.err.println("encountered an exception: " + e);
			e.printStackTrace();
		}
	}

}
