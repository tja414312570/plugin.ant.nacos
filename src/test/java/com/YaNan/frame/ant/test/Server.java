package com.yanan.frame.ant.test;

import java.net.InetAddress;
import java.util.List;

import com.yanan.framework.ant.core.cluster.ChannelManager;
import com.yanan.framework.ant.core.server.ServerMessageChannel;
import com.yanan.framework.ant.dispatcher.ChannelDispatcher;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.decoder.StandScanResource;
import com.yanan.utils.resource.ResourceManager;
import com.yanan.utils.string.PathMatcher;
import com.yanan.utils.string.PathMatcher.Token;

public class Server {
	public static void main(String[] args) {
//		
//		List<Token> token = PathMatcher.getPathMatcher("socket:{ant.server.addr}").getTokens();
//		System.err.println(PathMatcher.match("socket:{ant.server.addr}", "socket:xlxx").getTokens());
//		System.err.println(token);
		PlugsFactory.init(
        		ResourceManager.getResource("classpath:Ant2.yc"), 
        		new StandScanResource( "classpath*:**")
        		);
        
        ChannelManager<Object> channelManager = PlugsFactory.getPluginsInstance(ChannelManager.class);
        
        ServerMessageChannel<String> client = PlugsFactory.getPluginsInstance(ServerMessageChannel.class);
        
        channelManager.start(client);
        ChannelDispatcher channelDispatcher = PlugsFactory.getPluginsInstance(ChannelDispatcher.class);
      	channelDispatcher.bind(channelManager);
	}
}
