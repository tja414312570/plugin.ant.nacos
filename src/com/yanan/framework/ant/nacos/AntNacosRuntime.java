package com.yanan.framework.ant.nacos;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.client.naming.net.NamingProxy;
import com.yanan.framework.ant.handler.AntServiceInstance;
import com.yanan.framework.ant.interfaces.AntDiscoveryService;
import com.yanan.framework.ant.model.AntProvider;
import com.yanan.framework.ant.model.AntProviderSummary;
import com.yanan.framework.ant.service.AntRuntimeService;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.utils.reflect.AppClassLoader;
import com.yanan.utils.reflect.cache.ClassHelper;

public class AntNacosRuntime {
	private NamingService namaingService;
	private Properties properties;
	private AntRuntimeService runtimeService;
	private AntDiscoveryService nacosDiscovery;
	private static Logger logger = LoggerFactory.getLogger(AntNacosRuntime.class);
	List<String> eventList = new ArrayList<String>(16);
	public AntNacosRuntime(String path) {
		logger.debug("Ant Nacos servcie discovery!");
		try {
			properties = AntNacosConfigureFactory.build(path);
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
			PlugsFactory.getInstance().addDefinition(AntNacosDiscovery.class);
			nacosDiscovery = PlugsFactory.getPluginsInstance(AntDiscoveryService.class);
			((AntNacosDiscovery)nacosDiscovery).setNacosRuntime(this);
		} catch (NacosException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException | IOException e) {
			throw new RuntimeException("failed to init nacos server!",e);
		}
	}
	public NamingService getNamaingService() {
		return namaingService;
	}
	public void register(AntProvider antProvider) throws NacosException {
		namaingService.registerInstance(antProvider.getName(),
				antProvider.getHost(), antProvider.getPort(), antProvider.getName());
	}
	public List<Instance> getAllInstances(String name) throws NacosException {
		trySubscribeService(name);
		return namaingService.getAllInstances(name);
	}
	private void trySubscribeService(String name) {
		if(eventList.contains(name))
			return;
		eventList.add(name);
		logger.debug("Ant Nacos subscribe sercie ["+name+"]");
		//设置事件监听
		try {
			namaingService.subscribe(name, event->{
				if(event == null || ((NamingEvent)event).getInstances() == null ||  ((NamingEvent)event).getInstances().isEmpty())
					return;
				List<Instance> instanceList = ((NamingEvent)event).getInstances();
				logger.debug("Ant Nacos Event ["+name+"] instance "+instanceList);
				//检查实例
				try {
					AntServiceInstance handler = runtimeService.getServiceProviderMap().get(name);
					if(handler == null) {
						logger.debug("Ant Nacos add service "+name);
						runtimeService.addServiceFromDiscoveryService(name);
						return;
					}
					AntProviderSummary summary = handler.getAttribute(AntProviderSummary.class);
					if(summary == null)
						return;
					for(Instance instance : instanceList) {
						if(instance.getIp().equals(summary.getHost()) 
								&& instance.getPort() == summary.getPort()) {
							return;
						}
					}
					logger.debug("Ant Nacos recovery servcie ["+name+"]");
					runtimeService.tryRecoveryServiceAndNotifyDiscoveryService(handler);
				}catch(Throwable e) {
					e.printStackTrace();
				}
			});
		} catch (NacosException e) {
			e.printStackTrace();
		}
	}
	public AntRuntimeService getRuntimeService() {
		return runtimeService;
	}
	void setRuntimeService(AntRuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}
	public void deregisterInstance(AntProviderSummary providerSummary) throws NacosException {
		logger.debug("Ant Nacos delete servcie ["+providerSummary+"]");
		namaingService.deregisterInstance(providerSummary.getName(),providerSummary.getHost(), providerSummary.getPort());
	}
	public Instance getInstance(String name) throws NacosException {
		trySubscribeService(name);
		logger.debug("Ant Nacos query servcie ["+name+"]");
		return namaingService.selectOneHealthyInstance(name);
	}
}
