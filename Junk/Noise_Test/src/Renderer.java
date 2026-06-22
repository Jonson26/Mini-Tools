import java.awt.image.BufferedImage;
import java.awt.image.AffineTransformOp;
import java.awt.geom.AffineTransform;
import java.awt.Graphics;
import java.awt.Color;
import java.util.Arrays;

/*
This class contains most of the rendering code.
*/
public class Renderer{
	
	public Renderer(){
	}
	
	public BufferedImage renderBackGround(){
		BufferedImage out = new BufferedImage(Util.SIDE, Util.SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
		
		Graphics g = out.getGraphics();
		
		g.setColor(Color.GRAY);
		g.fillRect(0,0,Util.SIDE,Util.SIDE);
		
		g.setColor(Color.GREEN);
		g.drawRect(0,0,Util.SIDE-1,Util.SIDE-1);
		
		return out;
	}
	
	public BufferedImage renderNoise(BufferedImage background, int x_o, int y_o){
		BufferedImage out = new BufferedImage(Util.SIDE, Util.SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
		
		Graphics g = out.getGraphics();
		
		g.drawImage(background, 0, 0, null);
		
		BufferedImage noise1 = 
		blurSimpleAverage(blurSimpleAverage(scale(blurSimpleAverage(Noise.createNoise(25,25), 2), 4.0), 3), 3);
		g.drawImage(noise1, 0, 0, null);
		
		BufferedImage land1 = Noise.noiseToLand(noise1, 128);
		g.drawImage(land1, 0, 100, null);
		
		BufferedImage noise2 = blurSimpleAverage(blurSimpleAverage(Noise.createNoise(100,100), 2), 2);
		g.drawImage(noise2, 100, 0, null);
		
		BufferedImage land2 = Noise.noiseToLand(noise2, 128);
		g.drawImage(land2, 100, 100, null);
		
		BufferedImage noise1p2 = blend(noise1, noise2, 0.4);
		g.drawImage(noise2, 200, 0, null);
		
		BufferedImage land1p2 = Noise.noiseToLand(noise1p2, 128);
		g.drawImage(land1p2, 200, 100, null);
		
		BufferedImage noise_biome = 
		blurSimpleAverage(blurSimpleAverage(scale(blurSimpleAverage(Noise.createNoise(50,50), 2), 2.0), 2), 2);
		g.drawImage(noise_biome, 300, 200, null);
		
		BufferedImage land1b = Noise.landToBiome(land1, noise_biome, 128);
		g.drawImage(land1b, 0, 200, null);
		
		BufferedImage land2b = Noise.landToBiome(land2, noise_biome, 128);
		g.drawImage(land2b, 100, 200, null);
		
		BufferedImage land1p2b = Noise.landToBiome(land1p2, noise_biome, 128);
		g.drawImage(land1p2b, 200, 200, null);
		
		out = renderText(out, "noise 1", 2, 10);
		out = renderText(out, "land 1", 2, 110);
		out = renderText(out, "noise 2", 102, 10);
		out = renderText(out, "land 2", 102, 110);
		out = renderText(out, "noise 1+2", 202, 10);
		out = renderText(out, "land 1+2", 202, 110);
		out = renderText(out, "biome noise", 302, 210);
		
		return out;
	}
	
	public static BufferedImage drawImage(BufferedImage background, BufferedImage image, int x, int y){
		BufferedImage out = new BufferedImage(Util.SIDE, Util.SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
		
		Graphics g = out.getGraphics();
		
		g.drawImage(background, 0, 0, null);
		g.drawImage(image,      x, y, null);
		
		return out;
	}
	
	public static BufferedImage blurSimpleAverage(BufferedImage in, int intensity){
		int width = in.getWidth();
		int height = in.getHeight();
		
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				int red = 0;
				int green = 0;
				int blue = 0;
				int alpha = 0;

				int counter = 0;

				for(int i=-intensity; i<=intensity; i++){
					for(int j=-intensity; j<=intensity; j++){
						if(x+i>=0 && y+j>=0 && x+i<width && y+j<height){
							int argb = in.getRGB(x+i, y+j);

							red += (argb >> 16) & 0xFF;
							green += (argb >> 8) & 0xFF;
							blue += argb & 0xFF;
							alpha += (argb >> 24) & 0xFF;

							counter++;
						}
					}
				}

				if(counter==0) counter=1;

				red = red / counter;
				green = green / counter;
				blue = blue / counter;
				alpha = alpha / counter;
				
				out.setRGB(x, y, new Color(red, green, blue, alpha).getRGB());
			}
		}
		
		return out;
	}
	
	public static BufferedImage blend(BufferedImage n1, BufferedImage n2, double ratio){
		int width = n1.getWidth();
		int height = n1.getHeight();
		
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				double red = 0;
				double green = 0;
				double blue = 0;
				double alpha = 0;

				int argb1 = n1.getRGB(x, y);
				red   += ((argb1 >> 16) & 0xFF)*((1.0-ratio)*2.0);
				green += ((argb1 >>  8) & 0xFF)*((1.0-ratio)*2.0);
				blue  += ( argb1        & 0xFF)*((1.0-ratio)*2.0);
				alpha += ((argb1 >> 24) & 0xFF)*((1.0-ratio)*2.0);

				int argb2 = n2.getRGB(x, y);
				red   += ((argb2 >> 16) & 0xFF)*(ratio*2.0);
				green += ((argb2 >>  8) & 0xFF)*(ratio*2.0);
				blue  += ( argb2        & 0xFF)*(ratio*2.0);
				alpha += ((argb2 >> 24) & 0xFF)*(ratio*2.0);

				red   /= 2.0;
				green /= 2.0;
				blue  /= 2.0;
				alpha /= 2.0;
				
				out.setRGB(x, y, new Color((int)red, (int)green, (int)blue, (int)alpha).getRGB());
			}
		}
		
		return out;
	}
	
	public static BufferedImage renderText(BufferedImage background, String text, int x_o, int y_o){
		BufferedImage out = new BufferedImage(Util.SIDE, Util.SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
		
		Graphics g = out.getGraphics();
		
		g.drawImage(background, 0, 0, null);
		
		g.setColor(Color.CYAN);
		
		g.drawChars(text.toCharArray(), 0, text.length(), x_o, y_o);
		
		return out;
	}
	
	//Method used to scale a game frame to the desired size. Useful because monitors are getting bigger and bigger.
	public static BufferedImage scale(BufferedImage before, double scale) {
		int w = before.getWidth();
		int h = before.getHeight();
		// Create a new image of the proper size
		int w2 = (int) (w * scale);
		int h2 = (int) (h * scale);
		BufferedImage after = new BufferedImage(w2, h2, BufferedImage.TYPE_INT_ARGB);
		AffineTransform scaleInstance = AffineTransform.getScaleInstance(scale, scale);
		AffineTransformOp scaleOp 
			= new AffineTransformOp(scaleInstance, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);

		scaleOp.filter(before, after);
		return after;
	}
}