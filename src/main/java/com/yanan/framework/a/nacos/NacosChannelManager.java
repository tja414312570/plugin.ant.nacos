package com.yanan.framework.a.nacos;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.client.naming.net.NamingProxy;
import com.yanan.framework.a.core.MessageChannel;
import com.yanan.framework.a.core.cluster.AbstractChannelManager;
import com.yanan.framework.a.core.cluster.ChannelManager;
import com.yanan.framework.a.core.server.ServerMessageChannel;
import com.yanan.framework.plugin.annotations.Register;
import com.yanan.framework.plugin.annotations.Service;
import com.yanan.utils.asserts.Assert;
import com.yanan.utils.reflect.AppClassLoader;
import com.yanan.utils.reflect.cache.ClassHelper;

@Register
public class NacosChannelManager extends AbstractChannelManager<NacosInstance> implements ChannelManager<NacosInstance>
{
	@Service
	private Logger logger;
	private NamingService namaingService;
	private Properties properties;
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

	public <T> MessageChannel<T> getChannel(final NacosInstance name) {
        return null;
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
    
    public <T> List<MessageChannel<T>> getChannelList(final NacosInstance name) {
        return null;
    }
    
    public <T> Map<String, List<MessageChannel<T>>> getAllChannel() {
        return null;
    }
    
    public void start() {
    	logger.debug("Ant Nacos servcie discovery!");
		try {
			Assert.isNotNull(properties,"没有配置信息");
			logger.debug("Ant Nacos servcie config "+properties);
			//创建命名服务
			namaingService = NamingFactory.createNamingService(properties);
			//设置端口
			Field serverProxyField = ClassHelper.getClassHelper(NacosNamingService.class).getDeclaredField("serverProxy");
			serverProxyField.setAccessible(true);
			NamingProxy serverProxy = (NamingProxy) serverProxyField.get(namaingService);
			serverProxyField.setAccessible(false);
			AppClassLoader loader = new AppClassLoader(serverProxy);
			loader.set("serverPort", Integer.parseInt(properties.getProperty("port")));
			//添加AntDiscoverService
		} catch (NacosException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
			throw new RuntimeException("failed to init nacos server!",e);
		}
    }
    
    public void close() {
    }
}
