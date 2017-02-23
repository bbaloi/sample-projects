package com.tibco.xpdl.bw.vo;

public class JMSValuesVO
{
	private String jmsConnectionName;
	private String jmsResourceType;
	private String userJNDI;
	private String providerURL;
	private String namingURL;
	private String initialContextFactory;
	private String topicFactory;
	private String queueFactory;
	private String namingPrincipal;
	private String namingPassword;
	private String user;
	private String passwd;
	private String clientId;
	private boolean useXACF;
	
	public JMSValuesVO(String pConnectionName,String pResourceType,String pUserJNDI, String pProviderURL,
			String pNamingURL,String pCtxtFactory,String pTopicFactory,
			String pQueueFactory, String pNamingPrincipal,String pNamingPasswd,
			String pUser,String pPasswd, String pClientId,boolean pUseXACF)
	{
		jmsConnectionName=pConnectionName;
		jmsResourceType=pResourceType;
		userJNDI=pUserJNDI;
		providerURL=pProviderURL;
		namingURL=pNamingURL;
		initialContextFactory=pCtxtFactory;
		topicFactory=pTopicFactory;
		queueFactory=pQueueFactory;
		namingPrincipal=pNamingPrincipal;
		namingPassword=pNamingPasswd;
		user=pUser;
		passwd=pPasswd;
		clientId=pClientId;
		useXACF=pUseXACF;
		
		
	}

	public String getClientId() {
		return clientId;
	}

	public String getInitialContextFactory() {
		return initialContextFactory;
	}

	public String getNamingPassword() {
		return namingPassword;
	}

	public String getNamingPrincipal() {
		return namingPrincipal;
	}

	public String getNamingURL() {
		return namingURL;
	}

	public String getPasswd() {
		return passwd;
	}

	public String getProviderURL() {
		return providerURL;
	}

	public String getQueueFactory() {
		return queueFactory;
	}

	public String getTopicFactory() {
		return topicFactory;
	}

	public String getUser() {
		return user;
	}

	public String getUserJNDI() {
		return userJNDI;
	}

	public boolean isUseXACF() {
		return useXACF;
	}

	public String getJmsConnectionName() {
		return jmsConnectionName;
	}

	public String getJmsResourceType() {
		return jmsResourceType;
	}
}