package com.extemp.semantic.vo;

import java.util.List;

import com.hp.hpl.jena.rdf.model.Statement;

public class IntentVO 
{
	String userId;
	List stmtList;
	
	public IntentVO(String pUserId,List pStmtList)
	{
		userId = pUserId;
		stmtList=pStmtList;
	}
	
	public List getStmtList() {
		return stmtList;
	}
	public void setStmtList(List pStmtList) {
		this.stmtList = pStmtList;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
	

}
