package com.YaNan.frame.ant.test;

import java.lang.reflect.InvocationTargetException;

import com.alibaba.nacos.api.config.ConfigFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import com.yanan.frame.ant.AntContext;
import com.yanan.frame.ant.AntFactory;
import com.yanan.frame.ant.handler.AntMeessageSerialHandler;
import com.yanan.frame.ant.nacos.AntNacosRuntime;
import com.yanan.frame.ant.protocol.ant.command.DefaultAntClientServiceImpl;
import com.yanan.frame.plugin.PlugsFactory;

public class NacosServiceRegister {
	
	
	public static void main(String[] args) throws NacosException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InterruptedException {
		PlugsFactory.getInstance().addDefinition(AntMeessageSerialHandler.class);//消息序列化
		PlugsFactory.getInstance().addDefinition(DefaultAntClientServiceImpl.class);
//		PlugsFactory.getInstance().addPlugs(AntNacosDiscovery.class);
		AntNacosRuntime antNacosRuntime = new AntNacosRuntime("classpath:Ant.yc");
		//		registerInstance：注册实例。
//		deregisterInstance：注销实例。
//		getAllInstances：获取某一服务的所有实例。
//		selectInstances：获取某一服务健康或不健康的实例。
//		selectOneHealthyInstance：根据权重选择一个健康的实例。
//		getServerStatus：检测服务端健康状态。
//		subscribe：注册对某个服务的监听。
//		unsubscribe：注销对某个服务的监听。
//		getSubscribeServices：获取被监听的服务。
//		getServicesOfServer：获取命名空间（namespace)下的所有服务名。【注：此方法有个小坑，参数pageNo要从1开始】
		//启动Ant
		AntContext antContext = AntFactory.build("classpath:Ant.yc");
		antContext.start();
		
//		ConfigService configService = ConfigFactory.createConfigService(properties);
		//设置服务中心
//		Properties properties = new Properties();
//		properties.setProperty("serverAddr", "127.0.0.1");
//		properties.setProperty("namespace", "test");
//		ParamUtil
		
//		ClassLoader loader = new ClassLoader(naming);
//		 private NamingProxy serverProxy;
//		 NamingProxy serverProxy = (NamingProxy) loader.get("serverProxy");
//		 loader = new ClassLoader(serverProxy);
//		loader.set("serverPort", "8845");
//		naming.registerInstance("nacos.test.3", "11.11.11.11", 8888, "TEST1");
//
//		naming.registerInstance("nacos.test.3", "2.2.2.2", 9999, "DEFAULT");
//
//		System.out.println(naming.getAllInstances("nacos.test.3"));
//
////		naming.deregisterInstance("nacos.test.3", "2.2.2.2", 9999, "DEFAULT");
//
//		System.out.println(naming.getAllInstances("nacos.test.3"));
//
//		naming.subscribe("nacos.test.3", new EventListener() {
//		    public void onEvent(Event event) {
//		        System.out.println(((NamingEvent)event).getServiceName());
//		        System.out.println(((NamingEvent)event).getInstances());
//		    }
//		});
	}
}
