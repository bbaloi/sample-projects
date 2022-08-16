/*
 * LoginDelegate.java
 *
 * Created on June 28, 2003, 3:40 PM
 */

package com.perpetual.viewer.control;

import com.perpetual.viewer.model.vo.UserProfileVO;
import com.perpetual.viewer.control.exceptions.ViewerLoginException;

/**
 *
 * @author  brunob
 */
public interface LoginDelegate 
{
    public UserProfileVO login(String pUserId,String pPassword) throws ViewerLoginException;
    
}
