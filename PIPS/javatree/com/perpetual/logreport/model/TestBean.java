package com.perpetual.logreport.model;

/**
 * @author Mike Pot http://www.mikepot.com 
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.rmi.*;
import javax.ejb.*;

import com.perpetual.util.ejb.KeyGeneratorUtility;


/**
	@ejb.bean name="Test" view-type="local" type="CMP" primkey-field="id"
	@ejb.finder signature="Collection findAll()"
 */
public abstract class TestBean implements EntityBean
{
	/**
	  @ejb.create-method
	 */
	public Integer ejbCreate(TestData data) throws CreateException
	{
		if (data.getId() == null) data.setId(new Integer(KeyGeneratorUtility.generateKey()));

		setId(data.getId());
		setData(data);

		return null;
	}

	/**
	  @ejb.interface-method
	 */
	public abstract void setData(TestData data);

	/**
	  @ejb.interface-method
	 */
	public abstract TestData getData();

	/**
	  @ejb.pk-field
	  @ejb.persistent-field
	  @ejb.interface-method
	 */
	public abstract Integer getId();
	public abstract void setId(Integer id);

	/**
	  @ejb.persistent-field
	  @ejb.interface-method
	 */
	public abstract String getName();
	public abstract void setName(String name);
}



