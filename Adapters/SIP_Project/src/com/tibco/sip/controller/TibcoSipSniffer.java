package com.tibco.sip.controller;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;

import jpcap.JpcapCaptor;
import jpcap.NetworkInterface;
import jpcap.NetworkInterfaceAddress;
import org.apache.log4j.Logger;

import com.tibco.sip.util.Constants;

public class TibcoSipSniffer 
{
	private static final Logger sLogger = Logger.getLogger( "com.tibco.sip.controller.TibcoSipSniffer");
	private static PacketProcessor packetProcessor;
	private static Properties lServerProps=new Properties();
	private static String lPropertiesFileName;
	private static String packetDumpFile;
	private static ISnifferController snifferController;
	private static List interfacesList;
	private static String interfaceListStr;	
	
	public TibcoSipSniffer()
	{		
	}
		
	public static void main(String [] args)
	{
		sLogger.info("Starting Tibco Sip Sniffer ...!");
		TibcoSipSniffer sniffer = new TibcoSipSniffer();		
		sniffer.validateInput(args);
		sniffer.setup();		
		snifferController = new SnifferController(packetDumpFile,lServerProps);
		snifferController.startPacketProcessors(sniffer.getInterfacesList());
		snifferController.startRVTransformer();
		
		while(true);
	}
	
	
	public void validateInput(String args[])
	 {
		 //System.out.println("Num parms:"+args.length);
		if(args.length<2 )
		{
			sLogger.error("Invalid number of arguments !");
			sLogger.error("Proper arguments are: -properties_file <PropertiesFileName>");
			System.exit(1);
		}
		else
		{
			lPropertiesFileName= (String) args[1];
			initProps(lPropertiesFileName);
			
		}			
	 }
	public void initProps(String pPropsFileName)
	{
		try
		{
			lServerProps.load(new FileInputStream(lPropertiesFileName));
			String _lSpringConfig = lServerProps.getProperty("spring.config.location");
		
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		
	}
	public void setup()
	{
		String interfaceList = (String)lServerProps.getProperty(Constants.interfacesList);
		interfacesList = getInterfaces(interfaceList);
		packetDumpFile = (String)lServerProps.getProperty(Constants.dumpFileName);
	}	
	private List getInterfaces(String pIList)
	{
		ArrayList interfaceList=new ArrayList();
		String interfaces=null;
		if(pIList!=null && !interfaceList.equals(""))
		{						
			StringTokenizer tokeniser = new StringTokenizer(pIList,",");
			while(tokeniser.hasMoreTokens())
			{
				String _ip = tokeniser.nextToken();
				interfaces+=_ip+",";
				NetworkInterfaceAddress iAddress = new NetworkInterfaceAddress(_ip.getBytes(),null, null, null);
				NetworkInterfaceAddress [] iAddressList= new NetworkInterfaceAddress [1];
				iAddressList[0]=iAddress;
				NetworkInterface _interface =  new NetworkInterface(_ip,"my interface",false,"datalink_name", "datalink_description", null, iAddressList);
				interfaceList.add(_interface);
			}
			sLogger.debug("explicit interfaces defined:"+interfaces);
		}
		else
		{
			interfaceList=getActiveInterfaces();
			sLogger.debug("No explicit interfaces defined, will listen to all available interfaces:"+interfaceListStr);
		}
		return interfaceList;
	}
	public static ArrayList getActiveInterfaces()
	{
		ArrayList activeInterfaces=new ArrayList();
		String macAddress=null;
	
		NetworkInterface[] devices = JpcapCaptor.getDeviceList();
				
		for (int i = 0; i < devices.length; i++) {
				sLogger.debug(i+" :"+devices[i].name + "(" + devices[i].description+")");
				sLogger.debug("    data link:"+devices[i].datalink_name + "("
						+ devices[i].datalink_description+")");
				sLogger.debug("    MAC address:");
				for (byte b : devices[i].mac_address)
					sLogger.debug(Integer.toHexString(b&0xff) + ":");
				System.out.println();
				for (NetworkInterfaceAddress a : devices[i].addresses)
					sLogger.debug("    address:"+a.address + " " + a.subnet + " "
							+ a.broadcast);
					
				interfaceListStr+=devices[i].name+",";
			}
		for(int i=0;i<devices.length;i++)
		{
			activeInterfaces.add(devices[i]);
		}
		
		return activeInterfaces;
	}

	public static List getInterfacesList() {
		return interfacesList;
	}
}
	
