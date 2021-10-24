package com.yanan.framework.a.nacos;

import com.yanan.framework.plugin.annotations.*;
import com.yanan.framework.ant.channel.socket.server.*;
import com.yanan.framework.ant.core.cluster.ChannelNamingServer;

@Register(attribute="SocketServerMessageChannel_NacosChannelManager")
public class NacosSocketNamingServer implements ChannelNamingServer<NacosInstance,SocketServerMessageChannel<?>>
{
    public NacosInstance getServerName(final SocketServerMessageChannel<?> messageChannel) {
        System.err.println("=============================");
        System.err.println(messageChannel.getClass());
        System.err.println(messageChannel.getClass());
        final NacosInstance nacosInstance = new NacosInstance();
        nacosInstance.setHost("192.168.0.104");
        nacosInstance.setGroup("default");
        nacosInstance.setName("defaultName");
        nacosInstance.setPort(messageChannel.getServerAddr().getPort());
        return nacosInstance;
    }
}
