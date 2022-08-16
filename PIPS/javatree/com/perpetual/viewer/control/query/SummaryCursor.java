/*
 * SummaryCursor.java
 *
 * Created on August 16, 2003, 5:14 PM
 */

package com.perpetual.viewer.control.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.lang.Math;

import com.perpetual.viewer.model.vo.SummaryRecordVO;
import com.perpetual.util.PerpetualC2Logger;
/**
 *
 * @author  brunob
 */
public class SummaryCursor
{
    private static PerpetualC2Logger sLog = new PerpetualC2Logger( SummaryCursor.class );
 
    private int lRowCount=0;
    private int index=0;
    private int lPageSize=0;
    private ArrayList lRecordSet=null;
    
    /** Creates a new instance of SummaryCursor */
    public SummaryCursor(int pRecordSetSize ,int pPageSize,ArrayList pRecordset) 
    {
        lRowCount = pRecordSetSize;
        lPageSize = pPageSize;
        lRecordSet = pRecordset; 
        
    }
     public SummaryCursor(int pPageSize,ArrayList pRecordset) 
    {
        lPageSize = pPageSize;
        lRecordSet = pRecordset; 
        lRowCount = pRecordset.size();
        
     }
    public Iterator iterator()
    {
        return lRecordSet.iterator();
    }
    public boolean hasMore()
    {
        if(index<=lRowCount)
            return true;
        else
            return false;
    }
    public SummaryRecord nextRecord()
    {
        SummaryRecord record  =(SummaryRecord) lRecordSet.get(index);
        index++;        
        return record;
    }
    public int getNumPages()
    {
        double rowCount = new Double(lRowCount).doubleValue();
        double pageSize = new Double(lPageSize).doubleValue();
        double np = rowCount/pageSize;
        sLog.debug("double value numpages:"+np);
        double numPages =  Math.ceil(np);
        sLog.debug("num pages:"+numPages);
        
        return new Double(numPages).intValue();       
    }
    public void resetIndex()
    {
        index=0;
    }
    public Collection getPage(int pPageNum)
    {
        ArrayList pageList=new ArrayList();
        int pageStart = pPageNum*lPageSize;
        for(int i=pageStart;i<pageStart+lPageSize && i< lRowCount;i++)
        {
            pageList.add((SummaryRecordVO)lRecordSet.get(i));
        }
        return pageList;
    
    }
    public int size()
    {
        return lRowCount;
    }
    public int getPageSize()
    {
        return lPageSize;
    }
}
