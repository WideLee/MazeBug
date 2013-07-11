package info.gridworld.maze;

import java.util.ArrayList;

import info.gridworld.grid.Location;

public class stackNode{
	private ArrayList<Integer> directions;
	private Location location;
	private int direction;
	
	/**
	 * 
	 */
	public stackNode(Location loc, int dir) {
		location = loc;
		direction = dir;
		directions = new ArrayList<Integer>();
		directions.add(dir);
	}
	
	public Location getLocation(){
		return location;
	}
	
	public int getDirection(){
		return direction;
	}
	
	public ArrayList<Integer> getDirections(){
		return directions;
	}
	
	public void addDirections(int dir){
		directions.add(dir);
	}
	
	public void setDirection(int dir){
		direction = dir;
	}
}