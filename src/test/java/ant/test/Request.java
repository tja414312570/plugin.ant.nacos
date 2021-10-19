package ant.test;

import com.yanan.framework.ant.proxy.Ant;

@Ant("defaultName")
public interface Request
{
    int add(final int p0, final int p1);

	int multi(int i, int j);

}
