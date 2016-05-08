package snake;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

// You won't need to modify this class.
public class SnakeAIFrame extends JFrame {
	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				SnakeAIFrame frame = new SnakeAIFrame();
				frame.add(new SnakeAIPanel());
				frame.pack();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setTitle("Snake AI");
				frame.setVisible(true);
			}
		});
	}
}
