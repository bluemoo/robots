package ncj;

import java.awt.geom.Point2D;

public class Wave {

	private double _power;
	private double _x;
	private double _y;
	private long _time;
	private FiringSolution _solution;
	
	public Wave() {
		
	}
	
	public Wave(double power, EnemyState state) {
		_power = power;
		_x = state.getX();
		_y = state.getY();
		_time = state.getTime();
	}
	
	public double getPower() {
		return _power;
	}

	public double getX() {
		return _x;
	}

	public double getY() {
		return _y;
	}

	public long getTime() {
		return _time;
	}

	public double getVelocity() {
		return 20 - (3 * _power);
	}

	public FiringSolution getFiringSolution() {
		return _solution;
	}
	
	public void setFiringSolution(FiringSolution solution) {
		_solution = solution;
	}
	
	public boolean hasHit(IGearbox gearbox) {
		double distance = Point2D.distance(gearbox.getX(), gearbox.getY(), getX(), getY());
		double elapsedTime = gearbox.getTime() - getTime();
		double radius = getVelocity() * elapsedTime;
		
		return distance < radius;

	}

}
