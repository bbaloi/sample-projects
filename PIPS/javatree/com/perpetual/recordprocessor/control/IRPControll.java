/*
 * IRPControl.java
 *
 * Created on July 15, 2003, 1:03 PM
 */

package com.perpetual.recordprocessor.control;

import com.perpetual.exception.BasePerpetualException;

/**
 *
 * @author  brunob
 */
public interface IRPControll 
{
   public void reloadLogstore() throws BasePerpetualException;
   public void navigateLogstore() throws BasePerpetualException;
}