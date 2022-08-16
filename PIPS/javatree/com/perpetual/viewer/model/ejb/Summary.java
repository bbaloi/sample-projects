/*
 * UserRemote.java
 *
 * Created on June 28, 2003, 4:07 PM
 */

package com.perpetual.viewer.model.ejb;

import java.rmi.RemoteException;
import java.util.Date;

import javax.ejb.EJBObject;
/**
 *
 * @author  brunob
 */
public interface Summary extends EJBObject
{
    
   // public void deleteUser(UserPK pPrimayKey) throws java.rmi.RemoteException;
    //public void updateUser(UserVO pUser) throws java.rmi.RemoteException;
    public int getId() throws RemoteException;
    public void setId(int pId) throws RemoteException;
    public Date getStartdate() throws RemoteException;
    public void setStartdate(Date pStartDate) throws RemoteException;
    public Date getEnddate() throws RemoteException;
    public void setEnddate(Date pEndDate) throws RemoteException;
    public String getHostname() throws RemoteException;
    public void setHostname(String pHostName) throws RemoteException;
    public String getDomainname() throws RemoteException;
    public void setDomainname(String pDomainName) throws RemoteException; 
    public Integer  getMessagePatternId() throws RemoteException;
    public void setMessagePatternId(Integer pPatternId) throws RemoteException;
    public int getFacility0() throws RemoteException;
    public void setFacility0(int pFacility0) throws RemoteException;
    public int getFacility1() throws RemoteException;
    public void setFacility1(int pFacility1) throws RemoteException;
    public int getFacility2() throws RemoteException;
    public void setFacility2(int pFacility2) throws RemoteException;
    public int getFacility3() throws RemoteException;
    public void setFacility3(int pFacility3) throws RemoteException;
    public int getFacility4() throws RemoteException;
    public void setFacility4(int pFacility4) throws RemoteException;
    public int getFacility5() throws RemoteException;
    public void setFacility5(int pFacility5) throws RemoteException;
    public int getFacility6() throws RemoteException;
    public void setFacility6(int pFacility6) throws RemoteException;
    public int getFacility7() throws RemoteException;
    public void setFacility7(int pFacility7) throws RemoteException;
    public int getFacility8() throws RemoteException;
    public void setFacility8(int pFacility8) throws RemoteException;
    public int getFacility9() throws RemoteException;
    public void setFacility9(int pFacility9) throws RemoteException;
    public int getFacility10() throws RemoteException;
    public void setFacility10(int pFacility10) throws RemoteException;
    public int getFacility11() throws RemoteException;
    public void setFacility11(int pFacility11) throws RemoteException;
    public int getFacility12() throws RemoteException;
    public void setFacility12(int pFacility12) throws RemoteException;
    public int getFacility13() throws RemoteException;
    public void setFacility13(int pFacility13) throws RemoteException;
    public int getFacility14() throws RemoteException;
    public void setFacility14(int pFacility14) throws RemoteException;
    public int getFacility15() throws RemoteException;
    public void setFacility15(int pFacility15) throws RemoteException;
    public int getFacility16() throws RemoteException;
    public void setFacility16(int pFacility16) throws RemoteException;
    public int getFacility17() throws RemoteException;
    public void setFacility17(int pFacility17) throws RemoteException;
    public int getFacility18() throws RemoteException; 
    public void setFacility18(int pFacility18) throws RemoteException;
    public int getFacility19() throws RemoteException;
    public void setFacility19(int pFacility19) throws RemoteException;
    public int getFacility20() throws RemoteException;
    public void setFacility20(int pFacility20) throws RemoteException;
    public int getFacility21() throws RemoteException;
    public void setFacility21(int pFacility21) throws RemoteException;
    public int getFacility22() throws RemoteException;
    public void setFacility22(int pFacility22) throws RemoteException;
    public int getFacility23() throws RemoteException;
    public void setFacility23(int pFacility23) throws RemoteException;
    public int getSeverity0() throws RemoteException;
    public void setSeverity0(int pSeverity0) throws RemoteException;
    public int getSeverity1() throws RemoteException;
    public void setSeverity1(int pSeverity1) throws RemoteException;
    public int getSeverity2() throws RemoteException;
    public void setSeverity2(int pSeverity2) throws RemoteException;
    public int getSeverity3() throws RemoteException;
    public void setSeverity3(int pSeverity3) throws RemoteException;
    public int getSeverity4() throws RemoteException;
    public void setSeverity4(int pSeverity4) throws RemoteException;
    public int getSeverity5() throws RemoteException;
    public void setSeverity5(int pSeverity5) throws RemoteException;
    public int getSeverity6() throws RemoteException;
    public void setSeverity6(int pSeverity6) throws RemoteException;
    public int getSeverity7() throws RemoteException;
    public void setSeverity7(int pSeverity7) throws RemoteException;
}


