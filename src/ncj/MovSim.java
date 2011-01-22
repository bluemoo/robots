package ncj;

import robocode.util.*;


// Based off of code found here: http://robowiki.net/w/index.php?title=FuturePosition
public class MovSim {
	
	private double systemMaxTurnRate = Math.toRadians(10.0);
	private double systemMaxVelocity = 8.0;
	private double maxBraking = 2.0;
	private double maxAcceleration = 1.0;
	
	
	public double defaultMaxTurnRate = 10.0;
	public double defaultMaxVelocity = 8.0;
	
	
	public MovSim() {};
		
	
	public void futurePos(SimulatedGearbox b) { futurePos(b, defaultMaxVelocity, defaultMaxTurnRate, b.getBattleFieldWidth(), b.getBattleFieldHeight()); }
	
	private void futurePos(SimulatedGearbox b, double maxVelocity, double maxTurnRate, double battleFieldW, double battleFieldH) {
		double x = b.getX();
		double y = b.getY();
		double velocity = b.getVelocity();
		double heading = b.getHeadingRadians();
		double distanceRemaining = b.getDistanceRemaining();
		double angleToTurn = b.getTurnRemainingRadians();
		
		double acceleration = 0;
		boolean slowingDown = false;
		double moveDirection;
	
		maxTurnRate = Math.toRadians(maxTurnRate);
		if (distanceRemaining == 0) moveDirection = 0; else if (distanceRemaining < 0.0) moveDirection = -1; else moveDirection = 1;
		
		//heading
		double turnRate = Math.min(maxTurnRate, ((0.4 + 0.6 * (1.0 - (Math.abs(velocity) / systemMaxVelocity))) * systemMaxTurnRate));
		if (angleToTurn > 0.0) {
    		if (angleToTurn < turnRate) { heading += angleToTurn; angleToTurn = 0.0; } 
			else { heading += turnRate; angleToTurn -= turnRate; }
		} else if (angleToTurn < 0.0) {
    		if (angleToTurn > -turnRate) { heading += angleToTurn; angleToTurn = 0.0; } 
			else { heading -= turnRate;	angleToTurn += turnRate; }
		}
		heading = Utils.normalAbsoluteAngle(heading);
		
		//movement
		b.setHitWall(false);
		if (distanceRemaining != 0.0 || velocity != 0.0) { 
			//lastX = x; lastY = y;
			if (!slowingDown && moveDirection == 0) {
				slowingDown = true;
				if (velocity > 0.0) moveDirection = 1;
				else if (velocity < 0.0) moveDirection = -1;
				else moveDirection = 0;
		    }
		    double desiredDistanceRemaining = distanceRemaining;
    		if (slowingDown) {
				if (moveDirection == 1 && distanceRemaining < 0.0) desiredDistanceRemaining = 0.0;
				else if (moveDirection == -1 && distanceRemaining > 1.0) desiredDistanceRemaining = 0.0;
		    }
    		double slowDownVelocity	= (double) (int) (maxBraking / 2.0 * ((Math.sqrt(4.0 * Math.abs(desiredDistanceRemaining)+ 1.0)) - 1.0));
	    	if (moveDirection == -1) slowDownVelocity = -slowDownVelocity;
	    	if (!slowingDown) {
				if (moveDirection == 1) {
	    			if (velocity < 0.0) acceleration = maxBraking;
		    		else acceleration = maxAcceleration;
			    	if (velocity + acceleration > slowDownVelocity) slowingDown = true;
				} else if (moveDirection == -1) {
			    	if (velocity > 0.0) acceleration = -maxBraking;
	    			else acceleration = -maxAcceleration;
	    			if (velocity + acceleration < slowDownVelocity)	slowingDown = true;
				}
		    }
    		if (slowingDown) {
				if (distanceRemaining != 0.0 && Math.abs(velocity) <= maxBraking && Math.abs(distanceRemaining) <= maxBraking) slowDownVelocity = distanceRemaining;
				double perfectAccel = slowDownVelocity - velocity;
				if (perfectAccel > maxBraking) perfectAccel = maxBraking;
				else if (perfectAccel < -maxBraking) perfectAccel = -maxBraking;
				acceleration = perfectAccel;
	    	}
		    if (velocity > maxVelocity || velocity < -maxVelocity) acceleration = 0.0;
	    	velocity += acceleration;
		    if (velocity > maxVelocity)	velocity -= Math.min(maxBraking, velocity - maxVelocity);
	    	if (velocity < -maxVelocity) velocity += Math.min(maxBraking, -velocity - maxVelocity);
	    	double dx = velocity * Math.sin(heading); double dy = velocity * Math.cos(heading);
		    x += dx; y += dy;
		    //boolean updateBounds = false;
    		//if (dx != 0.0 || dy != 0.0) updateBounds = true;
		    if (slowingDown && velocity == 0.0) { distanceRemaining = 0.0; moveDirection = 0; slowingDown = false; acceleration = 0.0; }
		    //if (updateBounds) updateBoundingBox();
    		distanceRemaining -= velocity;
			if (x<18 || y<18 || x>battleFieldW-18 || y>battleFieldH-18) {
				distanceRemaining = 0;
				angleToTurn = 0;
				velocity = 0;
				moveDirection = 0;
				x = Math.max(18,Math.min(battleFieldW-18,x));
				y = Math.max(18,Math.min(battleFieldH-18,y));
				b.setHitWall(true);
			}
		}
		
		b.setAhead(distanceRemaining);
		b.setVelocity(velocity);
		b.setPosition(x, y);
		b.setTurnRightRadians(angleToTurn);
		b.setHeading(heading);
		b.setTime(b.getTime() + 1);
	}
		
}

