package com.yanan.framework.a.nacos;

import com.yanan.framework.plugin.annotations.*;
import com.yanan.framework.a.core.server.*;
import com.yanan.framework.a.channel.socket.server.*;
import com.yanan.framework.plugin.*;

@Register
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
