/*
 * Record.java
 *
 * Created on October 2, 2003, 4:01 PM
 */

package com.perpetual.reports;

import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author  brunob
 */
public final class Record 
{
    private Collection lFieldList = null;
    
    /** Creates a new instance of Record */
    public Record(Collection pFieldList) 
    {
        lFieldList = pFieldList;
    }
     public Record() 
    {
        lFieldList = new ArrayList();
    }
     public Collection getFieldList()
     {
         return lFieldList;
     }
     public void addField(Field pField)
     {
         lFieldList.add(pField);
     }
     public Field getField(String pName)
     {
         Field retField=null;
         
         Iterator it = lFieldList.iterator();
         while(it.hasNext())
         {
             Field field = (Field) it.next();
             if(pName.equals(field.getFieldName()))
             {
                retField = field;
                break;
             }
         }
         return retField;
         
     }
    
    
}
