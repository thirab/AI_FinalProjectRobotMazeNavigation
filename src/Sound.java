import lejos.nxt.SoundSensor;
import lejos.robotics.subsumption.Behavior;


public class Sound implements Behavior{
	
	private Map map;
	private SoundSensor s;

	public Sound(SoundSensor ss, Map m){
		s=ss;
		map=m;
	}
	@Override
	public boolean takeControl() {
		int soundLevel = s.readValue();
		if(soundLevel >50 && !map.goal()){
			return true;
		}
		return false;
	}
	@Override
	public void action() {
		map.SoundHeard();
		// TODO Auto-generated method stub
		
	}
	@Override
	public void suppress() {
		// TODO Auto-generated method stub
		
	}
	
}
