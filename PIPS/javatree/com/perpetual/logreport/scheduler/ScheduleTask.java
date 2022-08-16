package com.perpetual.logreport.scheduler;

import java.rmi.*;

import com.perpetual.logreport.model.ScheduleData;


public interface ScheduleTask
{
	public void execute(ScheduleData scheduleData, int referenceId) throws Exception, RemoteException;
}


