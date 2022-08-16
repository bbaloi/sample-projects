package com.mule.connector.snmp.api.vo;

import java.io.InputStream;
import java.util.List;

public class SNMPEvent 
{
	private String snmpVersion;
	private String messageId;
	private String trapOid;
	private String enterpriseOid;
	private String specificOid;
	private String specificTrap;
	private String genericTrap;
	private String requestId;
	private String tstamp;
	private String agentAddr;
    private String enterprise;
    private String errorStatus;
	private String errorIndex;
	private String community;
	private Object payload;
	private String pduType;
	
	public String getPduType() {
		return pduType;
	}
	public void setPduType(String pduType) {
		this.pduType = pduType;
	}
	private List<SNMPEventVariable> variablList;
	
	
	public List<SNMPEventVariable> getVariablList() {
		return variablList;
	}
	public void setVariablList(List<SNMPEventVariable> variablList) {
		this.variablList = variablList;
	}
	public String getAgentAddr() {
		return agentAddr;
	}
	public void setAgentAddr(String agentAddr) {
		this.agentAddr = agentAddr;
	}
	public String getEnterprise() {
		return enterprise;
	}
	public void setEnterprise(String enterprise) {
		this.enterprise = enterprise;
	}
	
	public String getErrorStatus() {
		return errorStatus;
	}
	public void setErrorStatus(String errorStatus) {
		this.errorStatus = errorStatus;
	}
	public String getErrorIndex() {
		return errorIndex;
	}
	public void setErrorIndex(String errorIndex) {
		this.errorIndex = errorIndex;
	}
	
	public String getCommunity() {
		return community;
	}
	public void setCommunity(String community) {
		this.community = community;
	}
	public String getTstamp() {
		return tstamp;
	}
	public void setTstamp(String tstamp) {
		this.tstamp = tstamp;
	}
	
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getGenericTrap() {
		return genericTrap;
	}
	public void setGenericTrap(String genericTrap) {
		this.genericTrap = genericTrap;
	}
	public void setPayload(Object payload) {
		this.payload = payload;
	}
	public String getSpecificTrap() {
		return specificTrap;
	}
	public void setSpecificTrap(String specificTrap) {
		this.specificTrap = specificTrap;
	}
	public String getSpecificOid() {
		return specificOid;
	}
	public void setSpecificOid(String specificOid) {
		this.specificOid = specificOid;
	}
	public String getEnterpriseOid() {
		return enterpriseOid;
	}
	public void setEnterpriseOid(String enteprriseOid) {
		this.enterpriseOid = enteprriseOid;
	}
	public String getTrapOid() {
		return trapOid;
	}
	public void setTrapOid(String trapOid) {
		this.trapOid = trapOid;
	}
	public String getSnmpVersion() {
		return snmpVersion;
	}
	public void setSnmpVersion(String snmpVersion) {
		this.snmpVersion = snmpVersion;
	}
	public Object getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getMessageId() {
		return messageId;
	}
	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}
	public SNMPEvent()
	{
		
	}
	


}
