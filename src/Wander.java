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

	public Wander(Map m,DifferentialPilot r ) {
		map = m;
		robot = r;
	}

	@Override
	public boolean takeControl() {
		// if all sensors are clear, take control
		if (map.isPossible() && !map.goal()) {
			return true;
		}
		// TODO shut down if map is impossible
		return false;
	}

	@Override
	public void action() {
		
		if(!map.forwardsIsChecked()){
		robot.travel(5);
		map.forwardCell().check();
		}
		try {
			Thread.yield();
			Thread.sleep(2000);
		} catch (InterruptedException ie) {
		}
		robot.travel(-5);
		map.wander();
		//robot.travel(wanderDistance);
		map.forward();
		
	}
	@Override
	public void suppress() {
		// update coordinates? stop.
		map.stop();
	}

}
