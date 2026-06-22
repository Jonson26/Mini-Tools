public class Vector3D{
	public double x, y, z;
	
	public Vector3D(double x, double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3D(Vector3D src){
		this(src.x, src.y, src.z);
	}
	
	public Vector3D(){
		this(0.0, 0.0, 0.0);
	}
	
	public void add(Vector3D v){
		x += v.x;
		y += v.y;
		z += v.z;
	}
	
	public Vector3D addSafe(Vector3D v){
		Vector3D out = new Vector3D(this);
		out.add(v);
		return out;
	}
	
	public void mul(double n){
		x *= n;
		y *= n;
		z *= n;
	}
	
	public Vector3D mulSafe(double n){
		Vector3D out = new Vector3D(this);
		out.mul(n);
		return out;
	}
	
	public void cross(Vector3D v){
		x = -v.y*z+y*v.z;
		y = v.x*z-x*v.z;
		z = -v.x*y+x*v.y;
	}
	
	public Vector3D crossSafe(Vector3D v){
		Vector3D out = new Vector3D(this);
		out.cross(v);
		return out;
	}
	
	public double dist(Vector3D v){
		return Math.sqrt((x-v.x)*(x-v.x) + (y-v.y)*(y-v.y) + (z-v.z)*(z-v.z));
	}
	
	public double magnitude(){
		return dist(new Vector3D());
	}
	
	public void normalize(){
		double mag = magnitude();
		if(mag > 0.0001){
			x /= mag;
			y /= mag;
			z /= mag;
		}else{
			x = 0.0;
			y = 0.0;
			z = 0.0;
		}
	}
	
	public Vector3D normalizeSafe(){
		Vector3D out = new Vector3D(this);
		out.normalize();
		return out;
	}
	
	@Override
    public boolean equals(Object obj) {
        if (obj == null) return false;

        if (obj.getClass() != this.getClass()) return false;

        final Vector3D other = (Vector3D) obj;
        if(this.x != other.x) return false;
        if(this.y != other.y) return false;
        if(this.z != other.z) return false;

        return true;
    }
	
	@Override
    public int hashCode() {
        int hash = 3;
        hash = (int)(53 * hash + this.x);
        hash = (int)(53 * hash + this.y);
        hash = (int)(53 * hash + this.z);
        return hash;
    }
    
    @Override
    public String toString() {
        return String.format("Vector3D(%.2f, %.2f, %.2f)", x, y, z);
    }
}