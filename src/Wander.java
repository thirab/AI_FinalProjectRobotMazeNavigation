import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/**
 * 
 * @author tai-lanhirabayashi & Jackie
 *
 */
public class Wander implements Behavior {

	DifferentialPilot robot;
	double wanderDistance = 23;
	Map map;
	UltrasonicSensor us;
	boolean wandering;

	public Wander(UltrasonicSensor u, Map m,DifferentialPilot r ) {
		map = m;
		robot = r;
		us = u;
		wandering = false;
	}

	@Override
	public boolean takeControl() {
		// if all sensors are clear, take control
		if (!wandering && map.isPossible() && !map.goal()&& !map.checkMoving()) {
			return true;
		}
		// TODO shut down if map is impossible
		return false;
	}

	@Override
	public void action() {
		
		wandering = true;
		robot.travel(2);
		try {
			Thread.yield();
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
		}
		map.moving();
		map.wander();
		//if(!map.isBlocked){
		System.out.println("I'm calling forward!");
			//moved actual movement to the map.
			//robot.travel(wanderDistance);
			//map.setCheck(false);
		//}
		wandering = false;
		map.stopMoving();
		try {
			Thread.yield();
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
		}
		
	}
	@Override
	public void suppress() {
		// update coordinates? stop.
		map.stop();
		wandering = false; 
	}

}
