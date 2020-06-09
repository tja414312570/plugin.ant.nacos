package com.YaNan.frame.ant.nacos;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;

import com.YaNan.frame.ant.interfaces.AntDiscoveryService;
import com.YaNan.frame.ant.model.AntProvider;
import com.YaNan.frame.ant.service.AntRuntimeService;
import com.YaNan.frame.plugin.PlugsFactory;
import com.YaNan.frame.utils.reflect.cache.ClassHelper;
import com.YaNan.frame.utils.reflect.ClassLoader;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.client.naming.net.NamingProxy;

public class AntNacosRuntime {
	private NamingService namaingService;
	private Properties properties;
	private AntRuntimeService runtimeService;
	public AntNacosRuntime(String path) {
		properties = AntNacosConfigureFactory.build("classpath:Ant.yc");
		try {
			namaingService = NamingFactory.createNamingService(properties);
			Field serverProxyField = ClassHelper.getClassHelper(NacosNamingService.class).getDeclaredField("serverProxy");
			serverProxyField.setAccessible(true);
			NamingProxy serverProxy = (NamingProxy) serverProxyField.get(namaingService);
			serverProxyField.setAccessible(false);
			ClassLoader loader = new ClassLoader(serverProxy);
			loader.set("serverPort", Integer.parseInt(properties.getProperty("port")));
			PlugsFactory.getInstance().addPlugs(AntNacosDiscovery.class);
			AntDiscoveryService nacosDiscovery = PlugsFactory.getPlugsInstance(AntDiscoveryService.class);
			((AntNacosDiscovery)nacosDiscovery).setNacosRuntime(this);
		} catch (NacosException | IllegalArgumentException | IllegalAccessException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
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
		return namaingService.getAllInstances(name);
	}
	public AntRuntimeService getRuntimeService() {
		return runtimeService;
	}
	void setRuntimeService(AntRuntimeService runtimeService) {
		this.runtimeService = runtimeService;
	}
}
