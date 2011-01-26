package ncj.Movement;

import ncj.IGearbox;
import robocode.util.Utils;

public class UpAndDownMovementController extends MovementControllerBase {
	Double ARENA_HEIGHT = 600.0;
	Double TURNAROUND_DISTANCE = 50.0;

	public UpAndDownMovementController(IGearbox gearbox) {
		super(gearbox);
	}

	@Override
	protected void set_controls(IGearbox gearbox) {
		if( Math.abs(Utils.normalRelativeAngle(gearbox.getHeadingRadians())) > .00001) {
			gearbox.setTurnLeftRadians(gearbox.getHeadingRadians());
			return;
		}
		
		double y = gearbox.getY();
		if(y > ARENA_HEIGHT - TURNAROUND_DISTANCE)
			gearbox.setAhead(Double.NEGATIVE_INFINITY);
		
		if(y < TURNAROUND_DISTANCE || gearbox.getDistanceRemaining() == 0)
			gearbox.setAhead(Double.POSITIVE_INFINITY);
	}

}
