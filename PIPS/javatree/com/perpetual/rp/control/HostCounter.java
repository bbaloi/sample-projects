/*
 * HostCounter.java
 *
 * Created on July 23, 2003, 10:24 PM
 */

package com.perpetual.rp.control;

import com.perpetual.rp.control.vendor.VendorCounter;

/**
 *
 * @author  brunob
 */
public final class HostCounter 
{
    public String hostName=null;
    public String vendorName=null;
    public String deviceType=null;
    public VendorCounter vendorCounter=null;
    public FacilityCounter facilityCounter=null;
    public SeverityCounter severityCounter=null;
    /** Creates a new instance of HostCounter */
    public HostCounter(String pHostName) 
    {
        hostName = pHostName;
        facilityCounter = new FacilityCounter();
        severityCounter = new SeverityCounter();
    }
    public HostCounter(VendorCounter pVendorCounter,String pHostName,
                                String pVendorName,String pDeviceType ) 
    {
        hostName = pHostName;
        facilityCounter = new FacilityCounter();
        severityCounter = new SeverityCounter();
        vendorCounter = pVendorCounter;
        vendorName = pVendorName;
        deviceType=pDeviceType;
        
    }
    public final class SeverityCounter
    {
        public int Emergency=0;  //0
        public int Alert=0;     //1
        public int Critical=0;  //2
        public int Error=0;     //3
        public int Warning=0;   //4
        public int Notice=0;    //5
        public int Info=0;      //6
        public int Debug=0;     //7        
    }
    public final class FacilityCounter
    {
        public int Kernel=0; //0
        public int User=0; //1
        public int Mail=0; //2
        public int Daemon=0; //3
        public int Security=0; //4
        public int Syslog=0; //5
        public int Lpr=0; //6
        public int News=0; //7
        public int UUCP=0; //8
        public int Crond=0; //9
        public int Authority=0; //10
        public int FTP=0; //11
        public int NTP=0; //12
        public int Audit=0; //13
        public int Alert=0; //14
        public int _Crond=0; //15
        public int Local0=0; //16
        public int Local1=0; //17
        public int Local2=0; //18
        public int Local3=0; //19
        public int Local4=0; //20
        public int Local5=0; //21
        public int Local6=0; //22
        public int Local7=0; //23
        
    }
    
}
