package com.YaNan.test.ant;

import java.io.*;

public interface AntService1
{
    int add(final int p0, final int p1);
    
    void exec(final Runnable p0);
    
    InputStream getInputStream(final File p0) throws FileNotFoundException;
}
