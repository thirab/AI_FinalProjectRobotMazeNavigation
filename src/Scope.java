import java.io.File;

import lejos.nxt.ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;

/**
 * 
 */

/**
 * @author Jackie and Tai
 *
 */
public class Scope implements Behavior{
	
	DifferentialPilot robot; //pilot for navigation
	ColorSensor cs; //lightsensor
	File music = new File("chopstick.wav"); //music file to play when eating
	boolean suppressed; //is the method supressed
	long wait; //used in action, for waiting 3 seconds
	boolean crossing; //is the crossing tape
	int colorID; 
	Map map;
	double cellDistance;
	
	public Scope (DifferentialPilot p, ColorSensor l, Map m){

		robot = p;
		cs = l;	
		map = m;
		cs.setFloodlight(Color.WHITE);
		suppressed = true; //initially surpressed
		crossing = false; //initially no eating
		colorID = 0; 
	}

	/**
	 * The robot feeds when a white spot is found below it's sensor.
	 */
	@Override
	public boolean takeControl() { 
		//if not in the middle of crossing, and new blue tape detected proceed
		if(!crossing && cs.getColorID() == 2){ //Color.BLUE == 2
				colorID = cs.getColorID();
				suppressed = false; //set suppressed check to false
				return true; //take control! time to feed
		}else if(!crossing && cs.getColorID() == 6){ //Color.WHITE = 6
			suppressed = false; //set suppressed check to false
			colorID = cs.getColorID();
			return true; //
		}else{
			return false; //supress
		}
	}

	@Override
	public void suppress() {
		suppressed = true; 
		robot.stop(); //robot 
	}
	/**
	 * Scope behavior responds depending on the color found- border or goal
	 */
	@Override
	public void action() {
		//set eating to true so arbitrator cannot call takeControl again on food source
		crossing = true;
		if(colorID == 2){
			//robot.travel(5); //need to change this
			while(colorID == 2){ //till he crosses line
				robot.forward(); 
			}
			map.forward();
			robot.stop();
		}
		else if(colorID == 6){
			robot.travel(6);
			while(robot.isMoving()){ //wait for 3 seconds
				lejos.nxt.Sound.playSample(music);
			}
			lejos.nxt.Sound.beepSequenceUp();
			map.MazeWon();
			suppressed = true;	//suppress is true
			crossing = false;	// no longer eating, set to false
		}
	}
}
