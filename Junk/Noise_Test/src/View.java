import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.KeyEvent;
import java.lang.Thread;
import javax.vecmath.*;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;
import javax.imageio.ImageIO;

/*
This class contains most of the game logic.
It is also used to "glue" all the other classes together
*/
public class View extends JPanel implements MouseListener, KeyListener {
	private Renderer renderer;
	private BufferedImage background; //Empty game board saved to save time on rendering
	private BufferedImage heightmap, texture;
	
	public View(){
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
		
		addMouseListener(this);
		addKeyListener(this);
		
		renderer = new Renderer();
		background = renderer.renderBackGround(); //Immediately prerender the background
		
		generateLandscape();
		//new AnimationThread().start();
		
		try{
			// Files.writeString(Paths.get(".\\out\\mesh.ply"),
				// Noise.heightmapToPly(heightmap, texture),
				// StandardCharsets.UTF_8);
			// Files.writeString(Paths.get(".\\out\\mesh.obj"),
				// Noise.heightmapToObj(heightmap),
				// StandardCharsets.UTF_8);
			// Files.writeString(Paths.get(".\\out\\texture.mtl"),
				// "newmtl texture\n"+
				// "Ka 1.000 1.000 1.000\n"+
				// "Kd 1.000 1.000 1.000\n"+
				// "Ks 0.000 0.000 0.000\n"+
				// " d 1.0\n"+
				// "illum 2\n"+
				// "# the ambient texture map\n"+
				// "map_Ka texture.png",
				// StandardCharsets.UTF_8);
			ImageIO.write(texture, "png", new File(".\\out\\texture_sky.png"));
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void generateLandscape(){
		int map_scale = 2;
		BufferedImage noise1 = 
			renderer.blurSimpleAverage(
				renderer.blurSimpleAverage(
					renderer.scale(
					renderer.blurSimpleAverage(
						Noise.createNoise(25*map_scale, 25*map_scale),
						2), 
				4.0), 
			3), 
		3);
		
		BufferedImage land1 = Noise.noiseToLand(noise1, 128);
		
		BufferedImage noise2 = renderer.blurSimpleAverage(
			renderer.blurSimpleAverage(
				Noise.createNoise(100*map_scale,100*map_scale), 
				2), 
			2);
		
		BufferedImage land2 = Noise.noiseToLand(noise2, 128);
		
		BufferedImage noise1p2 = renderer.blend(noise1, noise2, 0.4);
		
		// BufferedImage land1p2 = Noise.noiseToLand(noise1p2, 128);
		BufferedImage land1p2 = Noise.noiseToSky(noise1p2, 128);
		
		BufferedImage noise_biome = 
			renderer.blurSimpleAverage(
				renderer.blurSimpleAverage(
					renderer.scale(
						renderer.blurSimpleAverage(
							Noise.createNoise(50*map_scale,50*map_scale), 
							2), 
						2.0), 
					2), 
				2);
		
		BufferedImage land1b = Noise.landToBiome(land1, noise_biome, 128);
		
		BufferedImage land2b = Noise.landToBiome(land2, noise_biome, 128);
		
		BufferedImage land1p2b = Noise.landToBiome(land1p2, noise_biome, 128);
		
		heightmap = noise1p2;
		// texture = land1p2b;
		texture = renderer.blurSimpleAverage(renderer.blurSimpleAverage(land1p2,2),2);;
	}
	
	//Method where the rendering happens
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
		
		//BufferedImage frame = renderer.renderNoise(background, 0, 0);
		BufferedImage frame = renderer.drawImage(background, heightmap, 10, 10);
		frame = renderer.drawImage(frame, texture, 120, 10);
		BufferedImage scaledFrame = renderer.scale(frame, Util.SCALE); //Scale the resulting frame
		g.drawImage(scaledFrame, 0, 0, this); //Copy the frame onto the screen
    }
	
	@Override
	public void mouseClicked(MouseEvent e){
		repaint();
	}
	
	@Override
	public void mousePressed(MouseEvent e){
		
	}
	
	@Override
	public void mouseReleased(MouseEvent e){
		
	}
	
	@Override
	public void mouseEntered(MouseEvent e){
		
	}
	
	@Override
	public void mouseExited(MouseEvent e){
		
	}
	
    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
	
	private class AnimationThread extends Thread{
		private int delta = 0;
		
		public void update(){
			delta++;
		}
		
		@Override
		public void run(){
			while(true){
				try{
					sleep((int)(1000/30));
				}catch(Exception ex){
					System.out.println("Insomnia!");
				}
				
				update();
				
				repaint();
			}
		}
	}
}
