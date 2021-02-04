package com.YaNan.test.ant;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public interface AntService1 {
	public int add(int a,int b);
	
	void exec(Runnable runable);
	
	InputStream getInputStream(File file) throws FileNotFoundException;
}
