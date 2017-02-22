package com.extemp.cem.util;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class MessageContentUtil
{
	private static MessageContentUtil instance;
	private static Properties _sportsMessageProps;
	private static boolean _initialised=false;
	
	public MessageContentUtil()
	{
		String _sportsMsgFile = CEMUtil.getInstance().getCEMProperty("cem.sports.messages");
		_sportsMessageProps = new Properties();
		
		InputStream _input = null;
		
		//System.out.println("+++Propsfilename:"+_sportsMsgFile);
		
		if(_sportsMsgFile !=null)
		{
			
			try {
	 
				_input = new FileInputStream(_sportsMsgFile); 
				_sportsMessageProps.load(_input); 
				_input.close();
				//System.out.println(prop.getProperty("database"));		
	 
			} 
			catch (Exception ex) 
			{
				ex.printStackTrace();
			} 
			
		}
	}
	
		
	public static MessageContentUtil getInstance()
	{
		if(instance==null)
		{
			instance=new MessageContentUtil();
			_initialised=true;
		}
		return instance;
	}
	public static String getMessage(String pDomain,String pKey)
	{
		System.out.println("$$$ Domain="+pDomain+", key="+pKey);
		
		String _retMsg = null;
		if(!_initialised)
			getInstance();
		
		if(pDomain.equals("Sports"))
			_retMsg = _sportsMessageProps.getProperty(pKey);
		
		
		return _retMsg;
	}
	

}
