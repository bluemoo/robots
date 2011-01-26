package ncj.Movement;

public class MovementPlan {

	private double _ahead;
	private double _turn;
	private long _numberOfTicks = 1;
	private long _time;

	public MovementPlan setAhead(double ahead) {
		_ahead = ahead;
		return this;
	}
	
	public double getAhead() {
		return _ahead;
	}
	
	public MovementPlan setTurn(double radians) {
		_turn = radians;
		return this;
	}
	
	public double getTurn() {
		return _turn;
	}

	public MovementPlan setNumberOfTicks(long ticks) {
		_numberOfTicks = ticks;
		return this;
	}
	
	public long getNumberOfTicks() {
		return _numberOfTicks;
	}

	public long getTime() {
		return _time;
	}
	
	public MovementPlan setTime(long time) {
		_time = time;
		return this;
	}

}
