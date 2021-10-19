package com.YaNan.frame.ant.test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import com.YaNan.test.ant.Provider;
import com.yanan.framework.a.nacos.NacosConfigureFactory;
import com.yanan.framework.a.nacos.NacosInstance;
import com.yanan.framework.ant.core.MessageChannel;
import com.yanan.framework.ant.core.cluster.ChannelManager;
import com.yanan.framework.ant.core.server.ServerMessageChannel;
import com.yanan.framework.ant.dispatcher.ChannelDispatcher;
import com.yanan.framework.ant.proxy.Callback;
import com.yanan.framework.ant.proxy.Invokers;
import com.yanan.framework.plugin.Environment;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.decoder.StandScanResource;
import com.yanan.framework.resource.adapter.Config2PropertiesAdapter;
import com.yanan.utils.reflect.TypeToken;
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
//        System.setProperty("cglib.debugLocation", "C:\\cglib");
//        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        PlugsFactory.init(ResourceManager.getResource("classpath:plugin.yc"), 
        		ResourceManager.getResource("classpath:Ant2.yc"), 
        		new StandScanResource(ResourceManager.getClassPath(MessageChannel.class)[0] + "**"), 
        		new StandScanResource(ResourceManager.getClassPath(NacosInstance.class)[0] + "**"), 
        		new StandScanResource(ResourceManager.getClassPath(Config2PropertiesAdapter.class)[0] + "com.yanan.fram**") ,
        new StandScanResource(ResourceManager.getClassPath(DiscoveryServiceServerTest.class)[0] + "**") );
        
        ChannelManager<Object> channelManager = PlugsFactory.getPluginsInstance(ChannelManager.class);
        
        ServerMessageChannel<String> client = PlugsFactory.getPluginsInstance(ServerMessageChannel.class);
        
        channelManager.start(client);
//        Thread.sleep(10000l);
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
		
		
		result = channelDispatcher.request("defaultName", invokers);
		System.out.println("执行结果2:"+result);
		Callback<Integer> callback =Callback.newCallback(invokers);
		callback.on((res,dispatcher)->{
			System.err.println("异步结果:"+res);
			System.err.println("调配器结果:"+dispatcher);
		}, (err,dispatcher)->{
			System.err.println("异步错误:"+err);
			System.err.println("调配器结果:"+dispatcher);
		});
		channelDispatcher.requestAsync("defaultName", invokers,callback );
		System.err.println("调用结束");
		
		method = ClassHelper.getClassHelper(Provider.class).getMethod("parseString",new TypeToken<byte[]>(){}.getTypeClass());
		
		invokers.setInvokeMethod(method);
		invokers.setInvokeParmeters("hello world".getBytes());
		result = channelDispatcher.request("defaultName", invokers);
		System.out.println("执行结果:"+result);
		long now = System.currentTimeMillis();
		for(int i = 0;i<10000;i++) {
			result = channelDispatcher.request("defaultName", invokers);
//			System.out.println("执行结果:"+result);
		}
		System.out.println("执行耗时:"+(System.currentTimeMillis()-now)+"ms");
//        final MessageChannel<?> messageChannel = (MessageChannel<?>)server.getChannel("default");
//		System.out.println(messageChannel);
	}
}
