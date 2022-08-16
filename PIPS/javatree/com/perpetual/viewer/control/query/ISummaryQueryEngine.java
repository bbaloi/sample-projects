/*
 * ISummaryQueryEngine.java
 *
 * Created on August 1, 2003, 4:47 PM
 */

package com.perpetual.viewer.control.query;

import com.perpetual.exception.BasePerpetualException;
import java.util.Collection;

/**
 *
 * @author  brunob
 */
public interface ISummaryQueryEngine extends java.io.Serializable,IQueryEngine
{
    public Collection getSummaryRecords(SummaryFilter pFilter) throws java.rmi.RemoteException,javax.ejb.FinderException;
}
