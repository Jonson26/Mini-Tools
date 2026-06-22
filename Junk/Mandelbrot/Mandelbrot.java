import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JFrame;
import java.lang.Thread;

import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import java.awt.image.*;
import java.io.*;
import java.util.Iterator;

public class Mandelbrot extends JFrame{
	public static final double PW = 255.0;//999.0; // Pixel Width
	public static final double PH = 255.0;//999.0; // Pixel Height
	public static final double XFW = 2.47; // X Frame Width
	public static final double XFO = 2.0; // X Frame Height
	public static final double YFW = 2.24; // Y Frame Width
	public static final double YFO = 1.12; // Y Frame Height
	
	public static final int MAX_ITER = 1064;
	public static final int TOP_OFFSET = 0;//20;
	public static final int FONT_SIZE = 10;//22;
	public static final boolean GIF = true;//false;
	
	public boolean lock = false;
	
    private BufferedImage I;
	
	int[][] brot;
	int delta = 0;
	
	public static int[][] calcBrot(){
		int[][] brot = new int[(int)PH+1][(int)PW+1];
		int px,py,i;
		double x0,y0,x,y,xt;
		for(py=0;py<=PH;py++){
			for(px=0;px<=PW;px++){
				x=y=0;
				x0=(px/PW)*XFW-XFO;
				y0=(py/PH)*YFW-YFO;
				for(i=MAX_ITER;x*x+y*y<=4&&i>0;i--){
					xt=x*x-y*y+x0;
					y=2*x*y+y0;
					x=xt;
				}
				brot[py][px] = i;
			}
		}
		return brot;
	}
	
	public Mandelbrot(){
		super("Mandelbrot Set");
        setBounds(0, 0, (int)PW+1, (int)PH+1+TOP_OFFSET);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		brot = calcBrot();
		
		new AnimationThread().start();
	}
	
	@Override
    public void paint(Graphics g) {
		lock = true;
		I = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics ig = I.getGraphics();
		for(int py=0;py<=PH;py++){
			for(int px=0;px<=PW;px++){
				int iter = brot[py][px];
				double distance = Math.sqrt(Math.pow(py-PH/2, 2)+Math.pow(px-PW/2, 2));
				// I.setRGB(px, py+20, iter | (iter << (int)(((py+20)/(PH+20))*24+delta)%24));
				I.setRGB(px, py+TOP_OFFSET, iter | (iter << (int)Math.abs(distance/Math.PI-delta)%24));
			}
		}
		
		String text = "DAJ FILIPOWI PODWYŻKĘ";
		
		ig.setColor(Color.CYAN);
		
		ig.setFont(new Font("", Font.BOLD, FONT_SIZE));
		
		ig.drawChars(text.toCharArray(), 0, text.length(), 
		(int)(PW/2 + (Math.cos(delta/(6*Math.PI))*10*FONT_SIZE/2)-text.length()*FONT_SIZE/4), 
		(int)(PH/2 + (Math.sin(delta/(6*Math.PI))*10*FONT_SIZE/2)-FONT_SIZE/4));
        
		g.drawImage(I, 0, 0, this);
		lock = false;
    }

	public static void main(String[] args){
		if(args.length>0 && args[0].equals("-CLI")){
			int[][] brot = calcBrot();
			
			String o="";
			for(int py=0;py<=PH;py++){
				for(int px=0;px<=PW;px++){
					if(brot[py][px]<1)o+="█";
					else o+="▒";
				}
				o+="\n";
			}
			System.out.print(o);
		}else{
			new Mandelbrot().setVisible(true);
		}
	}
	
	private class AnimationThread extends Thread{
		BufferedImage[] frames = new BufferedImage[120];
		
		public void update(){
			delta++;
		}
		
		@Override
		public void run(){
			while(true){
				try{
					sleep((int)(1000/12));
				}catch(Exception ex){
					System.out.println("Insomnia!");
				}
				
				update();
				
				repaint();
				
				if(GIF){
					while(lock){
						try{
							sleep((int)(1000/12));
						}catch(Exception ex){
							System.out.println("Insomnia!");
						}
					}
					if(delta<=120){
						frames[delta-1] = I;
						System.out.print(".");
					}
					if(delta==120){
						System.out.println("!");
						try{
							// create a new BufferedOutputStream with the last argument
							ImageOutputStream output = 
								new FileImageOutputStream(new File("Mandelbrot.gif"));
							System.out.println("Initialised ImageOutputStream...");

							// create a gif sequence with the type of the first image, 1 second
							// between frames, which loops continuously
							GifSequenceWriter writer = 
								new GifSequenceWriter(output, frames[0].getType(), 1, false);
							System.out.println("Initialised GifSequenceWriter...");

							// write out the first image to our sequence...
							System.out.println("Writing frames...");
							for(int i=0; i<120; i++) {
								System.out.println("   Writing frame #"+i+"...");
								BufferedImage nextImage = frames[i];
								writer.writeToSequence(nextImage);
							}

							System.out.println("Closing...");
							writer.close();
							output.close();
							System.out.println("Done!");
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
			}
		}
	}
}