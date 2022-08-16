/*
 * UserPK.java
 *
 * Created on June 28, 2003, 4:14 PM
 */

package com.perpetual.viewer.model.ejb;

import java.lang.Integer;

/**
 *
 * @author  brunob
 */
public class SummaryPK implements java.io.Serializable
{
     public int id;
    /** Creates a new instance of UserPK */
     
   public SummaryPK()
   {
   }
   public void setId(int pId)
   {
       id=pId;
   }
   public int getId()
   {
       return id;
   }
    
    public SummaryPK(int pId)
    {
         id = pId;
    }
    public boolean equals( Object pObject )
    {
	if( pObject instanceof SummaryPK ) 
        {
		
            if(this.id==((SummaryPK)pObject).id)
                return true;
	}
	return( false );
    }

    public int hashCode()
    {
         Integer hash = new Integer(id);         
         String str = hash.toString();
	return str.hashCode();
    }
    
}
