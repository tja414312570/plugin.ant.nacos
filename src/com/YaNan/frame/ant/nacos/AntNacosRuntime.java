package com.YaNan.frame.ant.nacos;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.YaNan.frame.ant.handler.AntClientHandler;
import com.YaNan.frame.ant.interfaces.AntDiscoveryService;
import com.YaNan.frame.ant.model.AntMessagePrototype;
import com.YaNan.frame.ant.model.AntProvider;
import com.YaNan.frame.ant.model.AntProviderSummary;
import com.YaNan.frame.ant.service.AntRuntimeService;
import com.YaNan.frame.plugin.PlugsFactory;
import com.YaNan.frame.utils.reflect.cache.ClassHelper;
import com.YaNan.frame.utils.reflect.ClassLoader;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingFactory;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.listener.NamingEvent;
import com.alibaba.nacos.api.naming.pojo.Instance;
import com.alibaba.nacos.client.naming.NacosNamingService;
import com.alibaba.nacos.client.naming.net.NamingProxy;

public class AntNacosRuntime {
	private NamingService namaingService;
	private Properties properties;
	private AntRuntimeService runtimeService;
	private AntDiscoveryService nacosDiscovery;
	List<String> eventList = new ArrayList<String>(16);
	public AntNacosRuntime(String path) {
		properties = AntNacosConfigureFactory.build(path);
		try {
			//创建命名服务
			namaingService = NamingFactory.createNamingService(properties);
			//设置端口
			Field serverProxyField = ClassHelper.getClassHelper(NacosNamingService.class).getDeclaredField("serverProxy");
			serverProxyField.setAccessible(true);
			NamingProxy serverProxy = (NamingProxy) serverProxyField.get(namaingService);
			serverProxyField.setAccessible(false);
			ClassLoader loader = new ClassLoader(serverProxy);
			loader.set("serverPort", Integer.parseInt(properties.getProperty("port")));
			//添加AntDiscoverService
			PlugsFactory.getInstance().addPlugs(AntNacosDiscovery.class);
			nacosDiscovery = PlugsFactory.getPlugsInstance(AntDiscoveryService.class);
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
		trySubscribeService(name);
		return namaingService.getAllInstances(name);
	}
	private void trySubscribeService(String name) {
		if(eventList.contains(name))
			return;
		eventList.add(name);
		//设置事件监听
		try {
			namaingService.subscribe(name, event->{
				System.out.println("事件"+name);
				if(event == null)
					return;
				runtimeService.addServiceFromDiscoveryService(name);
				//获取handler 
				AntClientHandler handler =runtimeService.getClientHandler(name);
				//获取所有的未完成的请求
				List<AntMessagePrototype> list = runtimeService.getMessageQueue().getAllProcessingMessage(name);
				//使用新的Handler继续发送消息
				list.forEach(message->handler.write(message));
				
				System.out.println(((NamingEvent)event).getClusters());
				System.out.println(((NamingEvent)event).getGroupName());
			    System.out.println(((NamingEvent)event).getServiceName());
			    System.out.println(((NamingEvent)event).getInstances());
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
		System.out.println("删除服务:"+providerSummary.getHost());
		namaingService.deregisterInstance(providerSummary.getName(),providerSummary.getHost(), providerSummary.getPort());
	}
}
