package com.yanan.test.ant;

import java.io.*;

import com.yanan.framework.ant.dispatcher.DispatcherContext;

public interface Provider
{
    int add(final int p0, final int p1);
    
    void exec(final Runnable p0);
    
    String parseString(byte[] bytes);
    
    InputStream getInputStream(final File p0) throws FileNotFoundException;
    
    void notify(String args);
}
