CREATE DATABASE cem_db;

USE cem_db;
DROP TABLE IF EXISTS events;
DROP TABLE IF EXISTS activities;
DROP TABLE IF EXISTS social_media;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS offers;
DROP TABLE IF EXISTS auctions;



CREATE TABLE events (event_pk varchar(512) NOT NULL PRIMARY KEY,event_id VARCHAR(100), user_id VARCHAR(100), event_name VARCHAR(100),event_type VARCHAR(100),
						   event_source VARCHAR(200), app_source VARCHAR(100), device_id VARCHAR(100),phone_number VARCHAR(100),geo_location_coords VARCHAR(500),
							location_alias VARCHAR(200), location_number VARCHAR(200), event_message VARCHAR(5000), sentiment VARCHAR(100), event_status VARCHAR(100), tstamp datetime);


CREATE TABLE activities (activity_pk varchar(512) NOT NULL PRIMARY KEY,event_id VARCHAR(100), user_id VARCHAR(100), activity_type VARCHAR(100),event_type VARCHAR(100),browsed_url VARCHAR(1000),browsed_item_name VARCHAR(200),
								browsed_item_category VARCHAR(200),browsed_item_id VARCHAR(200),browsed_item_brand VARCHAR(200), browsed_item_price float,browsed_item_size VARCHAR(200),
								browsed_item_gender VARCHAR(200), published_content_category VARCHAR(200),published_content_topic VARCHAR(200),published_content_url VARCHAR(1000),
								published_content_id VARCHAR(200),published_content_data VARCHAR(1024),tstamp datetime);  
  

CREATE TABLE social_media(soc_media_pk int(11) NOT NULL PRIMARY KEY auto_increment,id varchar(10024),user_id VARCHAR(10024),social_media_id VARCHAR(200),game_id VARCHAR(100), message VARCHAR(1024), app_source VARCHAR(100),sentiment VARCHAR(100),num_friends int, num_followers int,tstamp datetime);

CREATE TABLE transactions(tx_pk varchar(512) NOT NULL PRIMARY KEY,user_id VARCHAR(100), order_id VARCHAR(200), item_id VARCHAR(200), item_name  VARCHAR(200), item_price float, item_quantity int, order_location VARCHAR(200),total_order_value float,
									tip float, tax float, payment_method VARCHAR(200), payment_number VARCHAR(200), loyalty_points_value long,tstamp datetime); 

CREATE TABLE offers(offers_pk varchar(512) NOT NULL PRIMARY KEY,user_id VARCHAR(100), user_name VARCHAR(512), offer_req_id VARCHAR(200), offer_type VARCHAR(200), offer_message VARCHAR(2024), app_source VARCHAR(512), status VARCHAR(100) ,tstamp datetime); 

CREATE TABLE auctions(auction_pk varchar(512) NOT NULL PRIMARY KEY,user_id VARCHAR(100),event_id VARCHAR(100),event_name VARCHAR(100), event_type VARCHAR(100), auction_object_id VARCHAR(300), event_source VARCHAR(300), bid float, tstamp datetime);


CREATE USER 'cem'@'localhost' IDENTIFIED BY 'cem';
GRANT ALL PRIVILEGES ON *.* TO 'root'@'localhost' IDENTIFIED BY 'root' WITH GRANT OPTION;
GRANT ALL PRIVILEGES ON *.* TO 'cem'@'localhost' IDENTIFIED BY 'cem' WITH GRANT OPTION;


commit;




