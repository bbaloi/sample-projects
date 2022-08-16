/*
 * ViewRawLogForm.java
 *
 * Created on May 17, 2003, 6:45 PM
 */

package com.perpetual.viewer.presentation;

import org.apache.struts.action.ActionForm;
import java.util.HashMap;

/**
 *
 * @author  brunob
 */
public class LoginForm extends ActionForm
{
    private String userId=null;
    private String password=null;
        
    /** Creates a new instance of ViewRawLogForm */
    public LoginForm() 
    {
    }
    public void setUserId(String pUserId)
    {
        userId=pUserId;
    }
    public String getUserId()
    {
        return userId;
    }
    public void setPassword(String pPassword)
    {
        password = pPassword;
    }
    public String getPassword()
    {
        return password;
    }
}
