package com.YaNan.framework.ant.test;

import com.yanan.framework.plugin.*;
import com.yanan.framework.plugin.annotations.Register;
import com.yanan.framework.plugin.annotations.Service;
import com.yanan.framework.plugin.autowired.enviroment.ResourceAdapter;
import com.yanan.framework.plugin.autowired.enviroment.Variable;
import com.yanan.framework.plugin.decoder.StandScanResource;
import com.yanan.framework.plugin.matcher.RegisterMatcher;
import com.yanan.framework.resource.adapter.Config2PropertiesAdapter;
import com.yanan.framework.resource.adapter.DefaultResourceAdapter;
import com.yanan.utils.beans.xml.Value;
import com.yanan.utils.reflect.ReflectUtils;
import com.yanan.utils.resource.ResourceManager;
import com.YaNan.frame.ant.test.DiscoveryServiceServerTest;
import com.yanan.framework.a.nacos.NacosInstance;
import com.yanan.framework.ant.core.MessageChannel;
import com.alibaba.nacos.api.exception.*;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.impl.SimpleConfigObject;

import java.lang.reflect.*;
import java.util.Map;
import java.util.Properties;

import javax.annotation.PostConstruct;

import java.io.*;

@Register
public class NacosServiceRegister
{
	@Service
	private ResourceAdapter<Config,Properties> resourceAdapter;
	
	@Service(attribute = "com.yanan.framework.plugin.autowired.enviroment.ResourceAdapter")
	private RegisterMatcher register;
	
	@Variable("nacos")
	private Properties propetries;
	
	@Variable("nacos")
	private Map server;
	@PostConstruct
	public void init() {
		System.err.println("初始化");
		System.err.println(resourceAdapter);
		System.err.println(propetries);
		System.err.println(server);
		System.err.println("");
	}
	
    public static void main(final String[] args) throws NacosException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InterruptedException, IOException, NoSuchFieldException {
    	PlugsFactory.init(ResourceManager.getResource("classpath:plugin.yc"), 
        		ResourceManager.getResource("classpath:Ant2.yc"), 
        		new StandScanResource(ResourceManager.getClassPath(MessageChannel.class)[0] + "**"), 
        		new StandScanResource(ResourceManager.getClassPath(NacosInstance.class)[0] + "**"), 
        new StandScanResource(ResourceManager.getClassPath(DiscoveryServiceServerTest.class)[0] + "**") ,
        new StandScanResource(ResourceManager.getClassPath(Config2PropertiesAdapter.class)[0] + "com.yanan.frame**") );
    	
    		
    	PlugsFactory.getPluginsInstance(NacosServiceRegister.class);
    	
    	SimpleConfigObject config = (SimpleConfigObject) Environment.getEnviroment().getConfigValue("nacos");
    	config.entrySet().forEach(item->{
    		System.err.println(item.getKey()+"-->"+item.getValue()+item.getValue().valueType());
    	});
    	Field field = NacosServiceRegister.class.getDeclaredField("resourceAdapter");
    	Class<?>[] clzz = ReflectUtils.getParameterizedType(field);
    	System.out.println(clzz);
    	Parameter param;
    	System.err.println();
    	DefaultResourceAdapter resourceAdapter = new DefaultResourceAdapter<>();
    	Properties properties = (Properties) resourceAdapter.parse(config);
    	
    	System.err.println(properties);
    	//        ChannelManager channelManager = PlugsFactory.getPluginsInstance(ChannelManager.class);
    }
    
}
