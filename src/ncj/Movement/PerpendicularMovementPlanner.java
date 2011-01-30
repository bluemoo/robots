package ncj.Movement;

import java.util.ArrayList;

import ncj.EnemyAnalysis;
import ncj.IGearbox;
import ncj.Vector2D;
import ncj.Wave;

public class PerpendicularMovementPlanner extends MovementPlanner {
	public PerpendicularMovementPlanner(EnemyAnalysis enemyAnalysis, PlannedMovementController movementController) {
		super(enemyAnalysis, movementController);
	}

	@Override
	public ArrayList<MovementPlan> calculatePlansToTry(Wave wave) {
		IGearbox lastPlanned = _movementController.getLastPlannedState();
		double direction = lastPlanned.getDistanceRemaining();
		if(direction == 0)
			direction = Double.POSITIVE_INFINITY;
		double rotation = calculateRotation(wave, lastPlanned);			
		
		ArrayList<MovementPlan> plans = new ArrayList<MovementPlan>();
		plans.add(new MovementPlan().setAhead(direction).setTurn(rotation));
		plans.add(new MovementPlan().setAhead(direction*-1).setTurn(rotation));
		return plans;
	}

	@Override
	public MovementPlan selectPlan( ArrayList<MovementPlan> plansThatDontHitAWall, Wave wave) {
		return plansThatDontHitAWall.get(0);
	}

	private double calculateRotation(Wave wave, IGearbox gearbox) {
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

}
