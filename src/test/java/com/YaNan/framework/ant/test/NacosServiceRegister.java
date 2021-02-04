package com.YaNan.framework.ant.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.YaNan.test.ant.AntService1;
import com.alibaba.nacos.api.exception.NacosException;
import com.yanan.framework.ant.AntContext;
import com.yanan.framework.ant.AntFactory;
import com.yanan.framework.ant.ProertiesWrapper;
import com.yanan.framework.ant.handler.AntMeessageSerialHandler;
import com.yanan.framework.ant.interfaces.AntService;
import com.yanan.framework.ant.nacos.AntNacosRuntime;
import com.yanan.framework.ant.protocol.ant.command.DefaultAntClientServiceImpl;
import com.yanan.framework.plugin.PlugsFactory;

public class NacosServiceRegister {
	
	
	public static void main(String[] args) throws NacosException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InterruptedException, IOException {
		PlugsFactory.getInstance().addScanPath(NacosServiceRegister.class);
		PlugsFactory.init("classpath:plugin.yc");
		AntService1 service = PlugsFactory.getPluginsInstance(AntService1.class);
		System.out.println(service.add(1, 3));
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
