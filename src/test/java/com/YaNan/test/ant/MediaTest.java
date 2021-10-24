package com.YaNan.test.ant;

import java.awt.AWTException;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.yanan.framework.plugin.annotations.Register;

@Register
public class MediaTest implements Media{
	Dimension d = Toolkit.getDefaultToolkit().getScreenSize(); 
	@Override
	public byte[] byffer() {
		try {
			BufferedImage bufferedImage = new Robot().createScreenCapture(new Rectangle(0, 0,d.width,d.height));
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			boolean flag = ImageIO.write(bufferedImage, "gif", out);
			byte[] b = out.toByteArray();
			return b;
		} catch (AWTException | IOException e) {
			e.printStackTrace();
		}
		return null;

	}

}
