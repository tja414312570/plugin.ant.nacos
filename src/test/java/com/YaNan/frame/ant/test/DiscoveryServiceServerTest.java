package com.YaNan.frame.ant.test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import com.YaNan.test.ant.Provider;
import com.yanan.framework.a.core.MessageChannel;
import com.yanan.framework.a.core.cluster.ChannelManager;
import com.yanan.framework.a.core.server.ServerMessageChannel;
import com.yanan.framework.a.dispatcher.ChannelDispatcher;
import com.yanan.framework.a.nacos.NacosConfigureFactory;
import com.yanan.framework.a.nacos.NacosInstance;
import com.yanan.framework.a.proxy.Invokers;
import com.yanan.framework.plugin.Environment;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.decoder.StandScanResource;
import com.yanan.utils.reflect.cache.ClassHelper;
import com.yanan.utils.resource.ResourceManager;

public class DiscoveryServiceServerTest
{
    @SuppressWarnings("unchecked")
	public static void main(final String[] args) throws InterruptedException, IOException {
    	System.out.println(ResourceManager.getClassPath(MessageChannel.class)[0] + "**");
    	Environment.getEnviroment().registEventListener(PlugsFactory.getInstance().getEventSource(),event->{
//    		if( ((PluginEvent)event).getEventType() == EventType.add_registerDefinition)
//			System.err.println(((RegisterDefinition)((PluginEvent)event).getEventContent()).getRegisterClass());
		});
//        System.setProperty("cglib.debugLocation", "D:\\cglib");
//        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        PlugsFactory.init(ResourceManager.getResource("classpath:plugin.yc"), 
        		new StandScanResource(ResourceManager.getClassPath(MessageChannel.class)[0] + "**"), 
        		new StandScanResource(ResourceManager.getClassPath(NacosInstance.class)[0] + "**"), 
        new StandScanResource(ResourceManager.getClassPath(DiscoveryServiceServerTest.class)[0] + "**") );
        
        Properties properties = NacosConfigureFactory.build("classpath:ant.yc");
        
        ChannelManager<Object> channelManager = PlugsFactory.getPluginsInstance(ChannelManager.class,properties);
        
        ServerMessageChannel<String> client = PlugsFactory.getPluginsInstance(ServerMessageChannel.class);
        
        channelManager.start(client);
//        channelManager.start();
        ChannelDispatcher channelDispatcher = PlugsFactory.getPluginsInstance(ChannelDispatcher.class);
		System.err.println(channelDispatcher);
		channelDispatcher.bind(channelManager);
		
		
//		Invoker invoker = PlugsFactory.getPluginsInstance(Invoker.class);
//		
//		channelDispatcher.bind(invoker);
		
		
		Invokers invokers = new Invokers();
		invokers.setInvokeClass(Provider.class);
		Method method = ClassHelper.getClassHelper(Provider.class).getMethod("add", int.class,int.class);
		invokers.setInvokeMethod(method);
		invokers.setInvokeParmeters(1,2);
		NacosInstance nacosInstance = new NacosInstance();
		nacosInstance.setName("defaultName");
		Object result = channelDispatcher.request("defaultName", invokers);
		System.out.println("执行结果:"+result);
		System.out.println("服务列表:"+channelManager.getChannelList("defaultName"));
//        final MessageChannel<?> messageChannel = (MessageChannel<?>)server.getChannel("default");
//		System.out.println(messageChannel);
	}
}
