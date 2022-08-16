/*
 * ISummaryCollectionQuery.java
 *
 * Created on July 22, 2003, 8:48 PM
 */

package com.perpetual.rp.control.query;

import com.perpetual.exception.BasePerpetualException;
import java.util.List;

/**
 *
 * @author  brunob
 */
public interface ISummaryCollectionQuery 
{
    public List getSummaryRecords(SummaryQueryTemplate pTemplate,boolean pLogStore) throws BasePerpetualException;
    
}
