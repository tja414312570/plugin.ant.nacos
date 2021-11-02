package com.yanan.frame.ant.test;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.yanan.framework.a.nacos.NacosInstance;
import com.yanan.framework.ant.core.cluster.ChannelManager;
import com.yanan.framework.ant.dispatcher.ChannelDispatcher;
import com.yanan.framework.ant.proxy.Callback;
import com.yanan.framework.ant.proxy.Invokers;
import com.yanan.framework.plugin.PlugsFactory;
import com.yanan.framework.plugin.decoder.StandScanResource;
import com.yanan.test.ant.Media;
import com.yanan.utils.reflect.cache.ClassHelper;
import com.yanan.utils.resource.ResourceManager;

public class MediaClient {
	private static JFrame frame;
	private static PaintPanel panel;



	public static void main(String[] args) throws InterruptedException, IOException {
//		Thread.sleep(10000l);
		PlugsFactory.init(
        		ResourceManager.getResource("classpath:Ant2.yc"), 
        		new StandScanResource( "classpath*:**")
        		);
        
        ChannelManager<Object> channelManager = PlugsFactory.getPluginsInstance(ChannelManager.class);
        
        channelManager.start();
        ChannelDispatcher channelDispatcher = PlugsFactory.getPluginsInstance(ChannelDispatcher.class);
		System.err.println(channelDispatcher);
		channelDispatcher.bind(channelManager);
		
		Invokers invokers = new Invokers();
		invokers.setInvokeClass(Media.class);
		Method method = ClassHelper.getClassHelper(Media.class).getMethod("subscribe");
		invokers.setInvokeMethod(method);
		invokers.setInvokeParmeters();
		NacosInstance nacosInstance = new NacosInstance();
		nacosInstance.setName("defaultName");
		
		channelDispatcher.subscribe("defaultName",invokers, (ctx,res)->{
		BufferedImage bufferedImage;
			System.err.println(((byte[])res).length/1024+" kb/帧");
//				bufferedImage = createImageFromBytes((byte[])res);
			showImage((Image) res, new Dimension(1280,720));
		}
		);
//		System.out.println("执行结果:"+result);
		System.out.println("服务列表:"+channelManager.getChannelList("defaultName"));
		
		
	}
	private static BufferedImage createImageFromBytes(byte[] imageData) throws IOException {
	    ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
	    return ImageIO.read(bais);
	}
	/**
     * 显示Image图片元素
     * @param image
     * @param size
     */
    public static void showImage(Image image, Dimension size) {
    	if(frame == null) {
    		 frame = new JFrame();
    	        frame.setSize(size);
    	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    	        frame.setLayout(new BorderLayout());
    	       panel= new PaintPanel(image, size);
    	        frame.add(panel);
    	        frame.pack();
    	        frame.setLocationRelativeTo(null);
    	        frame.setVisible(true);
    	}else {
    		panel.setImageIcon(image);
    		panel.repaint();
    	}
       
    }

  

    /**
     * 图片容器
     */
    private static class PaintPanel extends JPanel {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private ImageIcon imageIcon;
        public ImageIcon getImageIcon() {
			return imageIcon;
		}

		public void setImageIcon(Image imageIcon) {
			this.imageIcon = new ImageIcon(imageIcon);
		}

		private Dimension size;

        public PaintPanel(Image image, Dimension size) {
            imageIcon = new ImageIcon(image);
            this.size = size;
        }

        @Override
        public Dimension getPreferredSize() {
            // 初识大小
            return size;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // 图片大小自适应，可拖拽
            g.drawImage(this.imageIcon.getImage(), 0, 0, this.getWidth(), this.getHeight(), imageIcon.getImageObserver());
        }
    }
}
