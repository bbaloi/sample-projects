/*
 * RecordSet.java
 *
 * Created on October 2, 2003, 4:14 PM
 */

package com.perpetual.reports;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
/**
 *
 * @author  brunob
 */
public final class RecordSet 
{
    private Collection lRecordList=null;
    /** Creates a new instance of RecordSet */
    public RecordSet() 
    {
        lRecordList = new ArrayList();
    }
    public RecordSet(Collection pRecordSet)
    {
        lRecordList = pRecordSet;
    }
    public Collection getRecordCollection()
    {
        return lRecordList;
    }
    public Record getRecord(int index)
    {
        int size= lRecordList.size();
        Iterator it = lRecordList.iterator();
        for(int i=0;i<size;i++)
        {
            Record record=(Record) it.next();
            if(i==index)
                  return record;           
        }
        return null;
    }
    public void addRecord(Record pRecord)
    {
        lRecordList.add(pRecord);
    }
    public long size()
    {
        return lRecordList.size();
    }
}

