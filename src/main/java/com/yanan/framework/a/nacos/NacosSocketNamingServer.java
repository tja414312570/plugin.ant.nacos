package com.yanan.framework.a.nacos;

import com.yanan.framework.plugin.annotations.*;
import com.yanan.framework.ant.channel.socket.server.*;
import com.yanan.framework.ant.core.cluster.ChannelNamingServer;
import com.yanan.framework.ant.core.server.*;
import com.yanan.framework.plugin.*;

@Register(attribute="SocketServerMessageChannel_NacosChannelManager")
public class NacosSocketNamingServer implements ChannelNamingServer<NacosInstance>
{
    public NacosInstance getServerName(final ServerMessageChannel<?> messageChannel) {
        System.err.println("=============================");
        System.err.println(messageChannel.getClass());
        final SocketServerMessageChannel<?> proxy = (SocketServerMessageChannel<?>)PlugsFactory.getPluginsHandler((Object)messageChannel).getProxyObject();
        System.err.println(proxy.getClass());
        final NacosInstance nacosInstance = new NacosInstance();
        nacosInstance.setHost("127.0.0.1");
        nacosInstance.setGroup("default");
        nacosInstance.setName("defaultName");
        nacosInstance.setPort(proxy.getPort());
        return nacosInstance;
    }
}
