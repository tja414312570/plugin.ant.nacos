package com.yanan.framework.a.nacos;

import com.alibaba.nacos.api.naming.pojo.Instance;
import com.yanan.framework.a.channel.socket.SocketMessageChannel;
import com.yanan.framework.a.core.MessageChannel;
import com.yanan.framework.a.core.cluster.ChannelCreator;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.annotations.Register;
import com.yanan.utils.reflect.TypeToken;

@Register(attribute = "Instance")
public class NacosSocketChannelCreator<T> implements ChannelCreator<T, Instance>{

	@Override
	public MessageChannel<T> creatorChannel(Instance info) {
		MessageChannel<T> messageChannel = PlugsFactory.getPluginsInstance(new TypeToken<MessageChannel<T>>(){}.getTypeClass());
		SocketMessageChannel<T> socketMessageChannel = PlugsFactory.getPluginsHandler(messageChannel).getProxyObject();
		socketMessageChannel.setPort(info.getPort());
		socketMessageChannel.setHost(info.getIp());
		return socketMessageChannel;
	}

}
