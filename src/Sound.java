import lejos.nxt.SoundSensor;
import lejos.robotics.subsumption.Behavior;


public class Sound implements Behavior{
	
	private Map map;
	private SoundSensor s;
	boolean supressed;

	public Sound(SoundSensor ss, Map m){
		s=ss;
		map=m;
	}
	@Override
	public boolean takeControl() {
		int soundLevel = s.readValue();
		System.out.println("This is my sound guys: " + soundLevel);
		if(soundLevel >50 && !map.goal()){
			return true;
		}
		return false;
	}
	@Override
	public void action() {
		supressed=false;
		map.SoundHeard();
		if(supressed){
			return;
		}
		// TODO Auto-generated method stub
		
	}
	@Override
	public void suppress() {
		supressed = true;
		map.stop();
		// TODO Auto-generated method stub
		
	}
	
}
