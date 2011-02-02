package ncj;

public class CachedGearbox implements IGearbox {

	final IGearbox _gearbox;
	double _x;
	double _y;
	double _battleFieldHeight;
	double _battleFieldWidth;
	double _distanceRemaining;
	double _heading;
	long _time;
	double _turnRemaining;
	double _velocity;
	
	public CachedGearbox(IGearbox gearbox)
	{
		_gearbox = gearbox;
		update();
	}
	
	public void update()
	{
		_x = _gearbox.getX();
		_y = _gearbox.getY();
		_battleFieldHeight = _gearbox.getBattleFieldHeight();
		_battleFieldWidth = _gearbox.getBattleFieldWidth();
		_distanceRemaining = _gearbox.getDistanceRemaining();
		_heading = _gearbox.getHeadingRadians();
		_time = _gearbox.getTime();
		_turnRemaining = _gearbox.getTurnRemainingRadians();
		_velocity = _gearbox.getVelocity();
	}
	
	@Override
	public double getBattleFieldHeight() {
		return _battleFieldHeight;
	}

	@Override
	public double getBattleFieldWidth() {
		return _battleFieldWidth;
	}

	@Override
	public double getDistanceRemaining() {
		return _distanceRemaining;
	}

	@Override
	public double getGunHeadingRadians() {
		return _gearbox.getGunHeadingRadians();
	}

	@Override
	public double getGunTurnRemainingRadians() {
		return _gearbox.getGunTurnRemainingRadians();
	}

	@Override
	public double getHeadingRadians() {
		return _heading;
	}

	@Override
	public long getTime() {
		return _time;
	}

	@Override
	public double getTurnRemainingRadians() {
		return _turnRemaining;
	}

	@Override
	public double getVelocity() {
		return _velocity;
	}

	@Override
	public double getX() {
		return _x;
	}

	@Override
	public double getY() {
		return _y;
	}

	@Override
	public void setAhead(double distance) {
		_gearbox.setAhead(distance);
		update();
	}

	@Override
	public void setFire(double fire) {
		_gearbox.setFire(fire);
		update();
	}

	@Override
	public void setTurnGunRightRadians(double radians) {
		_gearbox.setTurnGunRightRadians(radians);
		update();
	}

	@Override
	public void setTurnLeftRadians(double radians) {
		_gearbox.setTurnLeftRadians(radians);
		update();
	}

	@Override
	public void setTurnRightRadians(double radians) {
		_gearbox.setTurnRightRadians(radians);
		update();
	}

}
