/*
 * ICollectionReset.java
 *
 * Created on July 29, 2003, 1:01 PM
 */

package com.perpetual.rp.control;

import java.util.Date;
/**
 *
 * @author  brunob
 */
public interface ICollectionTime
{
    public void resetLastColectionDate(String pDomainName,Date pCollectionDate);
    public Date getLastCollectionDate(String pDomain);
}
