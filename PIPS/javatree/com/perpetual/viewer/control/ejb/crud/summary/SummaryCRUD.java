package com.perpetual.viewer.control.ejb.crud.summary;

import java.rmi.RemoteException;
import java.util.Collection;

import javax.ejb.CreateException;
import javax.ejb.FinderException;
import javax.ejb.RemoveException;
import com.perpetual.viewer.control.query.ISummaryQueryEngine;
import com.perpetual.viewer.control.query.IQueryBuilder;
import com.perpetual.viewer.control.query.SummaryFilter;
import com.perpetual.exception.BasePerpetualException;

import com.perpetual.rp.model.vo.HostSummaryVO;
import com.perpetual.viewer.model.ejb.Summary;
import com.perpetual.viewer.model.ejb.SummaryPK;

import com.perpetual.viewer.control.query.SummaryCursor;

import java.util.Map;

public interface SummaryCRUD extends javax.ejb.EJBObject
//public interface SummaryCRUD extends javax.ejb.EJBObject,ISummaryQueryEngine,IQueryBuilder
{        
    
    public HostSummaryVO create(HostSummaryVO vo) throws RemoteException, CreateException;
    public void delete(HostSummaryVO vo) throws RemoteException, RemoveException; 
    public void delete(int id) throws RemoteException, RemoveException; 
   
     // read methods   
    public HostSummaryVO retrieveByPrimaryKey (SummaryPK pk) throws RemoteException, FinderException;
    public HostSummaryVO retrieveByPrimaryKey (int id) throws RemoteException, FinderException;
    public Collection retrieveAll() throws RemoteException, FinderException;
    //public Collection getSummaryRecords(SummaryFilter pFilter) throws RemoteException,FinderException;
    public String constructQuery(Map pMap,Map pParamMap) throws RemoteException;
    //public Collection executeQuery(String pQuery,Map pParamMap) throws RemoteException;   
    //public Collection getSummaryRecords(SummaryFilter pFilter) throws RemoteException,FinderException;
    public SummaryCursor executeQuery(String pQuery,Map pParamMap) throws RemoteException;   
    public SummaryCursor getSummaryRecords(SummaryFilter pFilter) throws RemoteException,FinderException;

}


