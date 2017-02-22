package com.extemp.cem.actions;

import java.util.HashMap;
import java.util.List;

public class FBOfferAction 
{
	private static HashMap _offerMap;
	
	public FBOfferAction()
	{
		_offerMap = new HashMap();
	}
	
	public static void processOffer(String pUserId, String pOrderId, List pOfferList)
	{
		System.out.println("------Processign FBOffer-------");
	}

}
