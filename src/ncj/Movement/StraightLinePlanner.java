package ncj.Movement;

import java.util.ArrayList;

import ncj.EnemyAnalysis;
import ncj.IGearbox;
import ncj.Vector2D;
import ncj.Wave;

public class StraightLinePlanner extends ThreeSixtyMovementPlanner {

	public StraightLinePlanner(EnemyAnalysis enemyAnalysis,
			PlannedMovementController movementController) {
		super(enemyAnalysis, movementController);
	}
	
	@Override
	public ArrayList<MovementPlan> calculatePlansToTry(Wave wave) {
		ArrayList<MovementPlan> plans = new ArrayList<MovementPlan>();
		
		double rot1 = calculateRotation1(wave, _movementController.getCurrentGearbox());
		double rot2 = calculateRotation2(wave, _movementController.getCurrentGearbox());
		
		for(double d = 0; d<=600; d+=3)
		{
			plans.add(new MovementPlan().setTurn(rot1).setAhead(d));
		}
		
		for(double d = 0; d>=-600; d-=3)
		{
			plans.add(new MovementPlan().setTurn(rot2).setAhead(d));
		}
		
		return plans;
	}
	
	private double calculateRotation1(Wave wave, IGearbox gearbox) {
		Vector2D pRobot = new Vector2D(gearbox.getX(), gearbox.getY());
		Vector2D pWave = new Vector2D(wave.getX(), wave.getY());
		Vector2D vWtoR = pRobot.minus(pWave);
		Vector2D vDesired = vWtoR.calculatePerpendicular();
		double bearing = Math.PI/2 - Math.atan2(vDesired.getY(), vDesired.getX());
		double rotation = robocode.util.Utils.normalRelativeAngle(bearing - gearbox.getHeadingRadians());
		
		if( Math.abs(rotation) > Math.PI/2)
			rotation = robocode.util.Utils.normalRelativeAngle(rotation + Math.PI);
		return rotation;
	}
	private double calculateRotation2(Wave wave, IGearbox gearbox) {
		Vector2D pRobot = new Vector2D(gearbox.getX(), gearbox.getY());
		Vector2D pWave = new Vector2D(wave.getX(), wave.getY());
		Vector2D vWtoR = pRobot.minus(pWave);
		Vector2D vDesired = vWtoR.calculatePerpendicular();
		double bearing = Math.PI/2 - Math.atan2(vDesired.getY(), vDesired.getX()) + Math.PI;
		double rotation = robocode.util.Utils.normalRelativeAngle(bearing - gearbox.getHeadingRadians());
		
		if( Math.abs(rotation) > Math.PI/2)
			rotation = robocode.util.Utils.normalRelativeAngle(rotation + Math.PI);
		return rotation;
	}

}
