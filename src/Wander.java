import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

public class Wander implements Behavior {

	DifferentialPilot robot;
	int upperThresh;
	int lowerThresh;
	double cellDistance;
	Map map;

	public Wander(Map m) {
		upperThresh = 90;
		lowerThresh = 10;
		map = m;
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
		try {
			Thread.yield();
			Thread.sleep(1000);
		} catch (InterruptedException ie) {
		}
		map.wander();
	}

	//
	// public void findValidDirection(){
	// if(map.rightIsValid()){
	// map.turnRight();
	// robot.rotate(90);
	// map.forward();
	// robot.travel(cellDistance);
	// }else if(map.leftIsValid()){
	// map.turnLeft();
	// robot.rotate(-90);
	// map.forward();
	// robot.travel(cellDistance);
	// }else if(map.backIsValid()){
	// map.turnRight();
	// robot.rotate(90);
	// map.turnRight();
	// robot.rotate(90);
	// map.forward();
	// robot.travel(cellDistance);
	// }else{
	// map.impossible();
	// }
	// }

	

	@Override
	public void suppress() {
		// update coordinates? stop.
		map.stop();
	}

}
