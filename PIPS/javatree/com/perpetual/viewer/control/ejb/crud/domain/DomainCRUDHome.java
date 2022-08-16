package com.perpetual.viewer.control.ejb.crud.domain;

import javax.ejb.EJBHome;


public interface DomainCRUDHome extends EJBHome
{
    DomainCRUD create()  throws javax.ejb.CreateException,java.rmi.RemoteException;    
}
