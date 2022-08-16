/*
 * PageFormatAdapter.java
 *
 * Created on October 2, 2003, 4:28 PM
 */

package com.perpetual.reports;

/**
 *
 * @author  brunob
 */
public interface ExportFormatAdapter
{
    public void writePage(RecordSet pRecordSet,Header pHeader,int pPageNumber,int pPageSize);
    
}
