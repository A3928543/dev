package com.banorte.site.mensajes.listener.vo;

import java.io.Serializable;

public class ConfiguracionVO implements Serializable
{
	private static final long serialVersionUID = 1L;

	private String queueManagerHost;
	private int    queueManagerPort;
	private String queueManagerChannel;
	private String queueManager;
	private String queueIn;
	private String userIdMCA;
	
	private String dataSourceName;
	private String initialContextFactory;
	private String providerURL;
	
	
	public String toString() 
	{
		return "{" +
				"[ConfiguracionVO{queueManagerHost:"+queueManagerHost+", queueManagerPort:"+queueManagerPort+
				", queueManagerChannel:"+queueManagerChannel+", queueManager:"+queueManager+", queueIn:"+queueIn+
				", dataSourceName:"+dataSourceName+", initialContextFactory:"+initialContextFactory+
				", providerURL:"+providerURL+", userIdMCA:" + userIdMCA + "}]";
	}
	
	public String getQueueManagerHost() {
		return queueManagerHost;
	}
	public void setQueueManagerHost(String queueManagerHost) {
		this.queueManagerHost = queueManagerHost;
	}
	public int getQueueManagerPort() {
		return queueManagerPort;
	}
	public void setQueueManagerPort(int queueManagerPort) {
		this.queueManagerPort = queueManagerPort;
	}
	public String getQueueManagerChannel() {
		return queueManagerChannel;
	}
	public void setQueueManagerChannel(String queueManagerChannel) {
		this.queueManagerChannel = queueManagerChannel;
	}
	public String getQueueManager() {
		return queueManager;
	}
	public void setQueueManager(String queueManager) {
		this.queueManager = queueManager;
	}
	public String getQueueIn() {
		return queueIn;
	}
	public void setQueueIn(String queueIn) {
		this.queueIn = queueIn;
	}
	public String getDataSourceName() {
		return dataSourceName;
	}
	public void setDataSourceName(String dataSourceName) {
		this.dataSourceName = dataSourceName;
	}
	public String getInitialContextFactory() {
		return initialContextFactory;
	}
	public void setInitialContextFactory(String initialContextFactory) {
		this.initialContextFactory = initialContextFactory;
	}
	public String getProviderURL() {
		return providerURL;
	}
	public void setProviderURL(String providerURL) {
		this.providerURL = providerURL;
	}

	public String getUserIdMCA() {
		return userIdMCA;
	}

	public void setUserIdMCA(String userIdMCA) {
		this.userIdMCA = userIdMCA;
	}
}
