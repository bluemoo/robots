package ncj;

import robocode.AdvancedRobot;

public class Gearbox implements IGearbox{
	AdvancedRobot _robot;
	
	public Gearbox(AdvancedRobot robot) {
		_robot = robot;
	}
	
	@Override
	public double getHeadingRadians() {
		return _robot.getHeadingRadians();
	}

	@Override
	public double getTurnRemainingRadians() {
		return _robot.getTurnRemainingRadians();
	}

	@Override
	public void setTurnLeftRadians(double radians) {
		_robot.setTurnLeftRadians(radians);
	}
	
	@Override
	public void setTurnRightRadians(double radians) {
		_robot.setTurnRightRadians(radians);
	}

	@Override
	public void setAhead(double distance) {
		_robot.setAhead(distance);
	}

	@Override
	public double getY() {
		return _robot.getY();
	}
	
	@Override
	public double getX() {
		return _robot.getX();
	}

	@Override
	public double getDistanceRemaining() {
		return _robot.getDistanceRemaining();
	}

	@Override
	public double getBattleFieldHeight() {
		return _robot.getBattleFieldHeight();
	}

	@Override
	public double getBattleFieldWidth() {
		return _robot.getBattleFieldWidth();
	}

	@Override
	public double getVelocity() {
		return _robot.getVelocity();
	}
	
	public long getTime() {
		return _robot.getTime();
	} 
	
	public double getGunTurnRemainingRadians() {
		return _robot.getGunTurnRemainingRadians();
	}

	@Override
	public void setTurnGunRightRadians(double radians) {
		_robot.setTurnGunRightRadians(radians);
	}

	@Override
	public double getGunHeadingRadians() {
		return _robot.getGunHeadingRadians();
	}

	@Override
	public void setFire(double fire) {
		_robot.setFire(fire);
	}

}
