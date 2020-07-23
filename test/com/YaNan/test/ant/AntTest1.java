package com.YaNan.test.ant;

import com.yanan.frame.plugin.annotations.Register;
import com.yanan.frame.plugin.annotations.Service;

@Register
public class AntTest1 implements AntService1{
	@Service
	private AntService2 service2;
	@Override
	public int add(int a, int b) {
		System.out.println(this);
		return a+b+service2.add(a, b);
	}

	@Override
	public void exec(Runnable runable) {
		runable.run();
	}

}
