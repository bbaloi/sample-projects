package com.extemp.cem.backbone.persistence;

import com.extemp.cem.actions.SentimentDetectionAction;
import com.extemp.cem.events.AuctionBidSubmitEvent;
import com.extemp.cem.events.BrowseActivityEvent;




import com.extemp.cem.events.OrderItem;

import java.sql.*;

import com.extemp.cem.events.MobileEvent;
import com.extemp.cem.events.SportsVenueMobileEvent;
import com.extemp.cem.events.payloads.PurchaseItem;
import com.extemp.cem.events.payloads.PurchaseRequest;
import com.extemp.cem.events.payloads.VoteRequest;
import com.extemp.cem.profiles.OfferRequest;
import com.extemp.cem.profiles.OfferResponse;
import com.extemp.cem.util.CEMUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.camel.javaapi.UntypedProducerActor;

public class JDBCClient extends UntypedActor {

    private String uri="jdbc:cem_db[?options]";
    private String _driver, _url, _user,_pass;   
    private Connection _connection;
    private static long cntr=0;
    
    
    private String _eventsInsert = "INSERT INTO events(event_pk,event_id, user_id, event_name, event_type, event_source,app_source, device_id, phone_number,geo_location_coords,"+
    								"location_alias,location_number,event_message,sentiment,event_status,tstamp) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    
    private String _activityInsert="INSERT INTO activities(activity_pk,event_id,user_id,activity_type,event_type,browsed_url,browsed_item_name,browsed_item_category,browsed_item_id,"
    							+ " browsed_item_brand,browsed_item_price,browsed_item_size, browsed_item_gender,published_content_category,published_content_topic,published_content_url,"
    							+ "published_content_id, published_content_data,tstamp) "
    							+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    private String _txInsert="INSERT INTO transactions(tx_pk, user_id,order_id,item_id,item_name,item_price,item_quantity,order_location,total_order_value,tip,tax,payment_method,payment_number,"
    							+ "loyalty_points_value,tstamp) "
    								+ "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    
    private String _offerInsert = "INSERT INTO offers(offers_pk,user_id,user_name,offer_req_id,offer_type,offer_message,app_source,status,tstamp) VALUES(?,?,?,?,?,?,?,?,?)";
    
   private String _auctionInsert = "INSERT INTO auctions(auction_pk,user_id,event_id,event_name,event_type,auction_object_id,event_source,bid,tstamp) VALUES(?,?,?,?,?,?,?,?,?)";   
   
       
   public String getEndpointUri() {
        return uri;
    }

    public JDBCClient() {
        this.uri="jdbc:cem_db[?options]";
        init();
    }

    public JDBCClient(String uri) {
        this.uri = "jdbc:cem_db[?options]";
        init();
    }

   public boolean isOneway() {
        return true;
    }

       	
    	private void init()
    	{
    		_driver = CEMUtil.getInstance().getCEMProperty("JDBC_DRIVER");
    		_url = CEMUtil.getInstance().getCEMProperty("DB_URL");
    		_user = CEMUtil.getInstance().getCEMProperty("USER");
    		_pass = CEMUtil.getInstance().getCEMProperty("PASS");
    		
    		try
    		{
    			 //STEP 1: Register JDBC driver
    			//Class.forName("com.mysql.jdbc.Driver");
      		 // Class.forName(_driver);
    			  //STEP 3: Open a connection
      	      System.out.println("Connecting to database: "+_url+", with user "+_user);
      	      _connection = DriverManager.getConnection(_url,_user,_pass);    		
    		}
    		catch(Exception excp)
    		{
    			excp.printStackTrace();
    		}
    		
    	}
    	
    	private static java.sql.Timestamp getCurrentTimeStamp() {
    		 
    		java.util.Date today = new java.util.Date();
    		return new java.sql.Timestamp(today.getTime());
     
    	}
    	private float getTotalOrderValue(List pOrders)
    	{
    		float retVal=0;
    	
    		Iterator _it = pOrders.iterator();
    	
    		while(_it.hasNext())
			{
					PurchaseItem _item = (PurchaseItem) _it.next();	 
					
					retVal=retVal+ (Float.parseFloat(Double.toString(_item.getPrice())) * _item.getQuantity());
    		
			}
    		
    		return retVal;
    	}

		@Override
		public void onReceive(Object message) throws Exception {
			// TODO Auto-generated method stub
	    	PreparedStatement preparedStatement=null;
	    	
	    	try
	    	{
	    			
	    		if(message instanceof SportsVenueMobileEvent)
		    	{
		    		SportsVenueMobileEvent _event = (SportsVenueMobileEvent) message;
		    		preparedStatement = handleMobileEvent(_event);
	    		
		    	}
	    		if(message instanceof OfferRequest)
	    		{
	    			OfferRequest _offer = (OfferRequest) message;
	    			preparedStatement = handleOfferRequest(_offer);
	    		}
	    		if(message instanceof OfferResponse)
	    		{
	    			OfferResponse _offer = (OfferResponse) message;
	    			preparedStatement = handleOfferResponse(_offer);
	    		}
	    		if(message instanceof PurchaseRequest)
		    	{
	    			PurchaseRequest _req= (PurchaseRequest) message;
	    			List stmtList = handlePurchaseRequest(_req );	    
	    			
	    			Iterator _it = stmtList.iterator();
	    			while(_it.hasNext())
	    			{
	    				PreparedStatement _stmt = (PreparedStatement) _it.next();
	    				_stmt.executeUpdate();		    	   	
	    		    	_stmt.close();
	    			}
		    	}	    		    		
		    	
		    	if(message instanceof BrowseActivityEvent)
		    	{
		    		BrowseActivityEvent _event = (BrowseActivityEvent) message;
		    		preparedStatement = handleBrowseActivity(_event);
		    	}
		    	if(message instanceof AuctionBidSubmitEvent)
		    	{
		    		AuctionBidSubmitEvent _event = (AuctionBidSubmitEvent) message;
		    		preparedStatement = handleAuctionBid(_event);
		    	}
		    	if(message instanceof VoteRequest)
		    	{
		    		VoteRequest _event = (VoteRequest) message;
		    		preparedStatement = handleVoteRequest(_event);
	    		
		    	}
		    	
		    	
		    	
		    	if(preparedStatement !=null)
		    	{
		    		preparedStatement.executeUpdate();		    	   	
		    		preparedStatement.close();
		    	}
	    	}
	    	catch(SQLException pExcp)
	    	{
	    		pExcp.printStackTrace();
	    	}
	    	
			
			
		}
		private PreparedStatement handleMobileEvent(SportsVenueMobileEvent _event)
		{
			
			PreparedStatement preparedStatement=null;
			String _sentiment=null;
			
			try
			{
								
					if(_event.getGamePurchaseItemList()==null)
					{
						System.out.println("### Updating Event table");
						preparedStatement = _connection.prepareStatement(_eventsInsert);		   			
			   	    	
						String ev_pk = cntr+"-event-"+getCurrentTimeStamp().toString();
						cntr++;
						preparedStatement.setString(1, ev_pk);
			   	    	preparedStatement.setString(2, _event.getEventId());
			   	    	preparedStatement.setString(3, _event.getUserId());
			   	    	preparedStatement.setString(4, _event.getEventName());
			   	    	preparedStatement.setString(5, _event.getEventType());
			   	    	preparedStatement.setString(6, _event.getEventSource());
			   	    	preparedStatement.setString(7, _event.getApplicationSource());
			   	    	preparedStatement.setString(8, _event.getDeviceId());
			   	    	preparedStatement.setString(9, _event.getDevicePhoneNumber());
			   	    	preparedStatement.setString(10, _event.getLocationCoordinate());
			   	    	preparedStatement.setString(11, _event.getLocationName());
			   	    	preparedStatement.setString(12, _event.getSeatNumber());
			   	    	preparedStatement.setString(13, _event.getEventMessage());    
			   	    	
			   	    	if( _event.getEventName().equals("GAME_ON_SOCIAL_FEED_TWEET"))			   	    	
			   	    		_sentiment = SentimentDetectionAction.getInstance().getSentiment(_event.getEventMessage());
			   	    	else
			   	    		_sentiment="NA";
			   	    	
			   	    	preparedStatement.setString(14, _sentiment); //sentiment
			   	    	if(_event.getEventName().equals("GAME_ON_FINISH"))
			   	    	{
			   	    		if(_event.getGameWin())
			   	    			preparedStatement.setString(15, "game_win");
			   	    		else
			   	    			preparedStatement.setString(15, "game_lost");    
			   	    	}
			   	    	else
			   	    		preparedStatement.setString(15, "NA");
			   	    		
			   	    			   	    	
			   	    	preparedStatement.setTimestamp(16, getCurrentTimeStamp());
					}
				
				}
				catch(SQLException excp)
				{
					excp.printStackTrace();
				}
				return preparedStatement;
		}
		private List handlePurchaseRequest(PurchaseRequest _req )
		{
			
			System.out.println("### Updating Tx table");
			PreparedStatement preparedStatement=null;
			
			ArrayList _stmtList = new ArrayList();
			
			try
			{
								
				float totalVal = getTotalOrderValue(_req.getPurchaseItemList().getPurchaseItem());
				
				Iterator _it = _req.getPurchaseItemList().getPurchaseItem().iterator();
				while(_it.hasNext())
				{
					preparedStatement = _connection.prepareStatement(_txInsert);
					
						PurchaseItem _item = (PurchaseItem) _it.next();	  		
						
						String tx_pk = cntr+"-tx-"+getCurrentTimeStamp().toString();
						cntr++;
						preparedStatement.setString(1, tx_pk);
						preparedStatement.setString(2, _req.getUserIdOrder());
						preparedStatement.setString(3, _req.getOrderId());
		    	    	preparedStatement.setString(4, _item.getItemId());
		    	    	preparedStatement.setString(5, _item.getItemName());
		    	    	preparedStatement.setFloat(6, Float.parseFloat(Double.toString(_item.getPrice())));
		    	    	preparedStatement.setInt(7, _item.getQuantity());
		    	    	preparedStatement.setString(8, _req.getSeatNumber());
		    	    	preparedStatement.setFloat(9,totalVal);
		    	    	preparedStatement.setFloat(10, Float.parseFloat(Double.toString(_req.getTip())));
		    	    	preparedStatement.setFloat(11, Float.parseFloat(Double.toString(_req.getTax())));
		    	    	preparedStatement.setString(12, _req.getPaymentMethod());//payment method
		    	    	preparedStatement.setString(13, _req.getPaymentNumber());//payment number
		    	    	preparedStatement.setLong(14, _req.getLoyaltyPointsValue()); //oyalty points
		    	    	preparedStatement.setTimestamp(15, getCurrentTimeStamp());
		    	    	
		    	    	
		    	    	_stmtList.add(preparedStatement);
		    	    	
				}
				
			}
			catch(SQLException excp)
			{
				excp.printStackTrace();
			}
			
			return _stmtList;
		}

		public PreparedStatement handleBrowseActivity(BrowseActivityEvent _event)
		{
			PreparedStatement preparedStatement=null;
			try
			{
				System.out.println("### Updating BrowseActivityTable");
	    		
	    		
	    		preparedStatement = _connection.prepareStatement(_activityInsert);	    		
	    	
	    		String activity_pk = cntr+"-activity-"+getCurrentTimeStamp().toString();
	    		cntr++;
	    		preparedStatement.setString(1, activity_pk);
	    		preparedStatement.setString(2, _event.getEventId());
	    		preparedStatement.setString(3, _event.getUserID());
	    		preparedStatement.setString(4, _event.getActivityType());
	    		preparedStatement.setString(5, _event.getEventType());
	    		preparedStatement.setString(6, _event.getBrowsedURL());
	    		preparedStatement.setString(7, _event.getBrowsedItemName());
	    		preparedStatement.setString(8, _event.getBrowsedItemCategory());
	    		preparedStatement.setString(9, _event.getBrowsedItemID());
	    		preparedStatement.setString(10, _event.getBrowsedItemBrand());
	    		preparedStatement.setFloat(11, Float.parseFloat(Double.toString(_event.getBrowsedItemPrice())));
	    		preparedStatement.setString(12, _event.getBrowsedItemSize());
	    		preparedStatement.setString(13, _event.getBrowsedItemGender());
	    		preparedStatement.setString(14, _event.getPublishedContentCategory());
	    		preparedStatement.setString(15, _event.getPublishedContentTopic());
	    		preparedStatement.setString(16, _event.getPublishedContentURL());
	    		preparedStatement.setString(17, _event.getPublishedContentID());
	    		preparedStatement.setString(18, _event.getPublishedContentData());
	    		preparedStatement.setTimestamp(19, getCurrentTimeStamp());	    
			}
	    	catch(SQLException excp)
	    	{
	    		excp.printStackTrace();
	    	}
			return preparedStatement;
		}
		private PreparedStatement handleOfferRequest(OfferRequest _offer)
		{
			PreparedStatement preparedStatement=null;
			try
			{
				
					System.out.println("### Updating Event table  OfferRequest");
					preparedStatement = _connection.prepareStatement(_offerInsert);
				
					String offer_pk = cntr+"-offer-"+getCurrentTimeStamp().toString();
					cntr++;
					preparedStatement.setString(1, offer_pk); 
					preparedStatement.setString(2, _offer.getUserIdOfferReq()); 
		   	    	preparedStatement.setString(3, _offer.getUserName());
		   	    	preparedStatement.setString(4, _offer.getReqIdOffer());
		   	    	preparedStatement.setString(5, _offer.getOfferType());
		   	    	preparedStatement.setString(6, _offer.getOffer()); 
		   	    	preparedStatement.setString(7, _offer.getApplicationSrcOffer()); 
		   	    	preparedStatement.setString(8, "REQUEST"); 		   	    	
		   	    	preparedStatement.setTimestamp(9, getCurrentTimeStamp());				
				
			}
			catch(SQLException excp)
			{
				excp.printStackTrace();
			}
			return preparedStatement;
		}
		private PreparedStatement handleOfferResponse(OfferResponse _offer)
		{
			PreparedStatement preparedStatement=null;
			try
			{
				System.out.println("### Updating Event table  OfferResponse");
				preparedStatement = _connection.prepareStatement(_offerInsert);
				
				String offer_pk = cntr+"-offer-"+getCurrentTimeStamp().toString();
				cntr++;
				preparedStatement.setString(1, offer_pk); 
				preparedStatement.setString(2, _offer.getUserIdOfferReq()); 
	   	    	preparedStatement.setString(3, _offer.getUserName());
	   	    	preparedStatement.setString(4, _offer.getReqIdOffer());
	   	    	preparedStatement.setString(5, _offer.getOfferType());
	   	    	preparedStatement.setString(6, _offer.getOffer()); 
	   	    	preparedStatement.setString(7, _offer.getApplicationSrcOffer()); 
	   	    	if(_offer.isApprove())
	   	    		preparedStatement.setString(8, "APPROVED"); 
	   	    	else
	   	    		preparedStatement.setString(8, "REJECTED");
	   	    	preparedStatement.setTimestamp(9, getCurrentTimeStamp());	   	    	
	   	    	
	   		}
			catch(SQLException excp)
			{
				excp.printStackTrace();				
			}
			
			return preparedStatement;
			
		}
		private PreparedStatement  handleAuctionBid(AuctionBidSubmitEvent _event)
		{
		
			PreparedStatement preparedStatement=null;
			
			try
			{
				System.out.println("### Updating Event table  Auction Bid");
				preparedStatement = _connection.prepareStatement(_auctionInsert);
				
				String auction_pk = cntr+"-auction-"+getCurrentTimeStamp().toString();
				cntr++;
				preparedStatement.setString(1, auction_pk); 
				preparedStatement.setString(2, _event.getUserId()); 
	   	    	preparedStatement.setString(3, _event.getEventId());
	   	    	preparedStatement.setString(4, _event.getEventName());
	   	    	preparedStatement.setString(5, _event.getEventType());
	   	    	preparedStatement.setString(6, _event.getAuctionObjectId()); 
	   	    	preparedStatement.setString(7, _event.getEventSource()); 
	   	    	preparedStatement.setFloat(8, Float.parseFloat(Double.toString(_event.getCurrentBid()))); 	   	    	
	   	    	preparedStatement.setTimestamp(9, getCurrentTimeStamp());	       	
	   		}
			catch(SQLException excp)
			{
				excp.printStackTrace();				
			}
			
			 			
			return preparedStatement;
		}
		private PreparedStatement handleVoteRequest(VoteRequest _event)
		{
			PreparedStatement preparedStatement=null;
			
			try
			{
				
						System.out.println("### Updating Event table");
						preparedStatement = _connection.prepareStatement(_eventsInsert);	
						
						String ev_pk = cntr+"-event-"+getCurrentTimeStamp().toString();
						cntr++;
						preparedStatement.setString(1, ev_pk);
			   	    	preparedStatement.setString(2, "12v");
			   	    	preparedStatement.setString(3, _event.getUserIdVote());
			   	    	preparedStatement.setString(4, "GAME_ON_VOTE");
			   	    	preparedStatement.setString(5, "IN_ARENA");
			   	    	preparedStatement.setString(6,  _event.getApplicationSrcVote());
			   	    	preparedStatement.setString(7,_event.getGameIdVote());
			   	    	preparedStatement.setString(8, "NA");
			   	    	preparedStatement.setString(9, "NA");
			   	    	preparedStatement.setString(10, "NA");
			   	    	preparedStatement.setString(11,"NA");
			   	    	preparedStatement.setString(12,"NA");
			   	    	preparedStatement.setString(13, "NA");    	    
			   	    	preparedStatement.setString(14, _event.getVote()); //sentiment			   	    	
			   	    	preparedStatement.setString(15, "NA"); 		   	    			   	    	
			   	    	preparedStatement.setTimestamp(16, getCurrentTimeStamp());
					
				}
				catch(SQLException excp)
				{
					excp.printStackTrace();
				}
			
			
			return preparedStatement;
		}
}
