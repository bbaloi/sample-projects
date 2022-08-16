/*
 * UserPK.java
 *
 * Created on June 28, 2003, 4:14 PM
 */

package com.perpetual.viewer.model.ejb;

import java.lang.Integer;

public class ActionPK implements java.io.Serializable
{
     public int id;
   
   // default constructor - required by the container
     
   public ActionPK()
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
    
    public ActionPK(int pId)
    {
         id = pId;
    }
    public boolean equals( Object pObject )
    {
	if( pObject instanceof ActionPK ) 
        {
		
            if(this.id==((ActionPK)pObject).id)
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
