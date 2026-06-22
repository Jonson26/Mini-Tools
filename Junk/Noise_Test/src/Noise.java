import java.awt.image.BufferedImage;
import java.awt.Color;

public class Noise{
	public static BufferedImage createNoise(int width, int height){
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				int v = (int)(Math.random()*255);
				out.setRGB(x, y, new Color(v,v,v).getRGB());
			}
		}
		
		return out;
	}
	
	public static BufferedImage noiseToLand(BufferedImage noise, int sealevel){
		int width = noise.getWidth();
		int height = noise.getHeight();
		
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				int argb = noise.getRGB(x, y);

				int red   = (argb >> 16) & 0xFF;
				int green = (argb >>  8) & 0xFF;
				int blue  =  argb        & 0xFF;
				int alpha = (argb >> 24) & 0xFF;
				
				if(blue>sealevel) out.setRGB(x, y, Color.GRAY.getRGB());
				else out.setRGB(x, y, Color.BLUE.getRGB());
			}
		}
		
		return out;
	}
	
	public static BufferedImage noiseToSky(BufferedImage noise, int threshold){
		int width = noise.getWidth();
		int height = noise.getHeight();
		
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				int argb = noise.getRGB(x, y);

				int red   = (argb >> 16) & 0xFF;
				int green = (argb >>  8) & 0xFF;
				int blue  =  argb        & 0xFF;
				int alpha = (argb >> 24) & 0xFF;
				
				if(blue>threshold) out.setRGB(x, y, Color.WHITE.getRGB());
				else out.setRGB(x, y, new Color(100, 100, 255).getRGB());
			}
		}
		
		return out;
	}
	
	public static BufferedImage landToBiome(BufferedImage noise_land, BufferedImage noise_biome, int threshold){
		int width = noise_land.getWidth();
		int height = noise_land.getHeight();
		
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB_PRE);
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				int argb_land = noise_land.getRGB(x, y);
				
				int argb_biome = noise_biome.getRGB(x, y);

				int red_biome   = (argb_biome >> 16) & 0xFF;
				int green_biome = (argb_biome >>  8) & 0xFF;
				int blue_biome  =  argb_biome        & 0xFF;
				int alpha_biome = (argb_biome >> 24) & 0xFF;
				
				if(argb_land == Color.BLUE.getRGB()) out.setRGB(x, y, Color.BLUE.getRGB());
				else if(blue_biome>threshold) out.setRGB(x, y, Color.GREEN.getRGB());
				else out.setRGB(x, y, Color.YELLOW.getRGB());
			}
		}
		
		return out;
	}
	
	public static String heightmapToObj(BufferedImage heightmap){
		int width = heightmap.getWidth();
		int height = heightmap.getHeight();
		
		int[][] vertices = new int[width*height][3];
		
		String out = 
		// "mtllib texture.mtl\n"+
		// "usemtl texture\n"+
		"#Vertices\n";
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				int argb = heightmap.getRGB(x, y);

				int red   = (argb >> 16) & 0xFF;
				int green = (argb >>  8) & 0xFF;
				int blue  =  argb        & 0xFF;
				int alpha = (argb >> 24) & 0xFF;
				
				vertices[x+y*width][0] = x;
				vertices[x+y*width][1] = y;
				vertices[x+y*width][2] = blue;
				
				out += String.format("v %d %d %d\n", x, blue, y);
			}
		}
		
		out += "#Vertices\n";
		
		for(int x=1; x<width-1; x++){
			for(int y=1; y<height-1; y++){
				out += String.format("f %d %d %d\n", y*width+x, y*width+x+1, (y+1)*width+x);
				out += String.format("f %d %d %d\n", y*width+x+1, (y+1)*width+x, (y+1)*width+x+1);
			}
		}
		
		return out;
	}
	
	public static String heightmapToPly(BufferedImage heightmap, BufferedImage texture){
		int width = heightmap.getWidth();
		int height = heightmap.getHeight();
		
		int[][] vertices = new int[width*height][3];
		
		String out =
		"ply\n"+
		"format ascii 1.0\n"+
		"element vertex %d\n"+
		"property float x\n"+
		"property float y\n"+
		"property float z\n"+
		"property uchar red\n"+
		"property uchar green\n"+
		"property uchar blue\n"+
		"element face %d\n"+
		"property list uchar int vertex_index\n"+
		"end_header\n";
		
		int vertex_count = 0;
		
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				int argb = heightmap.getRGB(x, y);

				int z = argb & 0xFF;
				
				argb = texture.getRGB(x, y);

				int red   = (argb >> 16) & 0xFF;
				int green = (argb >>  8) & 0xFF;
				int blue  =  argb        & 0xFF;
				int alpha = (argb >> 24) & 0xFF;
				
				vertices[x+y*width][0] = x;
				vertices[x+y*width][1] = y;
				vertices[x+y*width][2] = z;
				
				out += String.format("%f %f %f %d %d %d\n", x/(width*1.0), y/(height*1.0), ((int)Math.max(z, 128)/2)/256.0, red, green, blue);
			}
		}
		
		for(int x=1; x<width-1; x++){
			for(int y=1; y<height-1; y++){
				vertex_count += 2;
				out += String.format("3 %d %d %d\n", y*width+x, y*width+x+1, (y+1)*width+x);
				out += String.format("3 %d %d %d\n", y*width+x+1, (y+1)*width+x, (y+1)*width+x+1);
			}
		}
		
		return String.format(out, width*height, vertex_count).replaceAll(",",".");
	}
}