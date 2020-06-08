package com.YaNan.frame.ant.nacos;

import java.util.List;
import java.util.Properties;

import com.YaNan.frame.ant.interfaces.AntDiscoveryService;
import com.YaNan.frame.ant.model.AntProvider;
import com.YaNan.frame.plugin.PlugsFactory;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;

public class AntNacosRuntime {
	private NamingService namaingService;
	private Properties properties;
	public AntNacosRuntime(String path) {
		properties = AntNacosConfigureFactory.build("classpath:Ant.yc");
		try {
			namaingService = NamingFactory.createNamingService(properties);
			PlugsFactory.getInstance().addPlugs(AntNacosDiscovery.class);
			AntDiscoveryService nacosDiscovery = PlugsFactory.getPlugsInstance(AntDiscoveryService.class);
			System.out.println(nacosDiscovery);
			((AntNacosDiscovery)nacosDiscovery).setNacosRuntime(this);
		} catch (NacosException e) {
			new RuntimeException("failed to init nacos server!",e);
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
}
