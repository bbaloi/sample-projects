/*
 * UserBean.java
 *
 * Created on June 28, 2003, 4:08 PM
 */

package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EntityBean;
import javax.ejb.EntityContext;
import javax.ejb.FinderException;

import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.util.ejb.KeyGeneratorUtility;
import com.perpetual.rp.model.vo.HostSummaryVO;
import com.perpetual.viewer.control.query.SummaryFilter;
import java.util.Date;
import java.util.Collection;
/**
 *
 * @author  brunob
 */
public abstract class SummaryBean implements EntityBean
{
     private static PerpetualC2Logger sLog = new PerpetualC2Logger( SummaryBean.class );
  
    
    /** Creates a new instance of UserBean */
     public SummaryBean() 
    {
    }
    public abstract int getId();
    public abstract void setId(int pId);
    public abstract Date getStartdate();
    public abstract void setStartdate(Date pStartDate);
    public abstract Date getEnddate();
    public abstract void setEnddate(Date pEndDate);
    public abstract String getHostname();
    public abstract void setHostname(String pHostName);
    public abstract String getDomainname();
    public abstract void setDomainname(String pDomainName);
    public abstract Integer getMessagePatternId();
    public abstract void setMessagePatternId(Integer pPatternId);
    public abstract int getFacility0();
    public abstract void setFacility0(int pFacility0);
    public abstract int getFacility1();
    public abstract void setFacility1(int pFacility1);
    public abstract int getFacility2();
    public abstract void setFacility2(int pFacility2);
    public abstract int getFacility3();
    public abstract void setFacility3(int pFacility3);
    public abstract int getFacility4();
    public abstract void setFacility4(int pFacility4);
    public abstract int getFacility5();
    public abstract void setFacility5(int pFacility5);
    public abstract int getFacility6();
    public abstract void setFacility6(int pFacility6);
    public abstract int getFacility7();
    public abstract void setFacility7(int pFacility7);
    public abstract int getFacility8();
    public abstract void setFacility8(int pFacility8);
    public abstract int getFacility9();
    public abstract void setFacility9(int pFacility9);
    public abstract int getFacility10();
    public abstract void setFacility10(int pFacility10);
    public abstract int getFacility11();
    public abstract void setFacility11(int pFacility11);
    public abstract int getFacility12();
    public abstract void setFacility12(int pFacility12);
    public abstract int getFacility13();
    public abstract void setFacility13(int pFacility13);
    public abstract int getFacility14();
    public abstract void setFacility14(int pFacility14);
    public abstract int getFacility15();
    public abstract void setFacility15(int pFacility15);
    public abstract int getFacility16();
    public abstract void setFacility16(int pFacility16);
    public abstract int getFacility17();
    public abstract void setFacility17(int pFacility17);
    public abstract int getFacility18();
    public abstract void setFacility18(int pFacility18);
    public abstract int getFacility19();
    public abstract void setFacility19(int pFacility19);
    public abstract int getFacility20();
    public abstract void setFacility20(int pFacility20);
    public abstract int getFacility21();
    public abstract void setFacility21(int pFacility21);
    public abstract int getFacility22();
    public abstract void setFacility22(int pFacility22);
    public abstract int getFacility23();
    public abstract void setFacility23(int pFacility23);
    public abstract int getSeverity0();
    public abstract void setSeverity0(int pSeverity0);
    public abstract int getSeverity1();
    public abstract void setSeverity1(int pSeverity1);
    public abstract int getSeverity2();
    public abstract void setSeverity2(int pSeverity2);
    public abstract int getSeverity3();
    public abstract void setSeverity3(int pSeverity3);
    public abstract int getSeverity4();
    public abstract void setSeverity4(int pSeverity4);
    public abstract int getSeverity5();
    public abstract void setSeverity5(int pSeverity5);
    public abstract int getSeverity6();
    public abstract void setSeverity6(int pSeverity6);
    public abstract int getSeverity7();
    public abstract void setSeverity7(int pSeverity7);
    
    
     
    
    /*
     public UserVO getVO() throws RemoteException
	{
		return new UserVO(getUserid(), getUserpass(), getServicedomain(), getPhone(), getEmail(), getRealname(), getRoleid(), getId(), getEnabled());
	}
*/
    public SummaryPK ejbCreate(HostSummaryVO pSummary) throws javax.ejb.CreateException, java.rmi.RemoteException
    {
        sLog.debug("Creating SummaryBean !");
        SummaryPK summaryPrimary=null;
        
        int id = -1;
            if (pSummary.getId() == -1) {
			id = KeyGeneratorUtility.generateKey();
		}
		setId(id);
                         
            //setStartdate(pSummary.getStartTimeStamp().toString());
            //setEnddate(pSummary.getEndTimeStamp().toString());
            setStartdate(pSummary.getStartTimeStamp());
            setEnddate(pSummary.getEndTimeStamp());
            setHostname(pSummary.getHostName());
            setDomainname(pSummary.getDomainName());
            setMessagePatternId(pSummary.getMessagePatternId());
            setFacility0(pSummary.fac0);
            setFacility1(pSummary.fac1);
            setFacility2(pSummary.fac2);
            setFacility2(pSummary.fac3);
            setFacility4(pSummary.fac4);
            setFacility5(pSummary.fac5);
            setFacility6(pSummary.fac6);
            setFacility7(pSummary.fac7);
            setFacility8(pSummary.fac8);
            setFacility9(pSummary.fac9);
            setFacility10(pSummary.fac10);
            setFacility11(pSummary.fac11);
            setFacility12(pSummary.fac12);
            setFacility13(pSummary.fac13);
            setFacility14(pSummary.fac14);
            setFacility15(pSummary.fac15);
            setFacility16(pSummary.fac16);
            setFacility17(pSummary.fac17);
            setFacility18(pSummary.fac18);
            setFacility19(pSummary.fac19);
            setFacility20(pSummary.fac20);
            setFacility21(pSummary.fac21);
            setFacility22(pSummary.fac22);
            setFacility23(pSummary.fac23);
            setSeverity0(pSummary.sev0);
            setSeverity1(pSummary.sev1);
            setSeverity2(pSummary.sev2);
            setSeverity3(pSummary.sev3);
            setSeverity4(pSummary.sev4);
            setSeverity5(pSummary.sev5);
            setSeverity6(pSummary.sev6);
            setSeverity7(pSummary.sev7);            
                
         sLog.debug("Set the bean up !");
         
         summaryPrimary = new SummaryPK(getId());
         
         sLog.debug("Created PrimaryKey !");
         
        return summaryPrimary;
    }
     public void ejbPostCreate(HostSummaryVO pSummary) throws javax.ejb.CreateException
    {
        // Do nothing for now.
        //return;
    }
    /*public Collection getSummaryByCriteria(String jBossQl,Object [] args) throws FinderException, RemoteException
    {
        return ejbSelectSummaryByCriteria(jBossQl,args);
    }*/

   // public abstract Collection ejbSelectSummaryByCriteria(String jBossQl,Object [] args) throws FinderException, RemoteException;

    public void ejbRemove() throws RemoteException
    {
    }
    public void ejbActivate() throws RemoteException
    {
    }
    public void ejbPassivate()  throws RemoteException
    {
    }
    public void setEntityContext(EntityContext entityContext)  throws EJBException,RemoteException
    {
    }
    public void unsetEntityContext() throws EJBException,RemoteException
    {
    }
    public void ejbLoad() throws EJBException,RemoteException
    {
    }
    public void ejbStore() throws EJBException,RemoteException
    {
    }
   
}
