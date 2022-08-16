/*
 * HostCounter.java
 *
 * Created on July 23, 2003, 10:24 PM
 */

package com.perpetual.recordprocessor.control;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.vo.HostVO;

/**
 *
 * @author  brunob
 */
public final class HostCounter 
{
	private HostVO hostVO;
	public Map messagePatternCounter = null;
	public int defaultPatternCounter = 0; 

    /** Creates a new instance of HostCounter */
    
    public HostCounter(HostVO hostVO, Collection messagePatterns) 
    {
    	this.hostVO = hostVO;
    
    	this.messagePatternCounter = new HashMap();

		if (messagePatterns.size() > 0) {    	
	    	for (Iterator i = messagePatterns.iterator(); i.hasNext(); ) {
	    		MessagePatternData data = (MessagePatternData) i.next();
	    		this.messagePatternCounter.put(data.getId(),
	    				new MessagePatternCounter(data));
	    	}
		} else {
			// domain has no message patterns associated with it
			// add a default catch all
			this.messagePatternCounter.put(new Integer(-1),
				new MessagePatternCounter()); 
		}
	}
    public void createDefaulMessagePatternCounter()
    {
        this.messagePatternCounter.put(new Integer(-1),new MessagePatternCounter());         
    }
    public final class MessagePatternCounter
    {
    	public MessagePatternData messagePatternData = null;
		public FacilityCounter facilityCounter = null;
		public SeverityCounter severityCounter = null;
    	public Pattern pattern = null;
    	public int count = 0;
    	
    	public MessagePatternCounter(MessagePatternData messagePatternData) {
    		this();
    		this.messagePatternData = messagePatternData;
    		// compile the pattern for later re-use
    		try {
    			this.pattern = Pattern.compile(messagePatternData.getPattern());
    		} catch (PatternSyntaxException pse) {
    			this.pattern = null;  // can't compile - no match
    		}
    	}
    	
    	public MessagePatternCounter ()
    	{
			this.facilityCounter = new FacilityCounter();
			this.severityCounter = new SeverityCounter();
    	}
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


	/**
	 * @return
	 */
	public HostVO getHostVO() {
		return hostVO;
	}



}
