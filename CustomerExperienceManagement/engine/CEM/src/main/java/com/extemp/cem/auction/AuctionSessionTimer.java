package com.extemp.cem.auction;

import org.kie.api.runtime.rule.EntryPoint;

import com.extemp.cem.events.EnterpriseActivityEvent;
import com.extemp.cem.util.CEMUtil;
import com.extemp.cem.util.KBUtil;

public class AuctionSessionTimer implements Runnable
{

	private String auctionObjectId;
	private String userId;
	private long waitTime;
	
	public AuctionSessionTimer(String pAuctionObjectId)
	{
		auctionObjectId = pAuctionObjectId;
		
		
		String _time = CEMUtil.getInstance().getCEMProperty("cem.auction.timespan");
		if(_time.contains("s"))
		{
			String tVal = _time.substring(0, _time.indexOf("s"));
			waitTime = 1000 * Long.valueOf(tVal).longValue();
		}
		if(_time.contains("m"))
		{
			String tVal = _time.substring(0, _time.indexOf("m"));
			waitTime = 1000 *60* Long.valueOf(tVal).longValue();
		}
		if(_time.contains("h"))
		{
			String tVal = _time.substring(0, _time.indexOf("h"));
			waitTime = 1000*60*60* Long.valueOf(tVal).longValue();
		}
		
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		System.out.println("#*** Started wait timf of "+waitTime+" ms, for auction on "+auctionObjectId);
		
		try
		{
			Thread.sleep(waitTime);
		}
		catch(Exception excp)
		{
			excp.printStackTrace();
		}
		
		 EnterpriseActivityEvent _entActEvent = new EnterpriseActivityEvent();
		 
		 _entActEvent.setEventId("auction123");
		 _entActEvent.setUserId("ADMIN");
		 _entActEvent.setEventName("AUCTION_STOP");
		 _entActEvent.setEventType("ENTERPRISE_EVENT");
		 _entActEvent.setActivityObjectCategory("AUCTION");
		 _entActEvent.setActivityName("AUCTION");
		 _entActEvent.setActivityObject(auctionObjectId);
		 _entActEvent.setActivityObjectURI("");
		 _entActEvent.setEnterpriseId("");
		 _entActEvent.setEventSource("ENTERPRiSE");			 
		 _entActEvent.setActivityContextCategory("");
		 _entActEvent.setActivityContextContent("");
		    	   
	
	EntryPoint entryPoint = KBUtil.getInstance().getMobileEventEntryPoint("CEMSportsRulesKS", "EntepriseEventChannel");
	
	entryPoint.insert(_entActEvent);		
			
	  // Fire all Rules
    KBUtil.getInstance().getKSession().fireAllRules();


		
		
	}

}
