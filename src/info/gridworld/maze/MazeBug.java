package info.gridworld.maze;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Flower;
import info.gridworld.actor.Rock;
import info.gridworld.grid.*;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;

/**
 * A <code>MazeBug</code> can find its way in a maze. <br />
 * The implementation of this class is testable on the AP CS A and AB exams.
 */
public class MazeBug extends Bug {
	public Location next;
	public boolean isEnd = false;
	public boolean isBack = false;
	public Stack<stackNode> crossLocation = new Stack<stackNode>();
	public Integer stepCount = 0;
	boolean hasShown = false;// final message has been shown

	int[] count = { 0, 0, 0, 0 };

	/**
	 * Constructs a box bug that traces a square of a given side length
	 * 
	 * @param length
	 *            the side length
	 */
	public MazeBug() {
		setColor(Color.GREEN);
	}

	/**
	 * Moves to the next location of the square.
	 */
	public void act() {
		boolean willMove = canMove();
		if (isEnd == true) {
			// to show step count when reach the goal
			if (hasShown == false) {
				String msg = stepCount.toString() + " steps";
				JOptionPane.showMessageDialog(null, msg);
				hasShown = true;
			}
		} else if (willMove) {
			isBack = false;
			move();
			// increase step count when move
			stepCount++;
		} else {
			back();
			stepCount++;
		}
		System.out.println(getLocation());
		System.out.printf("%d %d %d %d\n", count[0], count[1], count[2],
				count[3]);
	}

	/**
	 * 
	 */
	private void back() {
		Location loc = getLocation();
		if (getLocation().equals(crossLocation.peek().getLocation())) {

			ArrayList<Location> locs = getFlowerLocation(getLocation());
			if (locs.size() == crossLocation.peek().getDirections().size() + 1) {
				count[(getDirection() + Location.HALF_CIRCLE) % 360 / 90]--;
				setDirection(crossLocation.peek().getDirections().get(0)
						+ Location.HALF_CIRCLE);
				next = getLocation().getAdjacentLocation(
						crossLocation.peek().getDirections().get(0)
								+ Location.HALF_CIRCLE);
				moveTo(next);
				Flower flower = new Flower(getColor());
				flower.putSelfInGrid(getGrid(), loc);

				crossLocation.pop();
				return;
			}
		}

		if (!isBack) {
			setDirection(getDirection() + Location.HALF_CIRCLE);
		}

		next = getLocation().getAdjacentLocation(getDirection());
		moveTo(next);

		Flower flower = new Flower(getColor());
		flower.putSelfInGrid(getGrid(), loc);
		isBack = true;
	}

	public ArrayList<Location> getFlowerLocation(Location loc) {
		Grid<Actor> grid = getGrid();
		if (grid == null)
			return null;

		ArrayList<Location> valid = new ArrayList<Location>();

		int d = Location.NORTH;
		for (int i = 0; i < Location.FULL_CIRCLE / Location.RIGHT; i++) {
			Location neighborLoc = loc.getAdjacentLocation(d);
			if (grid.isValid(neighborLoc)
					&& (grid.get(neighborLoc) == null || grid.get(neighborLoc) instanceof Flower))
				valid.add(neighborLoc);
			d = d + Location.RIGHT;
		}
		return valid;
	}

	/**
	 * Find all positions that can be move to.
	 * 
	 * @param loc
	 *            the location to detect.
	 * @return List of positions.
	 */
	public ArrayList<Location> getValid(Location loc) {
		Grid<Actor> grid = getGrid();
		if (grid == null)
			return null;

		ArrayList<Location> valid = new ArrayList<Location>();

		int d = Location.NORTH;
		for (int i = 0; i < Location.FULL_CIRCLE / Location.RIGHT; i++) {
			Location neighborLoc = loc.getAdjacentLocation(d);
			if (grid.isValid(neighborLoc) && grid.get(neighborLoc) == null)
				valid.add(neighborLoc);
			d = d + Location.RIGHT;
		}
		return valid;
	}

	/**
	 * Tests whether this bug can move forward into a location that is empty or
	 * contains a flower.
	 * 
	 * @return true if this bug can move.
	 */
	public boolean canMove() {

		ArrayList<Location> locs = getValid(getLocation());

		int d = Location.NORTH;
		for (int i = 0; i < Location.FULL_CIRCLE / Location.RIGHT; i++) {
			Location neighborLoc = getLocation().getAdjacentLocation(d);

			if (getGrid().isValid(neighborLoc)
					&& getGrid().get(neighborLoc) instanceof Rock
					&& getGrid().get(neighborLoc).getColor().equals(Color.RED)) {
				isEnd = true;
				return false;
			}

			d = d + Location.RIGHT;
		}

		if (!locs.isEmpty()) {
			int max = count[getLocation().getDirectionToward(locs.get(0)) / 90];
			int maxIndex = 0;
			for (int i = 1; i < locs.size(); i++) {
				int temp = count[getLocation().getDirectionToward(locs.get(i)) / 90];
				if (max < temp) {
					max = temp;
					maxIndex = i;
				}
			}
			next = locs.get(maxIndex);
			// next = locs.get((int) (Math.random() * locs.size()));
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Moves the bug forward, putting a flower into the location it previously
	 * occupied.
	 */
	public void move() {
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return;
		Location loc = getLocation();
		if (gr.isValid(next)) {

			if (!crossLocation.isEmpty()
					&& getLocation().equals(crossLocation.peek().getLocation())) {

				stackNode temp = crossLocation.pop();
				temp.addDirections(getLocation().getDirectionToward(next));
				temp.setDirection(getLocation().getDirectionToward(next));
				crossLocation.push(temp);
				count[(getDirection() + Location.HALF_CIRCLE) % 360 / 90]--;
				count[getLocation().getDirectionToward(next) / 90]++;
			} else if (getValid(getLocation()).size() > 1
					|| getLocation().getDirectionToward(next) != getDirection()) {

				stackNode temp = new stackNode(getLocation(), getDirection());
				crossLocation.push(temp);
				count[getLocation().getDirectionToward(next) / 90]++;
			}

			setDirection(getLocation().getDirectionToward(next));
			moveTo(next);

		} else {
			removeSelfFromGrid();
		}
		Flower flower = new Flower(getColor());
		flower.putSelfInGrid(getGrid(), loc);
	}
}
