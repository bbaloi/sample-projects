
package com.perpetual.viewer.control.ejb.crud.summary;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;
import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.model.ejb.Summary;
import com.perpetual.viewer.model.ejb.SummaryHome;
import com.perpetual.viewer.model.ejb.SummaryPK;
import com.perpetual.rp.model.vo.HostSummaryVO;
import com.perpetual.util.Constants;
import com.perpetual.util.ServiceLocator;
import com.perpetual.viewer.control.query.SummaryFilter;
import com.perpetual.viewer.control.query.AbstractQueryBuilder;
import com.perpetual.viewer.control.query.SummaryQueryBuilder;
import com.perpetual.exception.BasePerpetualException;

import com.perpetual.viewer.control.query.SummaryQueryEngine;
import com.perpetual.viewer.control.query.SummaryCursor;
import java.util.Map;


public class SummaryCRUDBean implements SessionBean 
{

	private static PerpetualC2Logger sLog = new PerpetualC2Logger(SummaryCRUDBean.class);
	
	SummaryHome summaryHome = null;
        Object homeObj=null;
        AbstractQueryBuilder queryBuilder=null;
	
	public void ejbCreate() 
	{
            sLog.debug("SummaryCRUD ejbCreate !");
            try
            {
	       homeObj = ServiceLocator.getServiceLocatorInstance().findHome(Constants.jndiName_Summary);
                    this.summaryHome = (SummaryHome) PortableRemoteObject.narrow(homeObj,
					SummaryHome.class);
            }
            catch(javax.naming.NamingException excp)
            {
                sLog.error("Cuuld not find Summay object !");
                excp.printStackTrace();
            }
	    queryBuilder = new SummaryQueryBuilder();
            
	}
    
	public SummaryCRUDBean() {
	}
	
	public HostSummaryVO create (HostSummaryVO pVo) throws RemoteException, CreateException
	{
		pVo.setId(-1);		
		Summary summary = this.summaryHome.create(pVo);
		
		// populate VO with generated id 
		return createNewVOFromBean(summary);
	}
	public synchronized SummaryCursor executeQuery(String pQuery,Map pParamMap) throws RemoteException
        {
            sLog.debug("Executing Query:"+pQuery);
            try
            {
                return SummaryQueryEngine.getInstance().executeQuery(pQuery,pParamMap);    
            }
            catch(BasePerpetualException excp)
            {
                throw new RemoteException("Executing Query Error !");
            }
          
        }
        public synchronized String constructQuery(Map pMap,Map pParamMap) throws RemoteException
        {
          sLog.debug("Invoking the SummaryQueryBuilder to build a query !");
          sLog.debug("--Incomming FilterMap:" + pMap);
          String query=null;
          try
          {
            query=queryBuilder.constructQuery(pMap,pParamMap);
          }
          catch(Exception excp)
          {
              throw new RemoteException(excp.getMessage());
          }
          return query;
        }
        public synchronized SummaryCursor getSummaryRecords(SummaryFilter pFilter) throws RemoteException,FinderException
        {
            sLog.debug("Getting summary Records !");
            Object [] queryParams=null;
            //Collection recordSet=null;
            SummaryCursor recordSet=null;
            //get records from SUmmary
            try
            {
                //Object homeObj =  ServiceLocator.getServiceLocatorInstance().findHome(Constants.jndiName_Summary);
                //SummaryHome lHome = (SummaryHome) PortableRemoteObject.narrow(homeObj,SummaryHome.class); 
                //sLog.debug("Got home, getting Records from SummaryBean  !");
                sLog.debug("Cosntructing Query !");
                HashMap paramMap = new HashMap();
                String query = queryBuilder.constructQuery(pFilter.getMap(),paramMap);
                //queryParams = queryBuilder.getQueryParameters(pFilter.getMap());
                sLog.debug("Executing Query !");
                recordSet = SummaryQueryEngine.getInstance().executeQuery(query,paramMap);    
                sLog.debug("Got Records from SummaryTable");
                //---------get other Vendor specific records-----------------
                //----------merge data from the SUmmary able and the Vendor info--------
            }
            /*catch(javax.naming.NamingException excp)
            {
                String msg = "Could not find Summary Home !";
                sLog.error(msg);
                ///throw new RemoteException(msg,excp);
            }*/
            catch(Exception excp)
            {
                throw new RemoteException("Database Access  Failure !",excp);
            }
            
            return recordSet;
        }
        
         public HostSummaryVO retrieveByPrimaryKey (SummaryPK pk) throws RemoteException, FinderException
         {
             HostSummaryVO vo=null;
             return vo;
         }
    
	public void delete (HostSummaryVO vo) throws RemoteException, RemoveException
	{
		try {
		
			Summary summary = this.summaryHome.findByPrimaryKey(new SummaryPK(vo.getId()));
			
			summary.remove();
			
		} catch (FinderException e) 
                {
                    sLog.error("Did not find appropriate record !");
		}
	}

	public void delete (int id) throws RemoteException, RemoveException
	{
		summaryHome.remove(new SummaryPK(id));
	}
	
	public HostSummaryVO retrieveByPrimaryKey (HostSummaryVO vo) throws RemoteException, FinderException
	{
		Summary summary = this.summaryHome.findByPrimaryKey(new SummaryPK(vo.getId()));
		
		return createNewVOFromBean(summary);
	}

	public HostSummaryVO retrieveByPrimaryKey(int id) throws RemoteException, FinderException
	{
		Summary summary = this.summaryHome.findByPrimaryKey(new SummaryPK(id));
		return createNewVOFromBean(summary);
	}

	public Collection retrieveAll () throws RemoteException, FinderException
	{
		List vos = new ArrayList();
		for (Iterator i = summaryHome.findAll().iterator(); i.hasNext(); )
			vos.add(createNewVOFromBean((Summary)i.next()));
		return vos;
	}

	private void populateBeanFromVO (Summary summary, HostSummaryVO vo) throws RemoteException
        {
		/*
             user.setUserid(vo.getUserId());
		if (vo.getPassword() != null)		// null means leave intact
			user.setUserpass(vo.getPassword());
		user.setServicedomain(vo.getServiceDomain());
		user.setPhone(vo.getPhone());
		user.setEmail(vo.getEmail());
		user.setRealname(vo.getRealname());
		user.setRoleid(vo.getRoleId());
		user.setEnabled(vo.isEnabled());
		user.setRealname(vo.getRealname());
                 **/
	}
	
	private HostSummaryVO createNewVOFromBean (Summary summary) throws RemoteException 
        {
            HostSummaryVO vo=null;
            /*
		return new UserVO(user.getUserid(), user.getUserpass(),
			user.getServicedomain(), user.getPhone(), user.getEmail(),
			user.getRealname(),  user.getRoleid(), user.getId(), user.getEnabled());
	*/
            return vo;
          }

	public void setSessionContext(SessionContext sessionContext)
		throws EJBException,RemoteException
	{
	}
   
	public void ejbRemove() throws RemoteException
	{
	}
   
	public void ejbActivate() throws RemoteException
	{
	}

	public void ejbPassivate()  throws RemoteException
	{
	}
}
