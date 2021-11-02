package com.yanan.frame.ant.test;

import java.lang.reflect.Method;

import com.yanan.framework.a.nacos.NacosInstance;
import com.yanan.framework.ant.core.cluster.ChannelManager;
import com.yanan.framework.ant.dispatcher.ChannelDispatcher;
import com.yanan.framework.ant.proxy.Invokers;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.decoder.StandScanResource;
import com.yanan.test.ant.Provider;
import com.yanan.utils.reflect.cache.ClassHelper;
import com.yanan.utils.resource.ResourceManager;

public class ClientNotify {
	public static void main(String[] args) throws InterruptedException {
		PlugsFactory.init(
        		ResourceManager.getResource("classpath:Ant2.yc"), 
        		new StandScanResource( "classpath*:**")
        		);
        
        ChannelManager<Object> channelManager = PlugsFactory.getPluginsInstance(ChannelManager.class);
        
        
        channelManager.start();
        ChannelDispatcher channelDispatcher = PlugsFactory.getPluginsInstance(ChannelDispatcher.class);
		System.err.println(channelDispatcher);
		channelDispatcher.bind(channelManager);
		
		Invokers invokers = new Invokers();
		invokers.setInvokeClass(Provider.class);
		Method method = ClassHelper.getClassHelper(Provider.class).getMethod("notify",String.class);
		invokers.setInvokeMethod(method);
		invokers.setInvokeParmeters("hello world");
		NacosInstance nacosInstance = new NacosInstance();
		nacosInstance.setName("defaultName");
//		Object result = channelDispatcher.request("defaultName", invokers);
//		System.out.println("执行结果:"+result);
//		System.out.println("服务列表:"+channelManager.getChannelList("defaultName"));
		 
		channelDispatcher.subscribe("defaultName", invokers,(ctx,message)->{
			System.err.println("通知消息:"+message);
		});
		
	}
}
