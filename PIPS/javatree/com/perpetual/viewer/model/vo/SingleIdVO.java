package com.perpetual.viewer.model.vo;

import java.io.Serializable;

public class SingleIdVO implements Serializable {
	
	protected int id = -1;
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
	
	public boolean equals (Object object) {
		
		if( object instanceof SingleIdVO ) 
		{	
			if (this.id == ((SingleIdVO) object).id)
				return true;
		}
		
		return( false );
	}
}
