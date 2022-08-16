/*
 * QueryRequestVO.java
 *
 * Created on November 14, 2003, 3:29 PM
 */

package com.perpetual.logserver.model.vo;
import com.perpetual.logserver.util.LogserverConstants;
import java.util.Collection;
import com.perpetual.logserver.ILogstoreDBMgr;
import com.perpetual.logserver.Cursor;
import com.perpetual.logserver.LogFilter;
import java.util.LinkedList;
import com.perpetual.logserver.util.InboxListener;

/**
 *
 * @author  brunob
 */
public final class QueryRequestVO implements java.io.Serializable
{
    private double uid;
    private int state=LogserverConstants.STATE_0;
    private Object payload;
    private Collection logFileList=null;
    private ILogstoreDBMgr dbMgr=null;
    private LogFilter logFilter=null;
    private int linesPerPage=0;
    private int pageNum=0;
    private InboxListener lInboxListener=null;
    /** Creates a new instance of QueryRequestVO */
    public QueryRequestVO(double pUid,Object pPayload,ILogstoreDBMgr pDbMgr,
                Collection pLogFileList,LogFilter pFilter,int pLinesPerPage) 
    {
        uid = pUid;
        payload=pPayload;
        dbMgr = pDbMgr;
        logFileList = pLogFileList;
        logFilter = pFilter;
        linesPerPage=pLinesPerPage;
        
    }
    public QueryRequestVO(double pUid,Object pPayload,ILogstoreDBMgr pDbMgr,
                Collection pLogFileList,LogFilter pFilter,int pLinesPerPage,InboxListener pInboxListener) 
    {
        uid = pUid;
        payload=pPayload;
        dbMgr = pDbMgr;
        logFileList = pLogFileList;
        logFilter = pFilter;
        linesPerPage=pLinesPerPage;
        lInboxListener=pInboxListener;
    }
    public QueryRequestVO(double pUid,Object pPayload,ILogstoreDBMgr pDbMgr,
                Collection pLogFileList,LogFilter pFilter,int pLinesPerPage,int pPageNumber) 
    {
        uid = pUid;
        payload=pPayload;
        dbMgr = pDbMgr;
        logFileList = pLogFileList;
        logFilter = pFilter;
        linesPerPage=pLinesPerPage;
        pageNum=pPageNumber;
    }
     public QueryRequestVO(double pUid,Object pPayload,ILogstoreDBMgr pDbMgr,
                Collection pLogFileList,LogFilter pFilter,int pLinesPerPage,int pPageNumber,InboxListener pInboxListener) 
    {
        uid = pUid;
        payload=pPayload;
        dbMgr = pDbMgr;
        logFileList = pLogFileList;
        logFilter = pFilter;
        linesPerPage=pLinesPerPage;
        pageNum=pPageNumber;
        lInboxListener=pInboxListener;
    }
    public Object getPayload()
    {
        return payload;
    }
    public double getUID()
    {
        return uid;
    }
    public int getState()
    {
        return state;
    }
    public void setState(int pState)
    {
        state=pState;
    }
    public void setPayload(Object pPayload)
    {
        payload=pPayload;
    }
    public ILogstoreDBMgr getLogstoreDBMgr()
    {
        return dbMgr;
    }
    public Collection getLogFileList()
    {
        return logFileList;
    }
    public LogFilter getFilter()
    {
        return logFilter;
    }
    public int getLinesPerPage()
    {
        return linesPerPage;
    }
    public int getPageNumber()
    {
        return pageNum;
    }
    public void setPageNumber(int num)
    {
        pageNum=num;
    }
    public InboxListener getInboxListener()
    {
        return lInboxListener;
    }
 
}
