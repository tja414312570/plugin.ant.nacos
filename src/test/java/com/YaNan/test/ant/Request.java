package com.YaNan.test.ant;

import com.yanan.framework.a.proxy.Ant;
import com.yanan.framework.plugin.annotations.Service;

@Ant
@Service
public interface Request
{
    int add(final int p0, final int p1);
}
