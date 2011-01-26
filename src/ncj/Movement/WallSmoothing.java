package ncj.Movement;

import java.util.Vector;

import ncj.IGearbox;
import robocode.util.Utils;

public class WallSmoothing implements IWallSmoothing {
	
	static double OFFSET = 160;
	static double LEFT = 0;
	static double RIGHT = 800;
	static double BOTTOM = 0;
	static double TOP = 600;
	
	private boolean isHeadingTowardCloseWall(double heading, double x, double y)
	{
		double dx = Math.sin(heading);
		double dy = Math.cos(heading);
	
		double distToTop = dy == 0 ? Double.POSITIVE_INFINITY : (TOP - y) / dy;
		double distToBottom = dy == 0 ? Double.POSITIVE_INFINITY : (BOTTOM - y) / dy;
		double distToLeft = dx == 0 ? Double.POSITIVE_INFINITY : (LEFT - x) / dx;
		double distToRight = dx == 0 ? Double.POSITIVE_INFINITY : (RIGHT - x) / dx;

		if((distToTop < 0 || distToTop > OFFSET) && (distToBottom < 0 || distToBottom > OFFSET)
				&& (distToLeft < 0 || distToLeft > OFFSET) && (distToRight < 0 || distToRight > OFFSET))
			return true;
		return false;
	}
	
	public void smooth(IGearbox gearbox)
	{
		double x = gearbox.getX();
		double y = gearbox.getY();
		double turn = gearbox.getTurnRemainingRadians();
		double heading = gearbox.getHeadingRadians();

		if( gearbox.getDistanceRemaining() < 0)
			heading = Utils.normalAbsoluteAngle(heading + Math.PI);

		if(x <= LEFT + OFFSET || x >= RIGHT - OFFSET || y >= TOP - OFFSET || y <= BOTTOM + OFFSET)
		{
			//Turn towards each of the four possible directions
			//Discard those that end up running towards a wall
			//Select the one with the least amount of turning
			double[] possibleTurns = {Utils.normalRelativeAngle(0 - heading),
										Utils.normalRelativeAngle(Math.PI - heading),
										Utils.normalRelativeAngle(Math.PI/2.0 - heading),
										Utils.normalRelativeAngle(Math.PI*3/2.0 - heading)};
			

			Vector<Double> turnsAwayFromWalls = new Vector<Double>();
			for( double possibleTurn : possibleTurns)
			{
				double headingAfterTurn = heading + possibleTurn;
				if(isHeadingTowardCloseWall(headingAfterTurn, x, y))	
					turnsAwayFromWalls.add(possibleTurn);
			}

			double leastTurning = Math.PI*2;
			for( double possibleTurn : turnsAwayFromWalls)
			{
				if(Math.abs(possibleTurn) < Math.abs(leastTurning))
					leastTurning = possibleTurn;
			}
			
			if(leastTurning > 0)
				turn = Math.max(leastTurning, gearbox.getTurnRemainingRadians());
			else
				turn = Math.min(leastTurning, gearbox.getTurnRemainingRadians());
		}
		gearbox.setTurnRightRadians(turn);
	}
	
	
}
