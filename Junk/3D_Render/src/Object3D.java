import java.util.ArrayList;
import java.awt.Color;

public class Object3D{
	public ArrayList<Vector3D> points;
	public ArrayList<Face3D> faces;
	
	public Object3D(){
		points = new ArrayList<>();
		faces = new ArrayList<>();
	}
	
	public Object3D(Object3D src){
		points = new ArrayList<>();
		faces = new ArrayList<>();
		for(int i=0; i<src.points.size(); i++) points.add(new Vector3D(src.points.get(i)));
		for(int i=0; i<src.faces.size(); i++) faces.add(new Face3D(src.faces.get(i)));
	}
	
	public int addPoint(Vector3D v){
		int n = points.size();
		points.add(v);
		return n;
	}
	
	public void addFace(int i_a, int i_b, int i_c, Color col){
		faces.add(new Face3D(i_a, i_b, i_c, col));
	}
	
	public void mul(double n){
		for(int i=0; i<points.size(); i++){
			points.get(i).mul(n);
		}
	}
	
	public Object3D mulSafe(double n){
		Object3D out = new Object3D(this);
		out.mul(n);
		return out;
	}
	
	public void add(Vector3D v){
		for(int i=0; i<points.size(); i++){
			points.get(i).add(v);
		}
	}
	
	public Object3D addSafe(Vector3D v){
		Object3D out = new Object3D(this);
		out.add(v);
		return out;
	}
	
	public void rotate(double pitch, double roll, double yaw) {
		double cosa = Math.cos(yaw);
		double sina = Math.sin(yaw);

		double cosb = Math.cos(pitch);
		double sinb = Math.sin(pitch);

		double cosc = Math.cos(roll);
		double sinc = Math.sin(roll);

		double Axx = cosa*cosb;
		double Axy = cosa*sinb*sinc - sina*cosc;
		double Axz = cosa*sinb*cosc + sina*sinc;

		double Ayx = sina*cosb;
		double Ayy = sina*sinb*sinc + cosa*cosc;
		double Ayz = sina*sinb*cosc - cosa*sinc;

		double Azx = -sinb;
		double Azy = cosb*sinc;
		double Azz = cosb*cosc;

		for (var i=0; i<points.size(); i++) {
			Vector3D point = points.get(i);
			double px = point.x;
			double py = point.y;
			double pz = point.z;

			point.x = Axx*px + Axy*py + Axz*pz;
			point.y = Ayx*px + Ayy*py + Ayz*pz;
			point.z = Azx*px + Azy*py + Azz*pz;
		}
	}
	
	public Object3D rotateSafe(double pitch, double roll, double yaw){
		Object3D out = new Object3D(this);
		out.rotate(pitch, roll, yaw);
		return out;
	}
}