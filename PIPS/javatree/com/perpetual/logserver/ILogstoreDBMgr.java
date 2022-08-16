/*
 * LogstoreDBMgr.java
 *
 * Created on November 14, 2003, 4:46 PM
 */

package com.perpetual.logserver;

import com.perpetual.logserver.model.vo.QueryRequestVO;
import java.util.Collection;

/**
 *
 * @author  brunob
 */
public interface ILogstoreDBMgr extends java.io.Serializable
{
  public QueryRequestVO threadWaiting();
  public void publishResult(QueryRequestVO vo);
  public void publishCursor(QueryRequestVO vo);
  public boolean getExit();
  public Cursor retrieveLogRecords(LogFilter pLogFilter,Collection pLogFileList);   
}