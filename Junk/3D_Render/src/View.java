import javax.swing.JPanel;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import java.lang.Thread;

/*
This class contains most of the game logic.
It is also used to "glue" all the other classes together
*/
public class View extends JPanel implements MouseListener {
	
	private Renderer renderer;
	private BufferedImage background; //Empty game board saved to save time on rendering
	private Object3D cube, cube_rotated, cube_moved;
	
	public View(){
		renderer = new Renderer();
		background = renderer.renderBackGround(); //Immediately prerender the background
		// cube = Util.loadObject3DFromFile("./img/gymnast.obj");
		// cube = Util.loadObject3DFromFile("./img/cube.obj");
		// cube = Util.loadObject3DFromFile("./img/teapot.obj");
		// cube = Util.loadObject3DFromFile("./img/lucy.obj");
		// cube = Util.loadObject3DFromFile("./img/suzanne.obj");
		cube = Util.loadObject3DFromFile("./img/cow.obj");
		// for(int i=0; i<cube.points.size(); i++){
			// Vector3D v = cube.points.get(i);
			// v.x -= 0.5;
			// v.y -= 0.5;
			// v.z -= 0.5;
		// }
		cube.mul(20.0/Util.SCALE);
		// cube.mul(0.1);
		cube.rotate(0.0, 3.1415/2, 0.0);
		cube.rotate(0.0, 3.1415/2, 0.0);
		//cube.mul(70.0);
		cube_rotated = new Object3D(cube);
		cube_moved = new Object3D(cube);
		new AnimationThread().start();
	}
	
	//Method where the rendering happens
	public void paintComponent(Graphics g) {
        super.paintComponent(g);
		
		BufferedImage frame = renderer.renderModel(background, cube_moved, true, 100, 450); 
		frame = renderer.renderModel(frame, cube_rotated, false, 500, 450); 
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
	
	private class AnimationThread extends Thread{
		private double dir = 1.0;
		private double move = 0.0;
		
		public void update_move(){
			move += dir;
			if(move >= 50.0) dir = -1.0;
			if(move <= -50.0) dir = 1.0;
		}
		
		@Override
		public void run(){
			double x_rot = 0.0;
			double move_raw = 0.0;
			while(true){
				try{
					sleep((int)(1000/60));
				}catch(Exception ex){
					System.out.println("Insomnia!");
				}
				
				cube_rotated = new Object3D(cube);
				cube_rotated.mul(3.0);
				cube_rotated.rotate(x_rot, 0.0, 0.0);
				cube_rotated.rotate(0.0, -0.2, 0.0);
				x_rot+=3.1415/2/60;
				
				cube_moved = new Object3D(cube);
				cube_moved.add(new Vector3D(0.0, 0.0, move));
				update_move();
				
				repaint();
			}
		}
	}
}
