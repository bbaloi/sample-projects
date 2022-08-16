package com.perpetual.viewer.presentation;

import org.apache.struts.action.ActionForm;

public class UserActionForm extends ActionForm
{
    private String userId=null;
    private String password=null;
    private String serviceDomain= null;
    private String phone=null;
    private String email=null;
    private String realname=null;
    private boolean enabled=true;
    private int roleId;
    private int id;
	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return
	 */
	public boolean getEnabled() {
		return enabled;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @return
	 */
	public String getRealname() {
		return realname;
	}

	/**
	 * @return
	 */
	public int getRoleId() {
		return roleId;
	}

	/**
	 * @return
	 */
	public String getServiceDomain() {
		return serviceDomain;
	}

	/**
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param string
	 */
	public void setEmail(String string) {
		email = string;
	}

	/**
	 * @param b
	 */
	public void setEnabled(boolean b) {
		enabled = b;
	}

	/**
	 * @param i
	 */
	public void setId(int i) {
		id = i;
	}

	/**
	 * @param string
	 */
	public void setPassword(String string) {
		password = string;
	}

	/**
	 * @param string
	 */
	public void setPhone(String string) {
		phone = string;
	}

	/**
	 * @param string
	 */
	public void setRealname(String string) {
		realname = string;
	}

	/**
	 * @param i
	 */
	public void setRoleId(int i) {
		roleId = i;
	}

	/**
	 * @param string
	 */
	public void setServiceDomain(String string) {
		serviceDomain = string;
	}

	/**
	 * @param string
	 */
	public void setUserId(String string) {
		userId = string;
	}
	
	public String getName () 
	{
		return getServiceDomain(); 
	}
	
	public void setName (String name) {
		setServiceDomain(name);
	}

}
   