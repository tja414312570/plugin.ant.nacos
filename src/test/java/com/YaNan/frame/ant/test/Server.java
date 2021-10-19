package com.YaNan.frame.ant.test;

import java.lang.reflect.Method;

import com.YaNan.test.ant.Provider;
import com.yanan.framework.a.nacos.NacosInstance;
import com.yanan.framework.ant.core.MessageChannel;
import com.yanan.framework.ant.core.cluster.ChannelManager;
import com.yanan.framework.ant.core.server.ServerMessageChannel;
import com.yanan.framework.ant.dispatcher.ChannelDispatcher;
import com.yanan.framework.ant.proxy.Callback;
import com.yanan.framework.ant.proxy.Invokers;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.decoder.StandScanResource;
import com.yanan.framework.resource.adapter.Config2PropertiesAdapter;
import com.yanan.utils.reflect.TypeToken;
import com.yanan.utils.reflect.cache.ClassHelper;
import com.yanan.utils.resource.ResourceManager;

public class Server {
	public static void main(String[] args) {
		PlugsFactory.init(ResourceManager.getResource("classpath:plugin.yc"), 
        		ResourceManager.getResource("classpath:Ant2.yc"), 
        		new StandScanResource(ResourceManager.getClassPath(MessageChannel.class)[0] + "**"), 
        		new StandScanResource(ResourceManager.getClassPath(NacosInstance.class)[0] + "**"), 
        		new StandScanResource(ResourceManager.getClassPath(Config2PropertiesAdapter.class)[0] + "com.yanan.fram**") ,
        new StandScanResource(ResourceManager.getClassPath(DiscoveryServiceServerTest.class)[0] + "**") );
        
        ChannelManager<Object> channelManager = PlugsFactory.getPluginsInstance(ChannelManager.class);
        
        ServerMessageChannel<String> client = PlugsFactory.getPluginsInstance(ServerMessageChannel.class);
        
        channelManager.start(client);
        ChannelDispatcher channelDispatcher = PlugsFactory.getPluginsInstance(ChannelDispatcher.class);
      	channelDispatcher.bind(channelManager);
	}
}
