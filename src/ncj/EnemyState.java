package ncj;

import robocode.ScannedRobotEvent;

public class EnemyState {

	private double _energy;
	private double _x = 0;
	private double _y = 0;
	private long _time;
	
	public EnemyState() {
	}
	
	public EnemyState( ScannedRobotEvent e, IGearbox gearbox) {
		setEnergy(e.getEnergy());
		setPosition(gearbox, e.getBearingRadians(), e.getDistance());
		setTime(e.getTime());
	}

	public EnemyState setEnergy(double e) {
		_energy = e;
		
		return this;
	}

	public double getEnergy() {
		return _energy;
	}


	public EnemyState setPosition(IGearbox gearbox, double bearingToEnemy, double distance) {
		
		double absoluteBearingToEnemy = gearbox.getHeadingRadians() + bearingToEnemy;
		
		double x = gearbox.getX() + Math.sin(absoluteBearingToEnemy) * distance;
		double y = gearbox.getY() + Math.cos(absoluteBearingToEnemy) * distance;
		
		return setPosition(x, y);
	}
	
	public EnemyState setPosition(double x, double y) {
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
	
	public EnemyState setTime(long time) {
		_time = time;
		
		return this;
	}
	
	public long getTime() {
		return _time;
	}

}
