import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;

/* 
Utility class with static methods useful throught the whole codebase.
*/
public class Util{
	public static final int SIDE = 900; //Determines how big the board should be
	public static final double SCALE = 1.0; //Ratio describing how the rendered game image should be scaled (2.0 means it should be 2x bigger)
	
	public static double f(double x){
		return x*x;
	}
	
	public static Object3D loadObject3DFromFile(String path){
		Object3D out = new Object3D();
		int temp = 255;
		Color[] cols = {
			// Color.RED,
			// Color.GREEN,
			// Color.BLUE,
			// Color.YELLOW,
			// Color.MAGENTA,
			// Color.CYAN,
			// Color.ORANGE,
			// Color.PINK,
			// Color.LIGHT_GRAY,
			// Color.GRAY,
			// Color.BLACK
			// new Color(0, 255, 0),
			// new Color(0, 245, 0),
			// new Color(0, 235, 0),
			//new Color(0, 191, 0),
			//new Color(0, 127, 0),
			//new Color(0,  63, 0)
			// new Color(63, 63, 63),
			// new Color(83, 83, 83),
			// new Color(103, 103, 103),
			// Color.WHITE,
			// Color.WHITE,
			// Color.WHITE,
			new Color(temp, temp, temp-=10),
			new Color(temp, temp, temp-=10),
			new Color(temp, temp, temp-=10),
			new Color(temp, temp, temp-=10),
			new Color(temp, temp, temp-=10),
			new Color(temp, temp, temp-=10),
			// new Color(temp-=10, 0, 0),
		};
		int colorIndex = 0;
		try{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			String currentLine = reader.readLine();
			while(currentLine != null){
				String[] splitLine = currentLine.trim().split("\\s+");
				switch(splitLine[0]){
					case "v":
						double a = Double.parseDouble(splitLine[1]);
						double b = Double.parseDouble(splitLine[2]);
						double c = Double.parseDouble(splitLine[3]);
						double w = 1.0;
						//if(splitLine.length>4 && splitLine[4]!="#") w = Double.parseDouble(splitLine[4]);
						out.addPoint(new Vector3D(a*w, b*w, c*w));
						break;
					case "f":
						String[] a_raw = splitLine[1].split("/");
						String[] b_raw = splitLine[2].split("/");
						String[] c_raw = splitLine[3].split("/");
						int a_index = Integer.parseInt(a_raw[0]);
						int b_index = Integer.parseInt(b_raw[0]);
						int c_index = Integer.parseInt(c_raw[0]);
						out.addFace(a_index-1, b_index-1, c_index-1, cols[colorIndex%cols.length]);
						colorIndex++;
						break;
					default:
						break;
				}
				currentLine = reader.readLine();
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return out;
	}
	
	// public static int sign (int p1_x, int p1_y, int p2_x, int p2_y, int p3_x, int p3_y)
	// {
		// return (p1_x - p3_x) * (p2_y - p3_y) - (p2_x - p3_x) * (p1_y - p3_y);
	// }

	// public static boolean pointInTriangle(int p_x, int p_y, int a_x, int a_y, int b_x, int b_y, int c_x, int c_y){
		// int d1, d2, d3;
		// boolean has_neg, has_pos;

		// d1 = sign(p_x, p_y, a_x, a_y, b_x, b_y);
		// d2 = sign(p_x, p_y, b_x, b_y, c_x, c_y);
		// d3 = sign(p_x, p_y, c_x, c_y, a_x, a_y);

		// has_neg = (d1 < 0) || (d2 < 0) || (d3 < 0);
		// has_pos = (d1 > 0) || (d2 > 0) || (d3 > 0);

		// return !(has_neg && has_pos);
	// }
	
	
	public static boolean pointInTriangle(int p_x, int p_y, int a_x, int a_y, int b_x, int b_y, int c_x, int c_y){
		double triangleArea = (a_y - c_y) * (b_x - c_x) + (b_y - c_y) * (c_x - a_x) * 1.0;
		double b1 = ((p_y - c_y) * (b_x - c_x) + (b_y - c_y) * (c_x - p_x)) / triangleArea;
		double b2 = ((p_y - a_y) * (c_x - a_x) + (c_y - a_y) * (a_x - p_x)) / triangleArea;
		double b3 =((p_y - b_y) * (a_x - b_x) + (a_y - b_y) * (b_x - p_x)) / triangleArea;
		
		return (b1 >= 0 && b1 <= 1 && b2 >= 0 && b2 <= 1 && b3 >= 0 && b3 <= 1);
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