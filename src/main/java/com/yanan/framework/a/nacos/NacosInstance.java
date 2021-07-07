package com.yanan.framework.a.nacos;

public class NacosInstance
{
    private String group;
    private String name;
    private String host;
    private int port;
    private String clusterName; 
    
    public String getGroup() {
        return this.group;
    }
    
    public void setGroup(final String group) {
        this.group = group;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public String getHost() {
        return this.host;
    }
    
    public void setHost(final String host) {
        this.host = host;
    }
    
    public int getPort() {
        return this.port;
    }
    
    public void setPort(final int port) {
        this.port = port;
    }
    
    @Override
	public String toString() {
		return "NacosInstance [group=" + group + ", name=" + name + ", host=" + host + ", port=" + port
				+ ", clusterName=" + clusterName + "]";
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}
}
