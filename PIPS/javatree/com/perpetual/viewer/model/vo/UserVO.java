/*
 * UserProfileVO.java
 *
 * Created on June 28, 2003, 3:42 PM
 */

package com.perpetual.viewer.model.vo;

/**
 *
 * @author  brunob
 */
public class UserVO implements java.io.Serializable
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
    /** Creates a new instance of UserProfileVO */
    public UserVO(int pId)
	{
		id = pId;
	}

	public boolean equals(Object other)		// so we can use Collections.removeAll by VO's
	{
		return other instanceof UserVO && ((UserVO)other).id == id;
	}

    public UserVO(String pUserId,String pPassword,String pServiceDomain,String pPhone,
                    String pEmail,String pRealname,int pRole,int pId,boolean pEnabled) 
    {
        userId=pUserId;
        password=pPassword;
        serviceDomain=pServiceDomain;
        phone=pPhone;
        email=pEmail;
        roleId=pRole;
        realname=pRealname;
        id=pId;
        enabled=pEnabled;
    }
   
	/**
	 * @return
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return
	 */
	public boolean isEnabled() {
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

}
