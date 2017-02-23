package com.extemp.cem.process;

import java.util.Properties;



import javax.jms.BytesMessage;
import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicConnection;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

import com.extemp.cem.process.utils.Constants;
import com.extemp.cem.process.utils.Utils;


public class JMSController implements MessageListener
{
	private String _jms_url; 
	ActiveMQConnectionFactory _jms_factory=null;
	Queue _queue=null;
	QueueConnection _queue_connection;
	TopicConnection _topic_connection;
	Connection _jms_connection;
	Session _jms_session;
	Destination _in_dest=null;
	Destination _out_dest=null;
	private String _user;
	private String _pass;
	private static final String _client_id="ProcessControllerInstance";
	private Properties _conProps=null;
	private String _in_destination_name;
	private String _in_destination_type;
	private String _out_destination_name;
	private String _out_destination_type;
	private MessageProducer _out_producer;
	private MessageConsumer _in_consumer;
	private Controller _controller;
	
	

	public JMSController()
	{
		//init();
	}

	public JMSController(String pEMSUrl,String pUser,String pPass, String pInDestName,String pInDestType,String pOutDestName,String pOutDestType)
	{
		_jms_url=pEMSUrl;
		_user = pUser;
		_pass = pPass;
		_in_destination_name=pInDestName;
		_in_destination_type=pInDestType;
		_out_destination_name=pOutDestName;
		_out_destination_type=pOutDestType;
		init();
	}
	public void init()
	{
		try
		{
			
			_jms_factory = new ActiveMQConnectionFactory(_user,_pass,_jms_url);			  
			
              // Create a Connection
              _jms_connection = _jms_factory.createConnection();
             
               // Create a Session
              _jms_session  = _jms_connection.createSession(false, Session.AUTO_ACKNOWLEDGE);                 
			
			if(_in_destination_type.equals("queue"))
			{
				_in_dest = _jms_session.createQueue(_in_destination_name);
			}
			else if(_in_destination_type.equals("topic"))
			{
				_in_dest = _jms_session.createTopic(_in_destination_name);
			}
			if(_out_destination_type.equals("queue"))
			{
				_out_dest = _jms_session.createQueue(_out_destination_name);
			}
			else if(_out_destination_type.equals("topic"))
			{
				_out_dest = _jms_session.createTopic(_out_destination_name);
			}
			
			//create consume for he in_destination & producers for the out_destination
			_in_consumer = _jms_session.createConsumer(_in_dest);			
			_out_producer = _jms_session.createProducer(_out_dest);	
			
			_jms_connection.start();
			System.out.println("JMS initialized !");
			
			
			/*
			 MQTT mqtt=new MQTT();
			  BlockingConnection publisherConnection=mqtt.blockingConnection();
			  Topic topic=new Topic(TEST_TOPIC,QoS.AT_MOST_ONCE);
			  MockEndpoint mock=getMockEndpoint("mock:result");
			  mock.expectedMinimumMessageCount(numberOfMessages);
			  publisherConnection.connect();
			  for (int i=0; i < numberOfMessages; i++) {
			    String payload="Message " + i;
			    publisherConnection.publish(topic.name().toString(),payload.getBytes(),QoS.AT_LEAST_ONCE,false);
			  }
			  */			
		
		}
		catch(JMSException pExcp)
		{
			pExcp.printStackTrace();
		}
	}

	public void sendMessage(String pMsg)
	{
		try
		{
			TextMessage _msg = _jms_session.createTextMessage();
			_msg.setText(pMsg);
			_out_producer.send(_msg);
		}
		catch(Exception pExcp)
		{
			pExcp.printStackTrace();
		}
		
	}
	@Override
	public void onMessage(Message arg0) {
		// TODO Auto-generated method stub
		String _command=null;
		
		try
		{
			if(arg0 instanceof TextMessage)
			{
				TextMessage _msg = (TextMessage) arg0;
				 _command =_msg.getText();
			}
			if(arg0 instanceof BytesMessage)
			{
				BytesMessage _msg = (BytesMessage) arg0;
				
				byte[] byteArr = new byte[(int)_msg.getBodyLength()];

				_msg.readBytes( byteArr);				
	          	             
				_command = new String(byteArr); 
				
			}
			
			System.out.println("Received command :"+_command);
			
			if(_command.equals(Constants.start_command))
				_controller.startAllProcesses(Utils.getInstance().get_propsFileConfig());
			if(_command.equals(Constants.stop_command))	
				_controller.stopAllProcesses(Utils.getInstance().get_propsFileConfig());
			if(_command.equals(Constants.restart_command))
				_controller.restartAllProcesses(Utils.getInstance().get_propsFileConfig());		
			
		}
		catch(Exception pExcp)
		{	
			pExcp.printStackTrace();
		}
		
	}
	public void registerListener(MessageListener pListener)
	{
		try
		{	
			if(pListener==null)
				_in_consumer.setMessageListener(this);
			else
				_in_consumer.setMessageListener(pListener);
		}
		catch(JMSException pExcp)
		{
			pExcp.printStackTrace();
		}
	}
	public void registerController(Controller pCtrl)
	{
		_controller=pCtrl;
	}
	public String get_jms_url() {
		return _jms_url;
	}
	public void set_jms_url(String _jms_url) {
		this._jms_url = _jms_url;
	}
	public String get_user() {
		return _user;
	}
	public void set_user(String _user) {
		this._user = _user;
	}
	public String get_pass() {
		return _pass;
	}
	public void set_pass(String _pass) {
		this._pass = _pass;
	}
	public String get_in_destination_name() {
		return _in_destination_name;
	}
	public void set_in_destination_name(String _in_destination_name) {
		this._in_destination_name = _in_destination_name;
	}
	public String get_in_destination_type() {
		return _in_destination_type;
	}
	public void set_in_destination_type(String _in_destination_type) {
		this._in_destination_type = _in_destination_type;
	}
	public String get_out_destination_name() {
		return _out_destination_name;
	}
	public void set_out_destination_name(String _out_destination_name) {
		this._out_destination_name = _out_destination_name;
	}
	public String get_out_destination_type() {
		return _out_destination_type;
	}
	public void set_out_destination_type(String _out_destination_type) {
		this._out_destination_type = _out_destination_type;
	}
	public Controller get_controller() {
		return _controller;
	}

	public void set_controller(Controller _controller) {
		this._controller = _controller;
	}
}
