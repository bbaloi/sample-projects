/*
 * RPConfigManager.java
 *
 * Created on July 15, 2003, 1:23 PM
 */

package com.perpetual.recordprocessor.control;

import java.util.Map;

import com.perpetual.recordprocessor.control.domain.IDomainChangeObserver;
import com.perpetual.recordprocessor.control.domain.IDomainMapHolder;
import com.perpetual.util.PerpetualC2Logger;
import com.perpetual.viewer.control.ejb.crud.domain.DomainCRUDHome;

/**
 *
 * @author  brunob
 */
public class RPConfigManager implements IDomainMapHolder
{
    private PerpetualC2Logger sLog = new PerpetualC2Logger( RPConfigManager.class );    
   
    private String lTemplateFile=null;
    
    private DomainCRUDHome domainCRUDHome = null;
    private Map domainMap;
    
    
    /** Creates a new instance of RPConfigManager */
    public RPConfigManager()
    {
        sLog.debug("RPConfigManager constructor");

        initialize();
    }
    
	private void initialize()
	{
    }

	public Map getDomainMap()
	{
		return this.domainMap;
	}

	/**
	 * @param map
	 */
	public void setDomainMap(Map map) {
		domainMap = map;
	}

}
