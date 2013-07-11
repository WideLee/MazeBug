package info.gridworld.maze;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Stack;

import javax.swing.JOptionPane;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

public class MazeDigBug extends Bug {

	Stack<stackNode> crossLocation = new Stack<stackNode>();
	public boolean isBegin = false;
	int count;
	public Location next;
	public boolean isEnd = false;
	public boolean isBack = false;
	boolean hasShown = false;// final message has been shown
	public Location initLocation;

	public MazeDigBug(int row, int col) {
		setColor(Color.PINK);
		count = 0;
		initLocation = new Location(row, col);
	}

	/**
	 * Moves to the next location of the square.
	 */
	public void act() {

		boolean willMove = canMove();
		isBegin = true;

		if (count == 0) {
			count++;
			if (isEnd == true) {
				// to show step count when reach the goal
				if (hasShown == false) {
					String msg = "Done";
					JOptionPane.showMessageDialog(null, msg);
					hasShown = true;
					isBegin = false;
				}
				count--;
			} else if (willMove) {
				isBack = false;
				move();
			} else {
				back();
			}
		} else {
			count = 0;
			actTwice();
		}
	}

	public void actTwice() {
		next = getLocation().getAdjacentLocation(getDirection());
		moveTo(next);
	}

	public int getCount() {
		return count;
	}

	/**
	 * 
	 */
	private void back() {
		if (getLocation().equals(crossLocation.peek().getLocation())) {

			ArrayList<Location> locs = getValidLocation(getLocation());
			if (locs.size() == crossLocation.peek().getDirections().size() + 1) {
				setDirection(crossLocation.peek().getDirections().get(0)
						+ Location.HALF_CIRCLE);
				next = getLocation().getAdjacentLocation(
						crossLocation.peek().getDirections().get(0)
								+ Location.HALF_CIRCLE);
				moveTo(next);

				crossLocation.pop();
				return;
			}
		}

		if (!isBack) {
			setDirection(getDirection() + Location.HALF_CIRCLE);
		}

		next = getLocation().getAdjacentLocation(getDirection());
		moveTo(next);
		isBack = true;
	}

	public ArrayList<Location> getValidLocation(Location loc) {
		Grid<Actor> grid = getGrid();
		if (grid == null)
			return null;

		ArrayList<Location> valid = new ArrayList<Location>();

		int d = Location.NORTH;
		for (int i = 0; i < Location.FULL_CIRCLE / Location.RIGHT; i++) {
			Location neighborLoc = loc.getAdjacentLocation(d);
			if (!(grid.get(neighborLoc) instanceof Rock && grid.get(neighborLoc
					.getAdjacentLocation(d)) == null))
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
	public ArrayList<Location> getNoneEmptyLocation(Location loc) {
		Grid<Actor> grid = getGrid();
		if (grid == null)
			return null;

		ArrayList<Location> valid = new ArrayList<Location>();

		int d = Location.NORTH;
		for (int i = 0; i < Location.FULL_CIRCLE / Location.RIGHT; i++) {
			Location neighborLoc = loc.getAdjacentLocation(d);
			if (grid.get(neighborLoc.getAdjacentLocation(d)) instanceof Rock
					&& grid.get(neighborLoc.getAdjacentLocation(d)).getColor()
							.equals(Color.BLACK))
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

		ArrayList<Location> locs = getNoneEmptyLocation(getLocation());

		if (getLocation().equals(initLocation) && isBegin) {
			isEnd = true;
			return false;
		}

		if (!locs.isEmpty()) {
			next = locs.get((int) (Math.random() * locs.size()));
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
		if (gr.isValid(next)) {

			if (!crossLocation.isEmpty()
					&& getLocation().equals(crossLocation.peek().getLocation())) {

				stackNode temp = crossLocation.pop();
				temp.addDirections(getLocation().getDirectionToward(next));
				temp.setDirection(getLocation().getDirectionToward(next));
				crossLocation.push(temp);

			} else if (getNoneEmptyLocation(getLocation()).size() > 1
					|| getLocation().getDirectionToward(next) != getDirection()) {

				stackNode temp = new stackNode(getLocation(), getDirection());
				crossLocation.push(temp);
			}

			setDirection(getLocation().getDirectionToward(next));
			moveTo(next);

		} else {
			removeSelfFromGrid();
		}
	}

}
