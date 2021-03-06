package com.YaNan.frame.ant.test;

import java.io.IOException;
import java.util.Properties;

import com.yanan.framework.a.channel.socket.server.MessageChannelCreateListener;
import com.yanan.framework.a.core.MessageChannel;
import com.yanan.framework.a.core.cluster.ChannelManager;
import com.yanan.framework.a.core.server.ServerMessageChannel;
import com.yanan.framework.a.dispatcher.ChannelDispatcher;
import com.yanan.framework.a.nacos.NacosConfigureFactory;
import com.yanan.framework.a.proxy.Invoker;
import com.yanan.framework.ant.nacos.AntNacosConfigureFactory;
import com.yanan.framework.plugin.Environment;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.decoder.StandScanResource;
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
        new StandScanResource(ResourceManager.getClassPath(AntNacosConfigureFactory.class)[0] + "**"), 
        new StandScanResource(ResourceManager.getClassPath(DiscoveryServiceServerTest.class)[0] + "**") );
        
        Properties properties = NacosConfigureFactory.build("classpath:ant.yc");
        
        ChannelManager<?> server = PlugsFactory.getPluginsInstance(ChannelManager.class,properties);
        
        ServerMessageChannel<String> client = PlugsFactory.getPluginsInstance(ServerMessageChannel.class);
        
        server.start(client);
        ChannelDispatcher<Object> channelDispatcher = PlugsFactory.getPluginsInstance(ChannelDispatcher.class);
		System.err.println(channelDispatcher);
		channelDispatcher.bind(server);
		
		Invoker invoker = PlugsFactory.getPluginsInstance(Invoker.class);
		
		channelDispatcher.bind(invoker);
//        final MessageChannel<?> messageChannel = (MessageChannel<?>)server.getChannel("default");
//		System.out.println(messageChannel);
	}
}
