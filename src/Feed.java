import	lejos.robotics.*;	
import	lejos.robotics.subsumption.*;	
import	lejos.nxt.*;
import lejos.robotics.navigation.DifferentialPilot;
import java.io.File;

public class Feed implements Behavior{
	
	DifferentialPilot robot;
	LightSensor ls;
	//File music = new File("bells.wav");
	boolean suppressed; 
	int generalLight;
	int currentLight;
	Map theMap;
	
	/**
	 * Feed takes in the robot, light sensor, and map in which it moves. 
	 * @param p
	 * @param l
	 * @param map
	 */
	public Feed (LightSensor l, Map map){
		theMap=map;
		ls = l;	
		ls.setFloodlight(Color.WHITE);
		generalLight = ls.readNormalizedValue();
		currentLight = ls.readValue();
		suppressed = true;
		//Thread.sleep(5000);	
		
	}

	/**
	 * takeControl in the Feed behavior checks if there is white below the robot.
	 * @return
	 */
	@Override
	public boolean takeControl() { //this need to be improved, no general light constantly checking the last stored light for the next.
		generalLight = ls.readNormalizedValue();
		currentLight = ls.readValue();
		int change = generalLight - currentLight;
		if(change>=470 && change<=600 && !theMap.goal()){
			suppressed = false;
			return true;
		}else{
			return false;
		}
	}

	@Override
	public void suppress() {
		suppressed = true; 
		//music should stop
	}
	
	@Override
	public void action() {
		if (!suppressed){
			try	{		
				Thread.yield();	
				Thread.sleep(1000);	
			}catch(InterruptedException	ie)	{}	
			robot.stop();
			lejos.nxt.Sound.beepSequenceUp();
			lejos.nxt.Sound.beepSequenceUp();
			lejos.nxt.Sound.beepSequenceUp();
			suppressed = true;	//should i make suppressed = true; or call suppress?
		}
	}

	//@Override
	/*public void stateChanged(SensorPort aSource, int aOldValue, int aNewValue) {
		// TODO Auto-generated method stub
		currentLight = ls.readValue();
		if(generalLight - currentLight > 20){
			suppressed = true;
		}
	}*/

}
