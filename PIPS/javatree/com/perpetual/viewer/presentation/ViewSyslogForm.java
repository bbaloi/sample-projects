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
public class ViewSyslogForm extends ActionForm
{
    private String fileName=null;
    private HashMap queryParameters= null;
    
    /** Creates a new instance of ViewRawLogForm */
    public ViewSyslogForm() 
    {
    }
    public void setFileName(String pFileName)
    {
        fileName=pFileName;
    }
    public String getFileName()
    {
        return fileName;
    }
    public void setQueryParameter(HashMap pMap)
    {
        queryParameters = pMap;
    }
    public HashMap getQueryParameters()
    {
        return queryParameters;
    }
}
