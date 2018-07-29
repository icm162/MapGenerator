


import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class IncreaseBuffer{
	public static ImageIcon execute(String ip, int bs){
		return enlargeImage(ip, bs);
	}
	public static BufferedImage execute1(BufferedImage bi, int bs){
		return enlargeImageBuffered(bi, bs);
	}
	public static ImageIcon enlargeImage(String ip, int bs){
		ImageIcon iio = new ImageIcon();
		try{
			BufferedImage bi = ImageIO.read(new File(ip));
			BufferedImage bio = new BufferedImage(bs * bi.getWidth(), bs * bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
			for(int cx = 0; cx < bi.getWidth(); cx++){
				for(int cy = 0; cy < bi.getHeight(); cy++){
					int rgbs = bi.getRGB(cx, cy);
					Color cs = new Color(rgbs);
					if(rgbs >> 24 == 0){
						cs = new Color(0, 0, 0, 0);
					}
					enlarge(bio, cs, cx, cy, bs);	
				}
			}	
			iio = new ImageIcon(bio);
		}catch (IOException e){
			e.printStackTrace();
		}
		return iio;
	}
	public static BufferedImage enlargeImageBuffered(BufferedImage bi, int bs){
		BufferedImage bio = new BufferedImage(bs * bi.getWidth(), bs * bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
		for(int cx = 0; cx < bi.getWidth(); cx++){
			for(int cy = 0; cy < bi.getHeight(); cy++){
				int rgbs = bi.getRGB(cx, cy);
				Color cs = new Color(rgbs);
				if(rgbs >> 24 == 0){
					cs = new Color(0, 0, 0, 0);
				}
				enlarge(bio, cs, cx, cy, bs);	
			}
		}
		return bio;
	}
	public static void enlarge(BufferedImage bio, Color c, int x, int y, int bs){
		for(int cx = 0; cx < bs; cx++){
			for(int cy = 0; cy < bs; cy++){
				bio.setRGB(bs * x + cx, bs * y + cy, c.getRGB());
			}
		}
	}
}
