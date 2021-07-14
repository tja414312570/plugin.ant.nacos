package ant.test;
import com.yanan.framework.plugin.annotations.Register;

@Register
public class Test implements Request{

	@Override
	public int add(int p0, int p1) {
		return p0+p1;
	}
	
}
