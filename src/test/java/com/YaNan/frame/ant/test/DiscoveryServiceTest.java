package com.YaNan.frame.ant.test;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.Properties;

import com.yanan.framework.a.core.MessageChannel;
import com.yanan.framework.a.core.cluster.ChannelManager;
import com.yanan.framework.a.nacos.NacosConfigureFactory;
import com.yanan.framework.ant.nacos.AntNacosConfigureFactory;
import com.yanan.framework.plugin.Environment;
import com.yanan.framework.plugin.PluginEvent;
import com.yanan.framework.plugin.PluginEvent.EventType;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.decoder.StandScanResource;
import com.yanan.framework.plugin.definition.RegisterDefinition;
import com.yanan.utils.resource.ResourceManager;

public class DiscoveryServiceTest {
	public static void main(final String[] args) throws InterruptedException, IOException {

		Environment.getEnviroment().registEventListener(PlugsFactory.getInstance().getEventSource(),event->{
    		if( ((PluginEvent)event).getEventType() == EventType.add_registerDefinition)
			System.err.println(((RegisterDefinition)((PluginEvent)event).getEventContent()).getRegisterClass());
		});
		
		PlugsFactory.init(ResourceManager.getResource("classpath:plugin.yc"),
				new StandScanResource(ResourceManager.getClassPath(MessageChannel.class)[0] + "**"),
				new StandScanResource(ResourceManager.getClassPath(AntNacosConfigureFactory.class)[0] + "**"),
				new StandScanResource(ResourceManager.getClassPath(DiscoveryServiceServerTest.class)[0] + "**"));

		Properties properties = NacosConfigureFactory.build("classpath:ant.yc");

		ChannelManager<?> server = PlugsFactory.getPluginsInstance(ChannelManager.class, properties);

		server.start();

		MessageChannel<String> messageChannel =server.getChannel("defaultName");
		System.err.println(messageChannel);
		messageChannel.transport("Hello");
	}
}
