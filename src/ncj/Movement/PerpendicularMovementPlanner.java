package ncj.Movement;

import ncj.EnemyAnalysis;
import ncj.IGearbox;
import ncj.Vector2D;
import ncj.Wave;
import ncj.Movement.PlannedMovementController.WaveHitResult;

public class PerpendicularMovementPlanner {
	PlannedMovementController _movementController;
	EnemyAnalysis _enemy;
	
	public PerpendicularMovementPlanner(EnemyAnalysis enemyAnalysis, PlannedMovementController movementController) {
		_movementController = movementController;
		_enemy = enemyAnalysis;
	}

	public void plan() {
		if(_enemy.bulletFired()) {
			MovementPlan plan = calculatePlan(_enemy.getLatestWave());
			_movementController.setMovement(plan);
		}
	}
	
	public MovementPlan calculatePlan(Wave wave) {
		
		IGearbox lastPlanned = _movementController.getLastPlannedState();
		double direction = lastPlanned.getDistanceRemaining();
		if(direction == 0)
			direction = Double.POSITIVE_INFINITY;
		double rotation = calculateRotation(wave, lastPlanned);			

		MovementPlan[] plans = new MovementPlan[] {
													new MovementPlan().setAhead(direction).setTurn(rotation).setTime(lastPlanned.getTime()),
													new MovementPlan().setAhead(direction*-1).setTurn(rotation).setTime(lastPlanned.getTime())
												  };
		MovementPlan plan = null;
		for(int i = 0; i < plans.length; i++)
		{
			plan = plans[i];
			plan.setNumberOfTicks(Integer.MAX_VALUE);
			WaveHitResult result = BuildControllerToTest(plan).run_until_wave_hits(wave);
			long elapsedTime = result.timeOfHit - lastPlanned.getTime();
			plan.setNumberOfTicks(elapsedTime);

			if(result.hitWall == false)
				break;
		}
				
		if( plan.getNumberOfTicks() > 0)
			return plan;
		
		return null;
	}

	private PlannedMovementController BuildControllerToTest(MovementPlan plan)
	{
		PlannedMovementController copiedPlans = new PlannedMovementController(_movementController.getLastPlannedState());
		copiedPlans.Copy(_movementController);
		copiedPlans.setMovement(plan);
		return copiedPlans;
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
