/*
 * CacheItem.java
 *
 * Created on November 7, 2003, 4:04 PM
 */

package com.perpetual.logserver.util;

/**
 *
 * @author  brunob
 */
public interface CacheItem extends java.io.Serializable
{
    public void destroy();//for files this means closing up file
    public void instantiate() throws Exception;
    public String getKey();      
    public boolean inUse();
}

