package com.YaNan.frame.ant.test;

import com.yanan.framework.plugin.*;
import com.yanan.framework.ant.handler.*;
import com.yanan.framework.ant.protocol.ant.command.*;
import com.yanan.framework.ant.nacos.*;
import com.yanan.framework.ant.*;
import com.alibaba.nacos.api.exception.*;
import java.lang.reflect.*;
import java.io.*;

public class NacosServiceRegister
{
    public static void main(final String[] args) throws NacosException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InterruptedException, IOException {
        PlugsFactory.getInstance().addDefinition((Class)AntMeessageSerialHandler.class);
        PlugsFactory.getInstance().addDefinition((Class)DefaultAntClientServiceImpl.class);
        final AntNacosRuntime antNacosRuntime = new AntNacosRuntime("classpath:Ant.yc");
        final AntContext antContext = AntFactory.build("classpath:Ant.yc");
        antContext.start();
    }
}
