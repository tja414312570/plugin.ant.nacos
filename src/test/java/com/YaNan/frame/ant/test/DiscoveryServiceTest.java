package com.YaNan.frame.ant.test;

import java.io.IOException;
import java.util.Properties;

import com.YaNan.test.ant.Request;
import com.yanan.framework.a.core.MessageChannel;
import com.yanan.framework.a.core.cluster.ChannelManager;
import com.yanan.framework.a.dispatcher.ChannelDispatcher;
import com.yanan.framework.a.nacos.NacosConfigureFactory;
import com.yanan.framework.a.proxy.Invoker;
import com.yanan.framework.ant.nacos.AntNacosConfigureFactory;
import com.yanan.framework.plugin.Environment;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.decoder.StandScanResource;
import com.yanan.utils.resource.ResourceManager;

public class DiscoveryServiceTest {
	public static void main(final String[] args) throws InterruptedException, IOException {

		Environment.getEnviroment().registEventListener(PlugsFactory.getInstance().getEventSource(),event->{
//    		if( ((PluginEvent)event).getEventType() == EventType.add_registerDefinition)
//			System.err.println(((RegisterDefinition)((PluginEvent)event).getEventContent()).getRegisterClass());
		});
		
		PlugsFactory.init(ResourceManager.getResource("classpath:plugin.yc"),
				new StandScanResource(ResourceManager.getClassPath(MessageChannel.class)[0] + "**"),
				new StandScanResource(ResourceManager.getClassPath(AntNacosConfigureFactory.class)[0] + "**"),
				new StandScanResource(ResourceManager.getClassPath(DiscoveryServiceServerTest.class)[0] + "**"));

		Properties properties = NacosConfigureFactory.build("classpath:ant.yc");

		ChannelManager<?> server = PlugsFactory.getPluginsInstance(ChannelManager.class, properties);

		server.start();

		ChannelDispatcher<Object> channelDispatcher = PlugsFactory.getPluginsInstance(ChannelDispatcher.class);
		System.err.println(channelDispatcher);
		channelDispatcher.bind(server);
		
		Invoker invoker = PlugsFactory.getPluginsInstance(Invoker.class);
		
		channelDispatcher.bind(invoker);
		
//		channelDispatcher.request("defaultName","hello world");
		
		Request request = PlugsFactory.getPluginsInstance(Request.class);
		System.err.println(request);
		Object result = request.add(10, 20);
		System.err.println(result);
//		MessageChannel<String> messageChannel =server.getChannel("defaultName");
//		System.err.println(messageChannel);
//		messageChannel.transport("Hello");
	}
}
