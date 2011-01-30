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
	
	public Wave setFiringSolution(FiringSolution solution) {
		_solution = solution;
		return this;
	}
	
	public boolean hasHit(IGearbox gearbox) {
		double elapsedTime = gearbox.getTime() - getTime();
		double robotX = gearbox.getX();
		double robotY = gearbox.getY();
		double radius = getVelocity() * elapsedTime;

		//Check corners
		for(int i=-1; i<=1; i+=2)
		{
			for(int j=-1; j<=1; j+=2)
			{
				double distance = Point2D.distance(robotX+18*i, robotY+18*j, getX(), getY());
				
				if( distance < radius)
					return true;
			}
		}
		return false;
	}

}
