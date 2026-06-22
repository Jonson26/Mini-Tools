import java.awt.Color;

public class Face3D{
	public int a, b, c;
	public Color col;
	
	public Face3D(int a, int b, int c, Color col){
		this.a = a;
		this.b = b;
		this.c = c;
		this.col = col;
	}
	
	public Face3D(Face3D src){
		this(src.a, src.b, src.c, src.col);
	}
}