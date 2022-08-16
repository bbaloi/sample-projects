package com.perpetual.viewer.control.ejb.crud.messagepattern;

/**
 * @author Mike Pot http://www.mikepot.com 
 *
 * Use is subject to license terms.
 * This file is provided with no warranty whatsoever.
 */

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.rmi.*;
import javax.ejb.*;

import org.apache.log4j.Logger;

import com.perpetual.util.EJBLoader;
import com.perpetual.viewer.model.ejb.MessagePatternHome;
import com.perpetual.viewer.model.ejb.MessagePattern;
import com.perpetual.viewer.model.ejb.MessagePatternUtil;
import com.perpetual.viewer.model.ejb.MessagePatternData;
import com.perpetual.viewer.model.ejb.DomainMessageHome;
import com.perpetual.viewer.model.ejb.DomainMessage;
import com.perpetual.viewer.model.ejb.DomainMessageUtil;
import com.perpetual.viewer.model.ejb.DomainMessageData;

import com.perpetual.util.PerpetualC2Logger;


/**
 * @ejb:bean name="MessagePatternCRUD" jndi-name="perpetual/MessagePatternCRUD" type="Stateless" view-type="remote"
   @ejb.transcation="NotSupported"
   @ejb.ejb-ref ejb-name="DomainMessage" view-type="remote" ref-name="DomainMessage"
   @ejb.ejb-ref ejb-name="MessagePattern" view-type="remote" ref-name="MessagePattern"
   @jboss.container-configuration name="SessionBean_Pool200"
 */
public abstract class MessagePatternCRUDBean implements SessionBean
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger(MessagePatternCRUDBean.class);
    private MessagePatternHome m_MessagePatternHome;
    private DomainMessageHome m_DomainMessageHome;
	
    
    /** @ejb.create-method */
	public void ejbCreate() throws CreateException, RemoteException
	{
		try
		{
			m_MessagePatternHome = MessagePatternUtil.getHome();
			m_DomainMessageHome = DomainMessageUtil.getHome();
		}
		catch (Throwable ex)
		{
			sLog.error("Couldn't initialize MessagePatternCRUDBean", ex);
			throw new CreateException("Couldn't initialize MessagePatternCRUDBean");
		}
	}
	
	/** @ejb.interface-method */
	public MessagePatternData retrieveByPrimaryKey(int id) throws RemoteException, FinderException
	{
		return m_MessagePatternHome.findByPrimaryKey(new Integer(id)).getData();
	}

	/** @ejb.interface-method */
	public Collection retrieveAll() throws RemoteException, FinderException
	{
		List list = new ArrayList();
		for (Iterator i = m_MessagePatternHome.findAll().iterator(); i.hasNext(); )
		{
			list.add(((MessagePattern)i.next()).getData());
		}
		return list;
	}
        /** @ejb.interface-method */
        public Collection retrieveMessagesFromDomain(int pDomainId) throws RemoteException, FinderException
        {
            
            return  m_DomainMessageHome.findByDomainId(pDomainId);    
            
        }
        

	/** @ejb.interface-method */
	public void deleteMessagePattern(int id) throws RemoteException, FinderException, RemoveException
	{
                Integer mpId = new Integer(id);
		MessagePattern messagePattern = m_MessagePatternHome.findByPrimaryKey(mpId);
                sLog.debug("deleting message pattern:"+mpId+":"+messagePattern.getName());
                Collection domainMessageList = m_DomainMessageHome.findByMessageId(id);
		Iterator it = domainMessageList.iterator();
                while(it.hasNext())
                {
                    DomainMessage dm = (DomainMessage)it.next();
                    dm.remove();
                }
                messagePattern.remove();
                 
	}
         /** @ejb.interface-method */
	public void deleteDomainMessage(DomainMessageData pDomainMsgData) throws RemoteException, FinderException, RemoveException
        {
            sLog.debug("Deleting domain-message for Domain:"+ pDomainMsgData.getDomainid()+" with message:"+pDomainMsgData.getMessageid());
            DomainMessage domainMessage = m_DomainMessageHome.findByPrimaryKey(pDomainMsgData.getId());
            domainMessage.remove();		
                        
        }

	/** @ejb.interface-method */
	public void addMessagePattern( MessagePatternData pMsgPatternData) throws RemoteException, CreateException, FinderException
	{
		sLog.info("!! " + pMsgPatternData);
		if (pMsgPatternData.getId() == null)
		{
			sLog.debug("Adding new message pattern");
			m_MessagePatternHome.create(pMsgPatternData);
                }
		else
		{
                        sLog.debug("Updating existing record:"+pMsgPatternData.getName());
			
			MessagePattern messagePattern = m_MessagePatternHome.findByPrimaryKey(pMsgPatternData.getId());
			
			// hand data back to entity bean
			//
			messagePattern.setData(pMsgPatternData);			
		}
	}
        /** @ejb.interface-method */
	public void addMessageToDomain(DomainMessageData pDomainMsgData) throws RemoteException, CreateException, FinderException
        {
                sLog.info("!! " + pDomainMsgData);
			sLog.debug("Adding msg:"+pDomainMsgData.getMessageid()+" to domain:"+pDomainMsgData.getDomainid());
			m_DomainMessageHome.create(pDomainMsgData);
                		
        }
       
	
}


 