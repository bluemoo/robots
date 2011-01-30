package ncj;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

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

		java.awt.geom.Ellipse2D waveEllipse = new Ellipse2D.Double(getX()-radius,getY()-radius,radius*2,radius*2);
		Rectangle2D.Double robotBound = new Rectangle2D.Double(robotX-18, robotY - 18, 36, 36);
		return waveEllipse.intersects(robotBound);
	}

}
