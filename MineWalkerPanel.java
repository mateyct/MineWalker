import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
/**
 * Contains the board and other elements
 * @author Mason Tolley
 *
 */
@SuppressWarnings("serial")
public class MineWalkerPanel extends JPanel {

	// init variables
	private MineFieldPanel field;
	private JPanel[] leftLabels;
	private JLabel livesLabel;
	private JLabel scoreLabel;
	private final String[] LABEL_WORDS = {"0 Nearby Mines", "1 Nearby Mine", "2 Nearby Mines", "3 Nearby Mines", "Exploded Mine", "", "Start", "Destination"};
	private final Color[] COLORS = {Color.green, Color.yellow, Color.orange, Color.red, Color.black, new Color(220, 220, 220), Color.cyan, Color.magenta};
	private JPanel leftLabelPanel;
	private JPanel rightLabelPanel;
	private int edgeWidth;
	private JButton mineButton;
	private JButton pathButton;
	private JButton startButton;
	private JPanel buttonPanel;
	private JTextArea sizeInput;
	private JSlider difficultySlider;
	private JLabel difficultyLabel;
	private JPanel difficultyPanel;
	private JPanel placeholder;
	private MakeSound soundPlayer;
	private int difficulty;
	
	/**
	 * Constructor: Organizes panels and labels
	 * @param width
	 * @param height
	 */
	public MineWalkerPanel() {
		// init labels
		leftLabels = new JPanel[8];
		leftLabelPanel = new JPanel();
		rightLabelPanel = new JPanel();
		// init sound player
		soundPlayer = new MakeSound();
		// define right and left panel layouts
		placeholder = new JPanel();
		placeholder.setPreferredSize(new Dimension(MineFieldPanel.getFieldSize(), MineFieldPanel.getFieldSize()));
		rightLabelPanel.setLayout(new GridLayout(2, 1, 1, 1));
		leftLabelPanel.setLayout(new GridLayout(leftLabels.length, 1, 0, 0));
		edgeWidth = 100;
		livesLabel = new JLabel("Lives: 5");
		scoreLabel = new JLabel("Score: 500");
		livesLabel.setPreferredSize(new Dimension(edgeWidth, edgeWidth / 2));
		scoreLabel.setPreferredSize(new Dimension(edgeWidth, edgeWidth / 2));
		// add to right label
		rightLabelPanel.add(livesLabel);
		rightLabelPanel.add(scoreLabel);
		rightLabelPanel.setBackground(new Color(220, 220, 220));
		// loop through and set
		for (int i = 0; i < leftLabels.length; i ++) {
			leftLabels[i] = new JPanel();
			JLabel temp = new JLabel(LABEL_WORDS[i]);
			if(COLORS[i].equals(Color.black)) {
				temp.setForeground(Color.white);
			}	
			leftLabels[i].add(temp, -1);
			leftLabels[i].setPreferredSize(new Dimension(edgeWidth, edgeWidth / 2));
			leftLabels[i].setBackground(COLORS[i]);
			leftLabelPanel.add(leftLabels[i]);
		}
		// init buttons
		mineButton = new JButton("Show Mines");
		mineButton.addActionListener(new ShowMineListener());
		mineButton.setPreferredSize(new Dimension(105, 30));
		mineButton.setEnabled(false);
		pathButton = new JButton("Show Path");
		pathButton.setPreferredSize(new Dimension(105, 30));
		pathButton.addActionListener(new ShowPathListener());
		pathButton.setEnabled(false);
		startButton = new JButton("New Game");
		startButton.setPreferredSize(new Dimension(105, 30));
		startButton.addActionListener(new StartGameListener());
		// set difficulty slider
		difficultyLabel = new JLabel("Difficulty: 25");
		difficultyLabel.setFont(new Font("Arial", 0, 13));
		difficultySlider = new JSlider(10, 100, 25);
		difficultyPanel = new JPanel();
		difficultyPanel.setPreferredSize(new Dimension(200, 50));
		difficultyPanel.add(difficultyLabel);
		difficultySlider.addChangeListener(new SliderListener());
		difficultyPanel.add(difficultySlider);
		
		// init input
		sizeInput = new JTextArea();
		sizeInput.setPreferredSize(new Dimension(edgeWidth / 2, edgeWidth / 4));
		// add buttons, input and slider
		buttonPanel = new JPanel();
		buttonPanel.setBackground(new Color(220, 220, 220));
		buttonPanel.add(mineButton);
		buttonPanel.add(pathButton);
		buttonPanel.add(startButton);
		buttonPanel.add(sizeInput);
		buttonPanel.add(difficultyPanel);
		// Set layout
		setLayout(new FlowLayout(1, 5, 5));
		add(leftLabelPanel);
		add(placeholder);
		// background
		setBackground(new Color(220, 220, 220));
		setPreferredSize(new Dimension(MineFieldPanel.getFieldSize() + (edgeWidth * 2) + 15, MineFieldPanel.getFieldSize() + (edgeWidth / 2)  + 10));
		add(rightLabelPanel);
		add(buttonPanel);
	}
	
	/**
	 * Action listener for field buttons
	 * @author Mason Tolley
	 *
	 */
	private class FieldButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			// get selected button
			MineFieldButton selected = (MineFieldButton) e.getSource();
			if(!selected.getDisabled()) {
				// move the player
				field.moveTo(selected.getLocation());
				// check for win
				if(field.getWon()) {
					// change score display
					scoreLabel.setText("Score: " + field.getScore());
					livesLabel.setText("Life: " + field.getLife());
					field.showMines(true);
					field.showPath(false);
					field.disableAll();
					// disable buttons
					mineButton.setText("Hide Mines");
					mineButton.setEnabled(false);
					pathButton.setText("Show Path");
					pathButton.setEnabled(false);
					soundPlayer.playSound("win.wav");
					JOptionPane.showMessageDialog(field, "You won with " + field.getLife() + " lives and a score of " + field.getScore() + " on " + difficulty + "% difficulty!");
					startButton.setText("New Game");
				}
				// check for loss
				if (field.getLife() <= 0) {
					// change score display
					scoreLabel.setText("Score: " + field.getScore());
					livesLabel.setText("Life: " + field.getLife());
					field.showMines(true);
					field.showPath(false);
					field.disableAll();
					JOptionPane.showMessageDialog(field, "Loser!");
					// disable buttons
					mineButton.setText("Hide Mines");
					mineButton.setEnabled(false);
					pathButton.setText("Show Path");
					pathButton.setEnabled(false);
					startButton.setText("New Game");
				}
			}
			else {
				// pay sound on invalid move
				soundPlayer.playSound("buzz.wav");
			}
			// change score display
			scoreLabel.setText("Score: " + field.getScore());
			livesLabel.setText("Life: " + field.getLife());
		}
	}
	/**
	 * Toggles whether or not to show mines
	 * @author Mason Tolley
	 *
	 */
	public class ShowMineListener implements ActionListener {
		// check which text is being displayed
		@Override
		public void actionPerformed(ActionEvent e) {
			if (mineButton.getText().equals("Show Mines")) {
				mineButton.setText("Hide Mines");
				field.showMines(true);
				// you don't deserve good score
				field.changeScore(-400);
			}
			else {
				mineButton.setText("Show Mines");
				field.showMines(false);
			}
			// change score display
			scoreLabel.setText("Score: " + field.getScore());
			livesLabel.setText("Life: " + field.getLife());
		}
	}
	/**
	 * Toggles whether or not to show mines
	 * @author Mason Tolley
	 *
	 */
	public class ShowPathListener implements ActionListener {
		// check which text is being displayed
		@Override
		public void actionPerformed(ActionEvent e) {
			if (pathButton.getText().equals("Show Path")) {
				pathButton.setText("Hide Path");
				field.showPath(true);
				// you don't deserve good score
				field.changeScore(-400);
			}
			else {
				pathButton.setText("Show Path");
				field.showPath(false);
			}
			// change score display
			scoreLabel.setText("Score: " + field.getScore());
			livesLabel.setText("Life: " + field.getLife());
		}
		
	}
	/**
	 * Starts new game or gives up on old one
	 * @author Mason Tolley
	 *
	 */
	public class StartGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			if(startButton.getText().equals("New Game")) {
				// get text
				int gridSize = 10;
				// catch error for int parse
				try {
					gridSize = Integer.parseInt(sizeInput.getText());
					// check for valid input
					if (gridSize < 2) {
						JOptionPane.showMessageDialog(placeholder, "Please choose a valid integer greater than 1");
						return;
					}
				}
				// catch non-numbers
				catch(NumberFormatException event) {
					gridSize = 10;
					if(!sizeInput.getText().equals(""))
						JOptionPane.showMessageDialog(placeholder, "I was generous this time and set the grid size to 10. Next time, please choose a valid integer greater than 1");
				}
				// remove old field
				if (field != null) {
					remove(field);
				}
				remove(placeholder);
				// init field
				difficulty = difficultySlider.getValue();
				field = new MineFieldPanel(new FieldButtonListener(), gridSize, difficulty);
				add(field, 1);
				// reset buttons
				field.showMines(false);
				field.showPath(false);
				pathButton.setText("Show Path");
				pathButton.setEnabled(true);
				mineButton.setText("Show Mines");
				mineButton.setEnabled(true);
				startButton.setText("Give up?");
				// change score display
				scoreLabel.setText("Score: " + field.getScore());
				livesLabel.setText("Life: " + field.getLife());
				// validate
				validate();
				repaint();
				// player animation
				field.startAnimation();
			}
			else {
				// remove old field
				remove(field);
				add(placeholder, 1);
				// set buttons
				startButton.setText("New Game");
				field.showMines(false);
				field.showPath(false);
				pathButton.setText("Show Path");
				pathButton.setEnabled(false);
				mineButton.setText("Show Mines");
				mineButton.setEnabled(false);
				// change score display
				scoreLabel.setText("Score: " + field.getScore());
				livesLabel.setText("Life: " + field.getLife());
				repaint();
				// make fun of person that gives up
				JOptionPane.showMessageDialog(placeholder, "You give up? Wimp...");
			}
		}
	}
	/**
	 * Updates the difficulty display
	 * @author Mason Tolley
	 *
	 */
	private class SliderListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			// change slider display on change
			difficultyLabel.setText("Difficulty: " + difficultySlider.getValue());
		}
		
	}
}
