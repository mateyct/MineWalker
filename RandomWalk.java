import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

/**
 * 
 */

/**
 * @author mason.tolley
 * This represents a randomly generated path from one point on a grid to another
 */
public class RandomWalk implements RandomWalkInterface {
	
	private int gridSize;
	private Random rand;
	private boolean done;
	private ArrayList<Point> path;
	private Point start;
	private Point end;
	private Point current;
	
	/**
	 * Creates a RandomWalk with no random seed
	 * @param gridSize
	 */
	public RandomWalk(int gridSize) {
		this.gridSize = gridSize;
		rand = new Random();
		path = new ArrayList<Point>();
		start = new Point(0, gridSize - 1);
		end = new Point(gridSize - 1, 0);
		current = new Point(start.x, start.y);
		path.add(start);
	}
	
	/**
	 * Creates a RandomWalk with a seed
	 * @param gridSize
	 * @param seed
	 */
	public RandomWalk(int gridSize, long seed) {
		this.gridSize = gridSize;
		rand = new Random(seed);
		path = new ArrayList<Point>();
		start = new Point(0, gridSize - 1);
		end = new Point(gridSize - 1, 0);
		current = new Point(start.x, start.y);
		path.add(start);
	}
	
	@Override
	public void step() {
		Point nextPoint = new Point();
		if (gridSize != 1) {
			while (true) {
				boolean goNorth = rand.nextBoolean();
				if (goNorth && current.y > 0) {
					nextPoint.x = current.x;
					nextPoint.y = current.y - 1;
					break;
				}
				else if(!goNorth && current.x < gridSize - 1) {
					nextPoint.x = current.x + 1;
					nextPoint.y = current.y;
					break;
				}
			}
		}
		if (nextPoint.x == gridSize - 1 && nextPoint.y == 0) {
			done = true;
		}
		current = new Point(nextPoint.x, nextPoint.y);
		path.add(nextPoint);
	}
	
	@Override
	public void stepEC() {
		Point nextPoint = new Point();
		if (gridSize != 1) {
			while (true) {
				int direction = rand.nextInt(100) + 1;
				if (direction >= 1 && direction <= 40 && current.y > 0) {
					nextPoint.x = current.x;
					nextPoint.y = current.y - 1;
					if(!path.contains(nextPoint)) {
						break;
					}
				}
				else if(direction >= 41 && direction <= 80 && current.x < gridSize - 1) {
					nextPoint.x = current.x + 1;
					nextPoint.y = current.y;
					if(!path.contains(nextPoint)) {
						break;
					}
				}
				else if(direction >= 81 && direction <= 90 && current.x > 0) {
					nextPoint.x = current.x - 1;
					nextPoint.y = current.y;
					if(!path.contains(nextPoint)) {
						break;
					}
				}
				else if(direction >= 91 && direction <= 100 && current.y < gridSize - 1) {
					nextPoint.x = current.x;
					nextPoint.y = current.y + 1;
					if(!path.contains(nextPoint)) {
						break;
					}
				}
				checkIfStuck();
			}
		}
		if (nextPoint.x == gridSize - 1 && nextPoint.y == 0) {
			done = true;
		}
		current = new Point(nextPoint.x, nextPoint.y);
		path.add(nextPoint);
	}
	
	private void checkIfStuck() {
		boolean cantUp = false;
		boolean cantRight = false;
		boolean cantDown = false;
		boolean cantLeft = false;
		Point up = new Point(current.x, current.y - 1);
		if(path.contains(up) || current.y == 0) {
			cantUp = true;
		}
		Point right = new Point(current.x + 1, current.y);
		if(path.contains(right) || current.x == gridSize - 1) {
			cantRight = true;
		}
		Point down = new Point(current.x, current.y + 1);
		if(path.contains(down) || current.y == gridSize - 1) {
			cantDown = true;
		}
		Point left = new Point(current.x - 1, current.y);
		if(path.contains(left) || current.x == 0) {
			cantLeft = true;
		}
		if (cantUp && cantRight && cantLeft && cantDown) {
			path = new ArrayList<Point>();
			path.add(start);
			current = new Point(start.x, start.y);
		}
	}
	
	@Override
	public void createWalk() {
		while (!done) {
			step();
		}
	}

	@Override
	public void createWalkEC() {
		while (!done) {
			stepEC();
		}
	}

	@Override
	public boolean isDone() {
		return done;
	}
	
	@Override
	public int getGridSize() {
		return gridSize;
	}

	@Override
	public Point getStartPoint() {
		return start;
	}

	@Override
	public Point getEndPoint() {
		return end;
	}

	@Override
	public Point getCurrentPoint() {
		return current;
	}

	@Override
	public ArrayList<Point> getPath() {
		return path;
	}

	public String toString () {
		String result = "";
		for (Point point : path) {
			result += "[" + point.x + ", " + point.y + "] ";
		}
		return result;
	}
}
