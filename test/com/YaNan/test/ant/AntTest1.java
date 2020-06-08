package com.YaNan.test.ant;

import com.YaNan.frame.plugin.annotations.Register;

@Register
public class AntTest1 implements AntService1{

	@Override
	public int add(int a, int b) {
		System.out.println("传送:"+a+"  "+b);
		return a+b;
	}

}
