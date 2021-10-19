package com.yanan.framework.a.nacos;

import com.yanan.framework.ant.core.cluster.ChannelInstanceNameServer;
import com.yanan.framework.plugin.annotations.Register;

@Register(attribute="String_NacosChannelManager")
public class NacosSocketInstanceNameServer implements ChannelInstanceNameServer<NacosInstance, String>{

	@Override
	public NacosInstance getInstanceName(String name) {
		NacosInstance instance = new NacosInstance();
		instance.setName(name);
		return instance;
	}

}
