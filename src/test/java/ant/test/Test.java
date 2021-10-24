package ant.test;
import java.net.InetAddress;
import java.net.UnknownHostException;

import com.alibaba.nacos.common.utils.InternetAddressUtil;
import com.yanan.framework.plugin.annotations.Register;

@Register
public class Test implements Request{

	@Override
	public int add(int p0, int p1) {
		return p0+p1;
	}

	@Override
	public int multi(int i, int j) {
		return i*j;
	}
	
	public static void main(String[] args) throws UnknownHostException {
		 InetAddress addr = InetAddress.getLocalHost();
		System.err.println(addr);
	}
}
