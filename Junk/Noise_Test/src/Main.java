import javax.swing.JFrame;

/*
Main class.
Not much is actually happening here, aside from initializing the game window and starting the game loop.
*/
public class Main{
	public static void main(String[] args){
		JFrame f = new JFrame("App Template");
		f.setSize(1000, 1000);
		f.setResizable(false);
		
		f.add(new View());
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
}