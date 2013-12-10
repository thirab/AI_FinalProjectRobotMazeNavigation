import java.io.File;

import lejos.nxt.ColorSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;


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
		if(!crossing && cs.getColorID() == 2 && !map.goal()){ //Color.BLUE == 2
				colorID = cs.getColorID();
				suppressed = false; //set suppressed check to false
				return true; //take control! time to feed
		}
		
		else if(!crossing && cs.getColorID() == 7){ //Color.BLACK = 7
			suppressed = false; //set suppressed check to false
			colorID = cs.getColorID();
			return true; //
		}
		else{
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
		
		//TODO this is not being called again after it is called once
		//set eating to true so arbitrator cannot call takeControl again on food source
		crossing = true;
		if(colorID == 2){
			System.out.println("Color is blue: " + cs.getColorID());
			robot.travel(5); //need to change this
//			while(colorID == 2){ //till he crosses line
//				robot.forward(); 
//			}
			map.forward();
			robot.stop();
		}
		else if(colorID == 7){
			System.out.println("Color is BLACK: " + cs.getColorID());
			robot.travel(6);
			while(robot.isMoving()){ //wait for 3 seconds
				lejos.nxt.Sound.playSample(music);
			}
			lejos.nxt.Sound.beepSequenceUp();
			//TODO there may be issues with the fact that map return assumes that the robot is in the center of the cell.
			map.mazeWon();
			suppressed = true;	//suppress is true
			crossing = true;	// no longer eating, set to true
		}
	}
}
