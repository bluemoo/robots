package ncj;

public class FakeGearbox implements IGearbox {		
	double _turnRemaining = 0;
	double _distanceRemaining = 0;
	private double _x;
	private double _y;
	double _headingRadians = 0;
	double _velocity;
	private long _time;
	private double _gunTurnRemaining;
	private double _gunHeading;
	private boolean _wasFired;
	private double _fired = -1;
	
	public FakeGearbox Copy(IGearbox gearbox) {
		_x = gearbox.getX();
		_y = gearbox.getY();
		_distanceRemaining = gearbox.getDistanceRemaining();
		_velocity = gearbox.getVelocity();
		_headingRadians = gearbox.getHeadingRadians();
		_turnRemaining = gearbox.getTurnRemainingRadians();
		_time = gearbox.getTime();

		return this;
	}
	
	public FakeGearbox setHeading(double radians) {
		_headingRadians = radians;
		return this;
	}
	
	public double getHeadingRadians() {
		return _headingRadians;
	}
	
	public double getTurnRemainingRadians() {
		return _turnRemaining;
	}

	public void setTurnLeftRadians(double angle) {
		_turnRemaining = angle * -1;
	}

	public void setTurnRightRadians(double angle) {
		_turnRemaining = angle;
	}

	public FakeGearbox setPosition(double x, double y) {
		_x = x;
		_y = y;
		
		return this;
	}

	public double getX() {
		return _x;
	}
	
	public double getY() {
		return _y;
	}
	
	public void setAhead(double distance) {
		_distanceRemaining = distance;
	}
	
	public double getDistanceRemaining() {
		return _distanceRemaining;
	}

	@Override
	public double getBattleFieldHeight() {
		return 600;
	}

	@Override
	public double getBattleFieldWidth() {
		return 800;
	}

	public FakeGearbox setVelocity(double velocity) {
		_velocity = velocity;
		
		return this;
	}
	
	@Override
	public double getVelocity() {
		// TODO Auto-generated method stub
		return _velocity;
	}

	public FakeGearbox setTime(long time) {
		_time = time;
		
		return this;
	}
	
	public long getTime() {
		return _time;
	}

	public double getGunTurnRemainingRadians() {
		// TODO Auto-generated method stub
		return _gunTurnRemaining;
	}

	@Override
	public void setTurnGunRightRadians(double radians) {
		_gunTurnRemaining = radians;
	}

	@Override
	public double getGunHeadingRadians() {
		return _gunHeading;
	}
	
	public FakeGearbox setGunHeading(double radians) {
		_gunHeading = radians;
		
		return this;
	}

	public boolean getWasFired() {
		return _wasFired;
	}

	public void clearFired() {
		_wasFired = false;
		_fired = -1;
	}
	
	@Override
	public void setFire(double fire) {
		_fired = fire;
		_wasFired = true;
	}

	public double getFired() {
		return _fired;
	}

}
