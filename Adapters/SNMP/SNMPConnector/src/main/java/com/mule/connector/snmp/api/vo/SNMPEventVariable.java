package com.mule.connector.snmp.api.vo;

public class SNMPEventVariable 
{
    private String oid;
    private String value;	        
    private String syntaxstring;
    private int syntax;
    
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getSyntaxstring() {
		return syntaxstring;
	}
	public void setSyntaxstring(String syntaxstring) {
		this.syntaxstring = syntaxstring;
	}
	public int getSyntax() {
		return syntax;
	}
	public void setSyntax(int syntax) {
		this.syntax = syntax;
	}
    
}
