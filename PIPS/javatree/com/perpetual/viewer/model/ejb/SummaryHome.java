/*
 * UserHome.java
 *
 * Created on June 28, 2003, 4:08 PM
 */

package com.perpetual.viewer.model.ejb;

import javax.ejb.EJBHome;
import com.perpetual.rp.model.vo.HostSummaryVO;
import java.util.Collection;
import javax.ejb.FinderException;
import java.rmi.RemoteException;
import com.perpetual.viewer.control.query.SummaryFilter;

/**
 *
 * @author  brunob
 */
public interface SummaryHome extends EJBHome
{
    public Summary create(HostSummaryVO pSummary) throws javax.ejb.CreateException, java.rmi.RemoteException;
    public Summary findByPrimaryKey( SummaryPK primaryKey ) throws FinderException, RemoteException;
    public Collection findAll() throws FinderException, RemoteException;
    //public Collection getSummaryByCriteria(String jBossQl,Object [] args) throws FinderException, RemoteException;
}