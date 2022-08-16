/*
 * UserPK.java
 *
 * Created on June 28, 2003, 4:14 PM
 */

package com.perpetual.viewer.model.ejb;

/**
 *
 * @author  brunob
 */
public class RoleActionPK implements java.io.Serializable
{
    public int id;
    
    public RoleActionPK() {
    	
    }
    
    public RoleActionPK( int id )
    {
         this.id = id;
    }
    
    public boolean equals( Object pObject )
    {
		if( pObject instanceof RoleActionPK ) 
	        {
			return this.id == ((RoleActionPK)pObject).id ;
		}
	
		return( false );
    }

    public int hashCode()
    {
		Integer hash = new Integer(this.id);         
		String str = hash.toString();
		
   		return str.hashCode();
    }
    
	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @param i
	 */
	public void setId(int i) {
		id = i;
	}

}
