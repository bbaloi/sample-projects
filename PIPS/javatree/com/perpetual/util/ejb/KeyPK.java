/*
 * UserPK.java
 *
 * Created on June 28, 2003, 4:14 PM
 */

package com.perpetual.util.ejb;


public class KeyPK implements java.io.Serializable
{
	public String name;

	public KeyPK() {}
   
	public void setName(String name)
	{
		this.name = name;
	}
   
	public String getName()
	{
		return this.name;
	}
    
    public KeyPK(String name)
    {
    	setName(name);
    }
    
    public boolean equals( Object pObject )
    {
    	if( pObject instanceof KeyPK ) 
        {
			if (this.getName().equals(((KeyPK) pObject).getName())) {
                return true;
			}
		}
		
		return( false );
    }

    public int hashCode()
    { 
		return this.name.hashCode();
    }
}
