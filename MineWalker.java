import javax.swing.JFrame;

/**
 * Represents a minewalker game where the player navigates a grid
 * @author Mason Tolley
 *
 */
public class MineWalker {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Mine Walker");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(new MineWalkerPanel());
		frame.setResizable(false);
		frame.pack();
		frame.setVisible(true);
	}
}
