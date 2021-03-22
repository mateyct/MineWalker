import java.awt.Color;
import java.awt.Point;

import javax.swing.JButton;

/**
 * Represents the grid squares on the mine field
 * @author Stormrunner
 *
 */
@SuppressWarnings("serial")
public class MineFieldButton extends JButton {
	// init variables
	private boolean isMine;
	private boolean isExploded;
	private boolean onPath;
	private Point location;
	private Color lastColor;
	private boolean disabled;
	/**
	 * Constructor: set mine so it is not exploded
	 */
	public MineFieldButton (Point loc) {
		isExploded = false;
		location = loc;
		lastColor = new Color(200, 200, 200);
		setBackground(lastColor);
	}
	/**
	 * Set the color of the button
	 * @param color
	 */
	public void setColor(Color color) {
		lastColor = new Color(getBackground().getRed(), getBackground().getGreen(), getBackground().getBlue());
		setBackground(color);
	}
	/**
	 * Saves the current color instead of the last color
	 * @param color
	 * @param save
	 */
	public void setColor(Color color, boolean save) {
		lastColor = color;
		setBackground(color);
	}
	/**
	 * Set color back to what it was before setColor was called
	 */
	public void returnColor() {
		setBackground(lastColor);
	}
	/**
	 * Sets the if button is a mine
	 * @param isOrIsNot
	 */
	public void setMine(boolean isOrIsNot) {
		isMine = isOrIsNot;
		if (!isOrIsNot) {
			isExploded = false;
		}
	}
	/**
	 * Returns true if the button is a mine
	 * @return buttons mine status
	 */
	public boolean getMine() {
		return isMine;
	}
	 /**
	  * Sets the button to exploded
	  */
	public void explode() {
		if(isMine)
			isExploded = true;
			setDisabled(true);
	}
	/**
	 * Returns true if the mine has exploded
	 * @return if mine exploded
	 */
	public boolean getExploded() {
		return isExploded;
	}
	/**
	 * Returns true if the mine is on the path
	 * @return mine's path status
	 */
	public boolean getOnPath() {
		return onPath;
	}
	
	/**
	 * Sets if the mine is on the path
	 * @param set
	 */
	public void setOnPath(boolean set) {
		onPath = set;
	}
	
	/**
	 * Returns the location of the button
	 * @return location
	 */
	public Point getLocation() {
		return location;
	}
	/**
	 * Sets to disabled if true
	 * @param disable
	 */
	public void setDisabled(boolean disable) {
		disabled = disable;
	}
	/**
	 * Returns true if the buttons is disabled
	 * @return disabled
	 */
	public boolean getDisabled() {
		return disabled;
	}
}
