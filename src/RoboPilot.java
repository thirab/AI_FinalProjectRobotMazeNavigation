
import lejos.nxt.*;
import	lejos.robotics.*;
import	lejos.robotics.subsumption.*;
import lejos.robotics.navigation.DifferentialPilot;

//TODO: finish adding listeners to behavior classes. add the buttons for terminating programs 
//maybe add the sound behavior when everything works
//light calibration (realized she wanted us to use the lightsensor so i changed it back)
//test tha baby bot

public class RoboPilot {

		private double trackWidth =11; //size of base, centimeters
		private double wheelDiameter = 5.6; //size of wheel diameter, centimeters
		private DifferentialPilot nav; //Pilot to guide robot's rotating and traversing	
		private double cellDistance = 30; // distance to travel between cells
		private Map map; // the map that the pilot navigates by
		
		
		//TouchSensor	tSensor	=	new	TouchSensor(SensorPort.S1);	
		UltrasonicSensor us;
		//light sensor to sense the goal state
		ColorSensor cs;	
		SoundSensor ss;

		Behavior b1;
		Behavior b2;
		Behavior b3;
		Behavior b4;
		Behavior[] bArray; 
		Arbitrator arby;
		
		//TODO I removed the code from behaviors to map so that the pilot doesn't need repeated code however I am wondering if
		//it will cause issues with Thread etc.
		//if it does I can port back robot movements and keep map development it was simply to move it all to a main area for debugging and clarity. 
		
		//let me know what you think.
		//I am unsure wether or not having nav away from the behaviors will cause problems.
		
		public RoboPilot () throws InterruptedException{
			nav = new DifferentialPilot (wheelDiameter, trackWidth, Motor.C, Motor.A);
			us = new UltrasonicSensor(SensorPort.S4);
			cs = new ColorSensor(SensorPort.S1);
			ss = new SoundSensor(SensorPort.S2);
			cs.setFloodlight(Color.WHITE);
			initializePilot();
			
			map = new Map(nav, cellDistance);
			
			b1 = new Wander(map);
			b2 = new Avoid(us,map);
			
			//TODO test sound sensor it currently creates a barrier wall (implying that the robot is heading in the wrong direction)
			//This can easily be altered to turns etc... 
			b4 = new Sound(ss,map);
			b3 = new Scope(nav,cs,map);
			bArray = new Behavior[] {b1,b4,b2,b3};
			arby = new Arbitrator(bArray);
		}
		
		/**
		 * initializePilot set's the initial DifferentialPilot settings: travel speed and rotation speed
		 */
		public void initializePilot(){	
			nav.reset(); //Reset tachoCount on both motors
			nav.setTravelSpeed(10);//Set travel and rotate speed
			nav.setRotateSpeed(30);
		}
		
		/**
		 * waitRobot instructs the robot to wait until it's start button is pressed to begin any action(s)
		 */
		public void waitRobot(){
			Button.ENTER.waitForPressAndRelease();
			arby.start();
		}
}
