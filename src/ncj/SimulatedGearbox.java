package ncj;


public class SimulatedGearbox extends FakeGearbox {
	MovSim _simulator = new MovSim();
	private boolean _hit;
	
	public void next() {
		//I'm not sure this handles turning correctly...
		_simulator.futurePos(this);
	}

	public boolean getHitWall() {
		return _hit;
	}

	public void setHitWall(boolean hit) {
		_hit = hit;
	}
	
	public boolean run_until_wave_hits(Wave wave) {
		boolean ranIntoWall = false;
		while( !wave.hasHit(this) ) {
			next();
			if(getHitWall())
				ranIntoWall = true;
		}
		return ranIntoWall;
	}
}
