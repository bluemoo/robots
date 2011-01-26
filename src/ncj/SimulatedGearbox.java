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
}
