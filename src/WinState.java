import	lejos.robotics.*;	
import	lejos.robotics.subsumption.*;	
import	lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import java.io.File;
import java.util.Stack;

public class WinState implements Behavior {
	DifferentialPilot robot;
	LightSensor ls;
	int generalLight;
	int currentLight;
	boolean suppressed; 
	Map map;
	double cellDistance;

/**
 * WinState
 * @param l the lightsensor attachment in Lejos
 * @param m the Map object which consists of the robot world, and contains the robot.
 */
	public WinState(LightSensor l, Map m) {
		ls = l;	
		ls.setFloodlight(Color.WHITE);
		generalLight = ls.readNormalizedValue();
		currentLight = ls.readValue();
		suppressed = false;
		map = m;

	}

	/**
	 * WinState takes control when a white surface is sensed.
	 */
	@Override
	public boolean takeControl() {
		generalLight = ls.readNormalizedValue();
		currentLight = ls.readValue();
		int change = generalLight - currentLight;
		System.out.println("The light change is" + " " + change);
		if(change>=400 && change<=550 && !suppressed){
			return true;
		}else{
			return false;
		}

	}

	/**
	 * WinState takes control when white light is sensed and declares that the maze has been won, the goal state (white) has been reached. 
	 * The navigation back to to home begins.
	 */
	@Override
	public void action() {
			map.MazeWon();
			suppressed = true;
	}

	@Override
	public void suppress() {
		map.stop();
	}

}
