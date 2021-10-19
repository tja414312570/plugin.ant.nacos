package com.yanan.framework.a.nacos;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.yanan.framework.ant.core.MessageChannel;
import com.yanan.framework.ant.core.cluster.AbstractChannelManager;
import com.yanan.framework.ant.core.cluster.ChannelCreator;
import com.yanan.framework.ant.core.cluster.ChannelManager;
import com.yanan.framework.ant.core.server.ServerMessageChannel;
import com.yanan.framework.plugin.annotations.Register;
import com.yanan.framework.plugin.annotations.Service;
import com.yanan.framework.plugin.autowired.enviroment.Variable;
import com.yanan.utils.asserts.Assert;

@Register
public class NacosChannelManager extends AbstractChannelManager<NacosInstance,NacosInstance> implements ChannelManager<NacosInstance>
{
	@Service
	private Logger logger;
	private NamingService namaingService;
	@Variable("nacos")
	private Properties properties;
	
	@Service(attribute = "com.alibaba.nacos.api.naming.pojo.Instance")
	private ChannelCreator<?, Instance> channelCreator;
    public Logger getLogger() {
		return logger;
	}
    public NacosChannelManager() {};
    public NacosChannelManager(Properties properties) {
    	this.properties = properties;
    }
	public NamingService getNamaingService() {
		return namaingService;
	}

	public Properties getProperties() {
		return properties;
	}
	
    public void registerChannel(final NacosInstance instance, final ServerMessageChannel<?> channel) {
        System.err.println("注册通道:" + instance + "--->" + channel);
        try {
			namaingService.registerInstance(instance.getName(),
					instance.getHost(), instance.getPort(), instance.getName());
		} catch (NacosException e) {
			e.printStackTrace();
			logger.error("注册通道失败["+instance+"]["+channel+"]",e);
		}
    }
    
    public void start() {
    	logger.debug("Ant Nacos servcie discovery!");
		try {
			Assert.isNotNull(properties,"没有配置信息");
			logger.debug("Ant Nacos servcie config "+properties);
			//创建命名服务
			namaingService = NamingFactory.createNamingService(properties);
			//添加AntDiscoverService
		} catch (NacosException | IllegalArgumentException | SecurityException e) {
			throw new RuntimeException("failed to init nacos server!",e);
		}
    }
    
    public void close() {
    }
	@Override
	public <T> Map<String, List<MessageChannel<T>>> getAllChannel() {
		return null;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected <T> List<MessageChannel<T>> getChannelInstanceList(NacosInstance nacosInstance) {
		List<Instance> instances;
		try {
			instances = namaingService.getAllInstances(nacosInstance.getName());
		} catch (NacosException e) {
		     throw new NacosChannelException("exception on get nacos instance",e);
		}
		List<MessageChannel<T>> messageChannels = new ArrayList<>();
		instances.forEach(instance ->{
			MessageChannel<T> messageChannel = (MessageChannel<T>) channelCreator.creatorChannel(instance);
			messageChannels.add(messageChannel);
		});
		return messageChannels;
	}
	@SuppressWarnings("unchecked")
	@Override
	public <T> MessageChannel<T> getChannelInstance(NacosInstance name) {
		logger.debug("通道实例:"+name);
		Instance instance;
		try {
			instance = namaingService.selectOneHealthyInstance(name.getName());
			logger.debug("实例信息:"+instance);
			System.out.println(instance);
			MessageChannel<T> messageChannel =(MessageChannel<T>) channelCreator.creatorChannel(instance);
			logger.debug("得到通道:"+messageChannel);
			messageChannel.open();
			return messageChannel;
		} catch (NacosException e) {
			 throw new NacosChannelException("exception on get nacos instance "+name,e);
		}
	}
}
