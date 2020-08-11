package com.YaNan.test.ant;

import com.yanan.framework.ant.annotations.Ant;

@Ant("queue")
public interface AntService1 {
	public int add(int a,int b);
	
	void exec(Runnable runable);
}
