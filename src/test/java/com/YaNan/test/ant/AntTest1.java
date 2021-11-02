package com.yanan.test.ant;

import com.yanan.framework.ant.dispatcher.DispatcherContext;
import com.yanan.framework.plugin.annotations.*;
import java.io.*;

@Register
public class AntTest1 implements Provider
{
    @Override
    public int add(final int a, final int b) {
//        System.out.println(this);
        return a + b;
    }
    
    @Override
    public void exec(final Runnable runable) {
        runable.run();
    }
    
    @Override
    public InputStream getInputStream(final File file) throws FileNotFoundException {
        return new FileInputStream(file);
    }

	@Override
	public String parseString(byte[] bytes) {
		return new String(bytes);
	}

	@Override
	public void notify(String args) {
		DispatcherContext<String> ctx = DispatcherContext.getCurrentContext();
		int i = 0;
		while(true) {
			i++;
			ctx.response(args+"-->"+i);
			try {
				Thread.sleep(1000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
