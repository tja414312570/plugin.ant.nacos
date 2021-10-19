package com.YaNan.frame.ant.test;

import java.lang.reflect.Method;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

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

public class Client {
	public static void main(String[] args) throws InterruptedException {
		PlugsFactory.init(ResourceManager.getResource("classpath:plugin.yc"), 
        		ResourceManager.getResource("classpath:Ant2.yc"), 
        		new StandScanResource(ResourceManager.getClassPath(MessageChannel.class)[0] + "**"), 
        		new StandScanResource(ResourceManager.getClassPath(NacosInstance.class)[0] + "**"), 
        		new StandScanResource(ResourceManager.getClassPath(Config2PropertiesAdapter.class)[0] + "com.yanan.fram**") ,
        new StandScanResource(ResourceManager.getClassPath(DiscoveryServiceServerTest.class)[0] + "**") );
        
        ChannelManager<Object> channelManager = PlugsFactory.getPluginsInstance(ChannelManager.class);
        
        
        channelManager.start();
        ChannelDispatcher channelDispatcher = PlugsFactory.getPluginsInstance(ChannelDispatcher.class);
		System.err.println(channelDispatcher);
		channelDispatcher.bind(channelManager);
		
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
		invokers.setInvokeMethod(method);
		invokers.setInvokeParmeters(1,2);
		result = channelDispatcher.request("defaultName", invokers);
		System.out.println("执行结果:"+result);
		long now = System.currentTimeMillis();
		long counts = 100000;
		for(int i = 0;i<counts;i++) {
			result = channelDispatcher.request("defaultName", invokers);
//			System.out.println("执行结果:"+i+"="+result);
		}
		long times = (System.currentTimeMillis()-now);
		System.out.println("串行执行耗时:"+times+"ms,吞吐率:"+(counts/times)+" 个/ms，平均耗时:"+(times/counts)+"ms");
		CountDownLatch lotch = new CountDownLatch((int) counts);
		now = System.currentTimeMillis();
		callback =Callback.newCallback(invokers);
		callback.on((res,dispatcher)->{
			lotch.countDown();
//			System.err.println(lotch.getCount()+"--->"+atomicInteger.get()+"-->"+res);
		}, (err,dispatcher)->{
			System.err.println("异步错误:"+err);
			System.err.println("调配器结果:"+dispatcher);
		});
		for(int i = 0;i<counts;i++) {
			invokers.setInvokeParmeters(1,2);
			channelDispatcher.requestAsync("defaultName", invokers,callback );
		}
		lotch.await();
		times = (System.currentTimeMillis()-now);
		System.out.println("并行执行耗时:"+times+"ms,吞吐率:"+(counts/times)+" 个/ms，平均耗时:"+(times/counts)+"ms");
//        final MessageChannel<?> messageChannel = (MessageChannel<?>)server.getChannel("default");
//		System.out.println(messageChannel);
		now = System.currentTimeMillis();
		Provider provider = PlugsFactory.getPluginsInstance(Provider.class);
		for(int i = 0;i<counts;i++) {
			provider.add(1, 2);
		}
		times = (System.currentTimeMillis()-now);
		System.out.println("直接执行耗时:"+times+"ms,吞吐率:"+(counts/times)+" 个/ms，平均耗时:"+(times/counts)+"ms");
	}
}
