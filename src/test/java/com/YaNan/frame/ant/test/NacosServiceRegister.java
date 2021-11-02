package com.yanan.frame.ant.test;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.YaNan.test.ant.Provider;
import com.alibaba.nacos.api.exception.NacosException;
import com.yanan.framework.a.nacos.NacosInstance;
import com.yanan.framework.ant.channel.socket.message.AbstractMessage;
import com.yanan.framework.ant.channel.socket.message.SocktMessageType;
import com.yanan.framework.ant.core.MessageChannel;
import com.yanan.framework.ant.core.MessageSerialization;
import com.yanan.framework.ant.dispatcher.Request;
import com.yanan.framework.ant.proxy.Invokers;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.decoder.StandScanResource;
import com.yanan.framework.resource.adapter.Config2PropertiesAdapter;
import com.yanan.utils.reflect.cache.ClassHelper;
import com.yanan.utils.resource.ResourceManager;

public class NacosServiceRegister
{
    public static void main(final String[] args) throws NacosException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InterruptedException, IOException {
    	 PlugsFactory.init(ResourceManager.getResource("classpath:plugin.yc"), 
         		ResourceManager.getResource("classpath:Ant2.yc"), 
         		new StandScanResource(ResourceManager.getClassPath(MessageChannel.class)[0] + "**"), 
         		new StandScanResource(ResourceManager.getClassPath(NacosInstance.class)[0] + "**"), 
         		new StandScanResource(ResourceManager.getClassPath(Config2PropertiesAdapter.class)[0] + "com.yanan.fram**") ,
         new StandScanResource(ResourceManager.getClassPath(DiscoveryServiceServerTest.class)[0] + "**") );
    	
    	 MessageSerialization messageSerialization = PlugsFactory.getPluginsInstance(MessageSerialization.class);
		Method method = ClassHelper.getClassHelper(Provider.class).getMethod("add", int.class,int.class);
		Invokers invokers = new Invokers();
		invokers.setInvokeClass(Provider.class);
		invokers.setInvokeMethod(method);
		invokers.setInvokeParmeters(1,2);
		Request<Invokers> request = new Request<>();
		request.setInvoker(invokers);
		request.setRID(0);
		request.setTimeout(1000);
		request.setType(0);
		AbstractMessage message = new AbstractMessage();
		message.setId(0);
		message.setMessage(request);
		message.setMessageType(SocktMessageType.REQUEST);
		messageSerialization.serial(message);
		invokers = new Invokers();
		invokers.setInvokeClass(Provider.class);
		invokers.setInvokeMethod(method);
		invokers.setInvokeParmeters(1,2);
		request = new Request<>();
		request.setInvoker(invokers);
		request.setRID(0);
		request.setTimeout(1000);
		request.setType(0);
		message = new AbstractMessage();
		message.setId(0);
		message.setMessage(request);
		message.setMessageType(SocktMessageType.REQUEST);
		messageSerialization.serial(message);
		messageSerialization.serial(message);
		messageSerialization.serial(message);
		messageSerialization.serial(message);
    }
}
