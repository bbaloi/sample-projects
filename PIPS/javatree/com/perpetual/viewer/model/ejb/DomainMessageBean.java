package com.perpetual.viewer.model.ejb;

/**
 * 
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.rmi.*;
import javax.ejb.*;

import java.util.Collection;

import com.perpetual.util.ejb.KeyGeneratorUtility;

/*
 commented @ejb.finder signature="Collection findByMessageId(Integer messageId)"
		query="SELECT OBJECT(o) FROM DomainMessage o where o.messageid = ?1"
 **/
/**
	@ejb.bean name="DomainMessage" view-type="remote" type="CMP" primkey-field="id" cmp-version="2.x"
	@jboss.table-name
		table-name = "domain_message"
	@ejb.finder signature="Collection findAll()"
	@ejb.finder signature="Collection findByMessageId(int messageid)"
		query="SELECT OBJECT(o) FROM DomainMessage o where o.messageid = ?1"
	@ejb.finder signature="Collection findByDomainId(int domainid)"
		query="SELECT OBJECT(o) FROM DomainMessage o where o.domainid = ?1"
        @ejb.transaction="Supports"
        @jboss.container-configuration name="EntityBean2.x_IPP_Pool200"   
 *
 */
public abstract class DomainMessageBean implements EntityBean
{
	/** @ejb.create-method */
	public Integer ejbCreate(DomainMessageData data) throws CreateException
	{
		if (data.getId() == null) data.setId(new Integer(KeyGeneratorUtility.generateKey()));

		setId(data.getId());
		setData(data);

		return null;
	}

	/** @ejb.interface-method */
	public abstract void setData(DomainMessageData data);

	/** @ejb.interface-method */
	public abstract DomainMessageData getData();

	/**
	  @ejb.pk-field
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract Integer getId();
	/** @ejb.interface-method */
	public abstract void setId(Integer id);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract Integer getDomainid();
	/** @ejb.interface-method */
	public abstract void setDomainid(Integer domainid);

	/**
	  @ejb.persistence
	  @ejb.interface-method
	 */
	public abstract Integer getMessageid();
	/** @ejb.interface-method */
	public abstract void setMessageid(Integer messageid);
        
        /**
	   @ejb.interface-method
	 */
        public abstract Collection findByMessageid();
	
}



