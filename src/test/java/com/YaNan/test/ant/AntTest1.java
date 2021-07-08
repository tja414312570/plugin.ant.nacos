package com.YaNan.test.ant;

import com.yanan.framework.plugin.annotations.*;
import java.io.*;

@Register
public class AntTest1 implements AntService1
{
    @Override
    public int add(final int a, final int b) {
        System.out.println(this);
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
}
