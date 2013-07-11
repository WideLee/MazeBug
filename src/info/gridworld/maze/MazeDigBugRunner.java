package info.gridworld.maze;
import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.grid.Location;
import info.gridworld.grid.UnboundedGrid;
import info.gridworld.actor.Rock;
import java.awt.Color;


public class MazeDigBugRunner
{
    public static void main(String[] args)
    {
        UnboundedGrid<Actor> ugr=new UnboundedGrid<Actor>();
        ActorWorld world = new ActorWorld(ugr); 
        for(int i=0;i<=32;i++){
        	for(int j=0;j<=32;j++){
        		if(j == 0 || j == 32 || i == 0 || i == 32)
        			world.add(new Location(i,j),new Rock(Color.gray));
        		else 
        			world.add(new Location(i,j),new Rock(Color.black));
        	}
        }
        
        
        world.add(new Location(1,1), new MazeDigBug(1, 1));
        world.show();
    }
}