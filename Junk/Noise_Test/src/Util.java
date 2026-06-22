import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;

/* 
Utility class with static methods useful throught the whole codebase.
*/
public class Util{
	public static final int SIDE = 400; //Determines how big the board should be
	public static final double SCALE = 2.0; //Ratio describing how the rendered game image should be scaled (2.0 means it should be 2x bigger)
	
	public static double f(double x){
		return x*x;
	}
	
	public static int min(int a, int b, int c){
		int m=a;
		if(b<m) m=b;
		if(c<m) m=c;
		return m;
	}
	
	public static int min(int a, int b){
		int m=a;
		if(b<m) m=b;
		return m;
	}
	
	public static int max(int a, int b, int c){
		int m=a;
		if(b>m) m=b;
		if(c>m) m=c;
		return m;
	}
	
	public static int max(int a, int b){
		int m=a;
		if(b>m) m=b;
		return m;
	}
}