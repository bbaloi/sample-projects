/*
 * SNMPTrapListener.java
 *
 * Created on March 25, 2003, 10:04 PM
 */

package com.perpetual.management.test;

import java.net.*;
//import com.wysdom.util.Log;

/**
 *
 * @author  brunob
 */
public class SNMPTrapListener 
{
    //private static Log sLog = new Log( SNMPTrapListener.class );
    
    /** Creates a new instance of SNMPTrapListener */
    public SNMPTrapListener() 
    {
    }
    public static void main(String [] args)
    {
        System.out.println("Starting SNMP Trap Listener !");
        //sLog.debug("Starting SNMP Trap Listener !");
        //DatagramSocket serverSocket = new DatagramSocket(new InetSocketAddress("10.1.1.6",)10.1.1.6","162");
        try
        {
            //InetSocketAddress sockAddr = new InetSocketAddress("192.168.1.173",162);
            //DatagramSocket server = new DatagramSocket(sockAddr);
            DatagramSocket server = new DatagramSocket(162);
            System.out.println("Waiting for data");
            byte [] buffer = new byte [4086];
            DatagramPacket packet= new DatagramPacket(buffer,4086);
            int trapnum=0;
            while(true)
            {
                trapnum++;
                server.receive(packet);
                System.out.println("-------got trap------");
                System.out.println("Trap"+trapnum+":"+new String(buffer));
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
