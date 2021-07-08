package com.YaNan.test.ant;

import com.yanan.framework.ant.handler.AntMeessageSerialHandler;
import com.yanan.framework.ant.protocol.ant.command.DefaultAntClientServiceImpl;
import com.yanan.framework.plugin.PlugsFactory;

public class Test {
	public static void main(String[] args) {
		PlugsFactory.getInstance().addDefinition(AntMeessageSerialHandler.class);//消息序列化
		PlugsFactory.getInstance().addDefinition(DefaultAntClientServiceImpl.class);
	}
}
