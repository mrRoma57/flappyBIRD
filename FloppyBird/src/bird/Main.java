package bird;
import javax.swing.*;
public class Main {
  public static void main(String[] args) throws Exception {
	  int boardWidth = 460;
	  int boardHeight = 640;
	  
	  JFrame frame = new JFrame("Floppy Bird!");
	  frame.setSize(boardWidth , boardHeight);
	  frame.setLocationRelativeTo(null);
	  frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);
	  frame.setResizable(false);
	  
	  FloppyBird bird = new FloppyBird();
	  frame.add(bird);
	  frame.pack();
	  bird.requestFocus();
	  frame.setVisible(true);

  }
}
