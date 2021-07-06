package com.YaNan.frame.ant.test;

import com.yanan.framework.plugin.*;
import com.yanan.utils.resource.*;
import com.yanan.framework.ant.*;
import com.yanan.framework.plugin.decoder.*;
import com.yanan.framework.a.core.cluster.*;
import com.yanan.framework.a.core.*;
import com.yanan.framework.plugin.event.*;

public class DiscoveryServiceTest
{
    public static void main(final String[] args) throws InterruptedException {
        Environment.getEnviroment().registEventListener((InterestedEventSource)PlugsFactory.getInstance().getEventSource(), event -> {});
        PlugsFactory.init(new Resource[] { ResourceManager.getResource("classpath:plugin.yc"), (Resource)new StandScanResource(String.valueOf(ResourceManager.getClassPath(new Class[] { AntContext.class })[0]) + "**"), (Resource)new StandScanResource(String.valueOf(ResourceManager.getClassPath(new Class[] { DiscoveryServiceTest.class })[0]) + "**") });
        final ChannelManager<String> client = (ChannelManager<String>)PlugsFactory.getPluginsInstance((Class)ChannelManager.class, new Object[0]);
        client.start();
        final MessageChannel<String> messageChannel = (MessageChannel<String>)client.getChannel((Object)"xxxxxxx");
        System.out.println(messageChannel);
    }
}
