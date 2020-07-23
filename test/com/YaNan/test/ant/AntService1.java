package com.YaNan.test.ant;

import com.yanan.frame.ant.annotations.Ant;

@Ant("queue")
public interface AntService1 {
	public int add(int a,int b);
	
	void exec(Runnable runable);
}
