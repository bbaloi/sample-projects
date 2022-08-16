/*
 * LoginDelegateRemote.java
 *
 * Created on June 28, 2003, 3:59 PM
 */

package com.perpetual.viewer.control.ejb;

import javax.ejb.EJBObject;
import com.perpetual.viewer.control.LoginDelegate;
import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.model.vo.UserVO;
import com.perpetual.viewer.control.exceptions.ViewerLoginException;
/**
 *
 * @author  brunob
 */
/*public interface Login extends EJBObject,LoginDelegate
{
    public UserVO login(String pUserId,String pPassword) throws ViewerLoginException;
}*/
public interface UserProfile extends javax.ejb.EJBObject
{
    public UserProfileVO getUserProfile(UserVO userVO) throws java.rmi.RemoteException;
    public UserProfileVO getUserProfile(int pUserId) throws java.rmi.RemoteException;
}