package com.YaNan.test.ant;

import java.io.*;

public interface Provider
{
    int add(final int p0, final int p1);
    
    void exec(final Runnable p0);
    
    String parseString(byte[] bytes);
    
    InputStream getInputStream(final File p0) throws FileNotFoundException;
}
