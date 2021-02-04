package com.YaNan.test.ant;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.yanan.framework.plugin.annotations.Register;
import com.yanan.framework.plugin.annotations.Service;

@Register
public class AntTest1 implements AntService1{
//	@Service
//	private AntService2 service2;
	@Override
	public int add(int a, int b) {
		System.out.println(this);
		return a+b;
	}

	@Override
	public void exec(Runnable runable) {
		runable.run();
	}

	@Override
	public InputStream getInputStream(File file) throws FileNotFoundException {
		return new FileInputStream(file);
	}

}
