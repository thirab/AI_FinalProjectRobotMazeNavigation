import lejos.nxt.LightSensor;
import lejos.robotics.Color;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.subsumption.Behavior;


public class BorderCross implements Behavior {

	DifferentialPilot robot;
	Double wanderDistance=(double) 5;
	LightSensor ls;
	int generalLight;
	int currentLight;
	boolean suppressed; 
	Map map;


	public BorderCross(DifferentialPilot p, LightSensor l, Map m) {
		robot = p;
		ls = l;	
		ls.setFloodlight(Color.WHITE);
		generalLight = ls.readNormalizedValue();
		currentLight = ls.readValue();
		suppressed = true;
		map = m;

	}

	@Override
	public boolean takeControl() {
		generalLight = ls.readNormalizedValue();
		currentLight = ls.readValue();
		int change = generalLight - currentLight;
		System.out.println("The light change is" + " " + change);
		
		//TODO if blue?
		if(change>=400 && change<=550){
			suppressed = false;
			System.out.println("i CALLED FOR CONTROL!");
			return true;
		}else{
			return false;
		}

	}

	@Override
	public void action() {
		if (!suppressed){
			try	{		
				Thread.yield();	
				Thread.sleep(1000);	
			}catch(InterruptedException	ie)	{}	
			map.forward();
			robot.travel(wanderDistance);
		}
	}

	@Override
	public void suppress() {
		suppressed = true; 
		//music should stop
	}

}
