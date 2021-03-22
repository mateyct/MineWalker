import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Represents the mine field
 * @author Stormrunner
 *
 */
@SuppressWarnings("serial")
public class MineFieldPanel extends JPanel {
	
	// create buttons
	private MineFieldButton[][] buttons;
	private final static int FIELDSIZE = 500;
	private RandomWalk randomPath;
	private ArrayList<Point> path;
	private int numButtons;
	private int numMines;
	private Random rand;
	private Point current;
	private int life;
	private int score;
	private boolean won;
	private final Color[] COLORS = {Color.green, Color.yellow, Color.orange, Color.red};
	private Color enabled;
	private Color disabled;
	private MakeSound soundPlayer;
	
	/**
	 * Constructor: create buttons from the MineFieldButton class
	 * @param listener
	 * @param gridSize
	 * @param difficulty
	 */
	public MineFieldPanel (ActionListener listener, int gridSize, int difficulty) {
		// init player
		life = 5;
		score = 500;
		won = false;
		// init buttons
		buttons = new MineFieldButton[gridSize][gridSize];
		setLayout(new GridLayout(buttons.length, buttons[0].length, 0, 0));
		setBackground(null);
		// define path
		randomPath = new RandomWalk(gridSize);
		randomPath.createWalk();
		path = randomPath.getPath();
		// make mines
		numButtons = (buttons.length * buttons[0].length) - path.size(); // get valid button count
		numMines = (int)Math.ceil((double)numButtons / (100.0 / difficulty)); // calculate 25% of buttons
		rand = new Random(); // random generator
		// init current spot
		current = new Point(randomPath.getStartPoint().y, randomPath.getStartPoint().x);
		// init kinds of colors
		enabled = new Color(240, 240, 240);
		disabled = new Color(200, 200, 200);
		// loop through and init
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j] = new MineFieldButton(new Point(i, j));
				buttons[i][j].addActionListener(listener);
				buttons[i][j].setPreferredSize(new Dimension((FIELDSIZE / gridSize), FIELDSIZE / gridSize));
				buttons[i][j].setDisabled(true);
				buttons[i][j].setLayout(new FlowLayout(1));
				add(buttons[i][j]);
			}
		}
		// set first buttons
		buttons[current.x][current.y + 1].setDisabled(false);
		buttons[current.x - 1][current.y].setDisabled(false);
		//add player to current
		buttons[current.x][current.y].setLayout(new BorderLayout());
		buttons[current.x][current.y].setText("\u263A");
		// highlight path
		for (Point p : path) {
			buttons[p.y][p.x].setOnPath(true);
			buttons[p.y][p.x].setMine(false);
		}
		// create mines
		while(numMines > 0) {
			// loop through all buttons to have a chance to be a mine
			for (int i = 0; i < buttons.length; i++) {
				for (int j = 0; j < buttons[i].length; j++) {
					// check with random chance, number of mines, and if button is on path
					if(rand.nextInt(10) == 0 && numMines > 0 && !buttons[i][j].getOnPath() && !buttons[i][j].getMine()) {
						buttons[i][j].setMine(true);
						numMines--;
					}
				}
			}
		}
		// set start and end button colors
		buttons[randomPath.getStartPoint().y][randomPath.getStartPoint().x].setBackground(Color.cyan); // start
		buttons[randomPath.getEndPoint().y][randomPath.getEndPoint().x].setBackground(Color.magenta); // end
		// make sound player
		soundPlayer = new MakeSound();
	}
	/**
	 * Returns field size
	 * @return field size
	 */
	public static int getFieldSize() {
		return FIELDSIZE;
	}
	
	/**
	 * Shows mines if show is true
	 * @param show
	 */
	public void showMines(boolean show) {
		// loop through all buttons to have a chance to be a mine
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				// check if mine
				if(buttons[i][j].getMine()) {
					if(show) {
						// show mines
						buttons[i][j].setColor(Color.black);
					}
					else {
						// hide mines
						if(!buttons[i][j].getExploded())
							buttons[i][j].returnColor();
						if (!won)
							setAdj();
					}
				}
			}
		}
		// set start and end button colors
		buttons[randomPath.getStartPoint().y][randomPath.getStartPoint().x].setBackground(Color.cyan); // start
		buttons[randomPath.getEndPoint().y][randomPath.getEndPoint().x].setBackground(Color.magenta); // end
		setBackground(null);
	}
	/**
	 * Shows path if show is true
	 * @param show
	 */
	public void showPath(boolean show) {
		// loop through all buttons to have a chance to be a mine
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				// check if mine
				if(buttons[i][j].getOnPath()) {
					if(show) {
						// show path
						buttons[i][j].setColor(Color.blue);
					}
					else {
						// hide path
						buttons[i][j].returnColor();
						if (!won) {
							setAdj();
						}
							
					}
				}
			}
		}
		// set start and end button colors
		buttons[randomPath.getStartPoint().y][randomPath.getStartPoint().x].setBackground(Color.cyan); // start
		buttons[randomPath.getEndPoint().y][randomPath.getEndPoint().x].setBackground(Color.magenta); // end
		setBackground(null);
	}
	/**
	 * Moves to the given location or does not if it can't
	 * @param loc
	 */
	public void moveTo(Point loc) {
		if(!buttons[loc.x][loc.y].getMine()) {
			// move player
			buttons[current.x][current.y].setText("");
			buttons[loc.x][loc.y].setLayout(new BorderLayout());
			buttons[loc.x][loc.y].setText("\u263A");
			buttons[loc.x][loc.y].repaint();
			soundPlayer.playSound("step.wav");
			current = loc;
			repaint();
			score--;
			// loop through and disable buttons
			disableAll();
			// check for win
			if (loc.x == randomPath.getEndPoint().y && loc.y == randomPath.getEndPoint().x) {
				// loop through and disable buttons
				disableAll();
				won = true;
				return;
			}
			setAdj();
		}
		else {
			if(!buttons[loc.x][loc.y].getExploded()) {
				// play explosion sound
				soundPlayer.playSound("Explosion.wav");
				// set mine to exploded and visible, decrement life and score
				buttons[loc.x][loc.y].setColor(Color.black);
				life--;
				score-=100;
				buttons[loc.x][loc.y].explode();
				// check for loss
				if (life <= 0) {
					disableAll();
				}
			}
		}
	}
	/**
	 * Returns true if the player has won
	 * @return won
	 */
	public boolean getWon() {
		return won;
	}
	/**
	 * Returns the current score
	 * @return score
	 */
	public int getScore() {
		return score;
	}
	/**
	 * Adds the parameter to the score
	 * @param changeAmount
	 */
	public void changeScore(int changeAmount) {
		score += changeAmount;
	}
	/**
	 * Returns the current amount of life
	 * @return life
	 */
	public int getLife() {
		return life;
	}
	/**
	 * Sets adjacent squares and their colors
	 */
	void setAdj() {
		// check to enable adjacent buttons and check for mines
		int adjMines = 0;
		// check up
		Point loc = current;
		if (loc.x - 1 >= 0) {
			if(!buttons[loc.x - 1][loc.y].getExploded()) {
				buttons[loc.x - 1][loc.y].setDisabled(false);
				// set enabled colors
				if(buttons[loc.x - 1][loc.y].getBackground().equals(disabled)) {
					buttons[loc.x - 1][loc.y].setColor(enabled);
				}
			}
			if(buttons[loc.x - 1][loc.y].getMine()) {
				adjMines++;
			}
		}
		// check down
		if (loc.x + 1 <= buttons.length - 1) {
			if(!buttons[loc.x + 1][loc.y].getExploded()) {
				buttons[loc.x + 1][loc.y].setDisabled(false);
				// set enabled colors
				if(buttons[loc.x + 1][loc.y].getBackground().equals(disabled)) {
					buttons[loc.x + 1][loc.y].setColor(enabled);
				}
			}
			if(buttons[loc.x + 1][loc.y].getMine()) {
				adjMines++;
			}
		}
		// check left
		if (loc.y - 1 >= 0) {
			if(!buttons[loc.x][loc.y - 1].getExploded()) {
				buttons[loc.x][loc.y - 1].setDisabled(false);
				// set enabled colors
				if(buttons[loc.x][loc.y - 1].getBackground().equals(disabled)) {
					buttons[loc.x][loc.y - 1].setColor(enabled);
				}
			}
			if(buttons[loc.x][loc.y - 1].getMine()) {
				adjMines++;
			}
		}
		// check right
		if (loc.y + 1 <= buttons[0].length - 1) {
			if(!buttons[loc.x][loc.y + 1].getExploded()) {
				buttons[loc.x][loc.y + 1].setDisabled(false);
				// set enabled colors
				if(buttons[loc.x][loc.y + 1].getBackground().equals(disabled)) {
					buttons[loc.x][loc.y + 1].setColor(enabled);
				}
			}
			if(buttons[loc.x][loc.y + 1].getMine()) {
				adjMines++;
			}
		}
		buttons[loc.x][loc.y].setColor(COLORS[adjMines], true);
	}
	// disables all buttons
	public void disableAll() {
		for (int i = 0; i < buttons.length; i++) {
			for (int j = 0; j < buttons[i].length; j++) {
				buttons[i][j].setDisabled(true);
				if(buttons[i][j].getBackground().equals(enabled))
					buttons[i][j].setColor(disabled, true);
			}
		}
	}
	/**
     * Action event for the player animation
     */
	private class TimerActionListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
            if (buttons[current.x][current.y].getText().equals("\u263A")) {
            	buttons[current.x][current.y].setText("");
            }
            else {
            	buttons[current.x][current.y].setText("\u263A");
            }
		}
	}

   /**
    * Calls the animation for the player
    */
    public void startAnimation()
    {
	    TimerActionListener taskPerformer = new TimerActionListener();
	    new Timer(500, taskPerformer).start();
    }
}
