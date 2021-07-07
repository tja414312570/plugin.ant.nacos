package com.YaNan.frame.ant.test;

import com.yanan.framework.plugin.*;
import com.yanan.utils.resource.*;
import com.yanan.framework.ant.*;
import com.yanan.framework.ant.nacos.AntNacosConfigureFactory;
import com.yanan.framework.plugin.decoder.*;
import com.yanan.framework.a.core.cluster.*;
import com.yanan.framework.a.core.server.ServerMessageChannel;
import com.yanan.framework.a.nacos.NacosConfigureFactory;

import java.io.IOException;
import java.util.Properties;

import com.yanan.framework.a.core.*;
import com.yanan.framework.plugin.event.*;

public class DiscoveryServiceTest {
	public static void main(final String[] args) throws InterruptedException, IOException {

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
