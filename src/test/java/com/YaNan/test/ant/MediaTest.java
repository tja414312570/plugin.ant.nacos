package com.yanan.test.ant;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.peer.RobotPeer;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.omg.CORBA.CTX_RESTRICT_SCOPE;

import com.yanan.framework.ant.dispatcher.DispatcherContext;
import com.yanan.framework.plugin.annotations.Register;

@Register
public class MediaTest implements Media{
	public static void main(String[] args) {
		new MediaTest().byffer();
	}
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
	@Override
	public byte[] byffer() {
		try {
			long now = System.currentTimeMillis();
			BufferedImage bufferedImage = new Robot().createScreenCapture(new Rectangle(0, 0,1280,720));
			long cap = System.currentTimeMillis()-now;
			System.err.println("截图:"+cap);
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ImageIO.setUseCache(false);
			boolean flag = ImageIO.write(bufferedImage, "bmp", out);
			byte[] b = out.toByteArray();
			System.err.println("写入:"+(System.currentTimeMillis()-cap-now));
			return b;
		} catch (AWTException | IOException e) {
			e.printStackTrace();
		}
		return null;

	}
	@Override
	public void subscribe() {
		DispatcherContext<String> ctx = DispatcherContext.getCurrentContext();
		long times = System.currentTimeMillis();
		long counts = 0;
		try {
			while(true) {
				long now = System.currentTimeMillis();
				BufferedImage bufferedImage = new Robot().createScreenCapture(new Rectangle(0, 0,1280,720));
				long captTimes = (System.currentTimeMillis()-now);
				ctx.response(bufferedImage);
				
				counts++;
				System.err.println("截图"+captTimes+"ms,传输:"+(System.currentTimeMillis()-now-captTimes)+"ms,平均帧率:"+(counts*1000/(System.currentTimeMillis()-times)));
				Thread.sleep(1l);
			}
		}catch(Exception e) {
			ctx.exception(e);
		}
		
		
	}

}
