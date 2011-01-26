package ncj.Movement;

import ncj.IGearbox;
import robocode.util.Utils;

public class WallSmoothing implements IWallSmoothing {
	
	static double OFFSET = 180;
	static double LEFT = OFFSET;
	static double RIGHT = 800 - OFFSET;
	static double BOTTOM = OFFSET;
	static double TOP = 600 - OFFSET;
	
	public void smooth(IGearbox gearbox)
	{
		double x = gearbox.getX();
		double y = gearbox.getY();
		double turn = gearbox.getTurnRemainingRadians();
		double heading = gearbox.getHeadingRadians();

		if( gearbox.getDistanceRemaining() < 0)
			heading = Utils.normalAbsoluteAngle(heading + Math.PI);

		if(x <= LEFT || x >= RIGHT || y >= TOP || y <= BOTTOM)
			if (y <= BOTTOM)
				heading = Utils.normalAbsoluteAngle(heading + Math.PI/2.0);	
			else if (x >= RIGHT)
				heading = Utils.normalAbsoluteAngle(heading + Math.PI);
			else if ( y >= TOP)
				heading = Utils.normalAbsoluteAngle(heading + Math.PI*3.0/2);
				
			if(heading - Math.PI*3/2 > 0)
				turn = Math.max(Math.PI*2 - heading, gearbox.getTurnRemainingRadians());
			else
				turn = Math.min(Math.PI - heading, gearbox.getTurnRemainingRadians());
		
		gearbox.setTurnRightRadians(turn);
	}
	
	
}
