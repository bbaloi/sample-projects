/*
 * UserPK.java
 *
 * Created on June 28, 2003, 4:14 PM
 */

package com.perpetual.viewer.model.ejb;

import java.lang.Integer;

public class SeverityPK implements java.io.Serializable
{
     public int id;
   
   // default constructor - required by the container
     
   public SeverityPK()
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
    
    public SeverityPK(int pId)
    {
         id = pId;
    }
    public boolean equals( Object pObject )
    {
	if( pObject instanceof SeverityPK ) 
        {
		
            if(this.id==((SeverityPK)pObject).id)
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
