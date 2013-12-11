import java.io.File;

import	lejos.robotics.*;	
import	lejos.robotics.subsumption.*;	
import	lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
/**
 * 
 * @author tai-lanhirabayashi & Jackie
 *
 */
public class Avoid implements Behavior{

	UltrasonicSensor us;
	int blockInRange;
	Map map;

	/**
	 * Avoid avoids obstacles
	 * @param u the ultrasonic sensor that senses obstacles
	 * @param m the world map containing the navigator
	 */
	public Avoid (UltrasonicSensor u, Map m){
		
		us = u;	
		blockInRange = 30; //block in 1 ft
		map=m;
	}
	/**
	 * Avoid takes control when an obstacle within 1ft of the robot is sensed.
	 */
	@Override
	public boolean takeControl() {
		//System.out.println("Avoid is trying to take control, current distance is" + " "+ us.getDistance());
		if (us.getDistance() <= blockInRange && !map.goal()){
			System.out.println("Avoiding");
			//System.out.println("The distance from the wall is" + us.getDistance());
			return true;
		}else{
			return false;
		}	

	}
	/**
	 * Avoid acts to notify the map world that an obstacle has been found
	 */
	@Override
	public void action() {
		//doesn't need to move, just report
		//robot.travel(-30,true); // travel backwards 30 cm
		//while(robot.isMoving()){} //do nothing until he is done backing up
		//the robot re-orients to a position where the robot is not facing an obstacle by turning to the best direction.
		map.obstacleFound();
		try	{		
			Thread.yield();	
			Thread.sleep(1000);		//	Stops	for	a	short	time	(one	second)		
		}catch(InterruptedException	ie)	{}		
	}

	@Override
	public void suppress() {
		// do nothing, wait for next obstacle
		map.stop();
	}
}
