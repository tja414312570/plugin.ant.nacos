package com.YaNan.framework.ant.test;

import com.yanan.framework.plugin.*;
import com.YaNan.test.ant.*;
import com.yanan.framework.ant.nacos.*;
import com.yanan.framework.ant.*;
import com.alibaba.nacos.api.exception.*;
import java.lang.reflect.*;
import java.io.*;

public class NacosServiceRegister
{
    public static void main(final String[] args) throws NacosException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InterruptedException, IOException {
        PlugsFactory.getInstance().addScanPath(new Class[] { NacosServiceRegister.class });
        PlugsFactory.init(new String[] { "classpath:plugin.yc" });
        final AntService1 service = (AntService1)PlugsFactory.getPluginsInstance((Class)AntService1.class, new Object[0]);
        System.out.println(service.add(1, 3));
        final AntNacosRuntime antNacosRuntime = new AntNacosRuntime("classpath:Ant.yc");
        final AntContext antContext = AntFactory.build("classpath:Ant.yc");
        antContext.start();
    }
}
