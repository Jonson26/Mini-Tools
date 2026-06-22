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
	public double[] zBuffer;
	public BufferedImage stencil;
	
	public Renderer(){
		zBuffer = new double[Util.SIDE*Util.SIDE];
		stencil = new BufferedImage(Util.SIDE, Util.SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
	}
	
	public BufferedImage renderBackGround(){
		BufferedImage out = new BufferedImage(Util.SIDE, Util.SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
		
		Graphics g = out.getGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0,0,Util.SIDE,Util.SIDE);
		
		g.setColor(Color.GREEN);
		g.drawRect(0,0,Util.SIDE-1,Util.SIDE-1);
		
		return out;
	}
	
	public BufferedImage renderModel(BufferedImage background, Object3D obj, boolean wireframe, int x_o, int y_o){
		BufferedImage out = new BufferedImage(Util.SIDE, Util.SIDE, BufferedImage.TYPE_INT_ARGB_PRE);
		
		Arrays.fill(zBuffer, Double.MAX_VALUE*-1);
		
		Graphics g = out.getGraphics();
		
		g.drawImage(background, 0, 0, null);
		
		// double heading = Math.toRadians(0.0);
		// Matrix3 transform = new Matrix3(new double[] {
				// Math.cos(heading), 0, -Math.sin(heading),
				// 0, 1, 0,
				// Math.sin(heading), 0, Math.cos(heading)
			// });
		
		x_o /= Util.SCALE;
		y_o /= Util.SCALE;
		
		for(int i=0; i<obj.faces.size(); i++){
			Face3D v = obj.faces.get(i);
			Vector3D a = obj.points.get(v.a);
			Vector3D b = obj.points.get(v.b);
			Vector3D c = obj.points.get(v.c);
			
			g.setColor(v.col);
			int[] xPoints = {(int)a.x + x_o, (int)b.x + x_o, (int)c.x + x_o};
			int[] yPoints = {(int)a.y + y_o, (int)b.y + y_o, (int)c.y + y_o};
			double[] zPoints = {a.z, b.z, c.z};
			// int[] xPoints = {(int)((a.x*s_x)/(a.z*r_x)*r_z), (int)((b.x*s_x)/(b.z*r_x)*r_z), (int)((c.x*s_x)/(c.z*r_x)*r_z)};
			// int[] yPoints = {(int)((a.y*s_y)/(a.z*r_y)*r_z), (int)((b.y*s_y)/(b.z*r_y)*r_z), (int)((c.y*s_y)/(c.z*r_y)*r_z)};
			if(wireframe){
				g.drawPolygon(
					xPoints,
					yPoints,
					3
				);
			}else{
				// g.fillPolygon(
					// xPoints,
					// yPoints,
					// 3
				// );
				
				fillTriangle(out, xPoints, yPoints, zPoints, v.col.getRGB());
			}
		}
		
		return out;
	}
	
	public void fillTriangle(BufferedImage target, int[] xPoints, int[] yPoints, double[] zPoints, int rgb){
		double z = 0.0;
		int x_min = Util.max(Util.min(xPoints[0], xPoints[1], xPoints[2]), 0);
		int x_max = Util.min(Util.max(xPoints[0], xPoints[1], xPoints[2]), Util.SIDE);
		int y_min = Util.max(Util.min(yPoints[0], yPoints[1], yPoints[2]), 0);
		int y_max = Util.min(Util.max(yPoints[0], yPoints[1], yPoints[2]), Util.SIDE);
		
		// Graphics g = stencil.getGraphics();
		// g.setColor(Color.BLACK);
		// g.fillRect(0,0,Util.SIDE,Util.SIDE);
		// g.setColor(Color.WHITE);
		// g.fillPolygon(xPoints, yPoints, 3);
		
		for(int x=x_min; x<x_max; x++){
			for(int y=y_min; y<y_max; y++){
				if(
					Util.pointInTriangle(x, y, xPoints[0], yPoints[0], xPoints[1], yPoints[1], xPoints[2], yPoints[2])
					// stencil.getRGB(x, y) == Color.WHITE.getRGB()
				){
					z = (
							zPoints[2]*(x-xPoints[0])*(y-yPoints[1]) + 
							zPoints[0]*(x-xPoints[1])*(y-yPoints[2]) + 
							zPoints[1]*(x-xPoints[2])*(y-yPoints[0]) - 
							zPoints[1]*(x-xPoints[0])*(y-yPoints[2]) - 
							zPoints[2]*(x-xPoints[1])*(y-yPoints[0]) - 
							zPoints[0]*(x-xPoints[2])*(y-yPoints[1])
						)/(
							(x-xPoints[0])*(y-yPoints[1]) +
							(x-xPoints[1])*(y-yPoints[2]) +
							(x-xPoints[2])*(y-yPoints[0]) -
							(x-xPoints[0])*(y-yPoints[2]) -
							(x-xPoints[1])*(y-yPoints[0]) -
							(x-xPoints[2])*(y-yPoints[1])
						);
					if(zBuffer[x+Util.SIDE*y]<z){
						zBuffer[x+Util.SIDE*y]=z;
						target.setRGB(x, y, rgb);
					}
				}
			}
		}
	}
	
	public Vector3D projectVector3D(Vector3D a){
		Vector3D c = new Vector3D(0.0, 0.0, 0.0);
		Vector3D e = new Vector3D(0.0, 0.0, 0.9);
		
		Vector3D d = new Vector3D(a.x-c.x, a.y-c.y, a.z-c.z);
		
		Vector3D b = new Vector3D(e.z/d.z*d.x+e.x, e.z/d.z*d.y+e.y, 0.0);
		
		return b;
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