package com.tibco.stax.controller;

import com.tibco.stax.action.Action;
import com.tibco.stax.handler.IHandler;

public class TibcoStaxController extends Controller
{
	
	public TibcoStaxController()
	{
		super();
	}
	public TibcoStaxController(IHandler pHdl,Action pAction)
	{
		super(pHdl,pAction);
	}
}
