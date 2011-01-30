package ncj.Movement;

import ncj.EnemyAnalysis;
import ncj.IGearbox;
import ncj.SimulatedGearbox;
import ncj.Vector2D;
import ncj.Wave;

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
	
		WaveHitResult result = run_until_wave_hits(wave, rotation, direction);	
		
		if(result.hitWall) {
			direction *= -1;	
			result = run_until_wave_hits(wave, rotation, direction);
		}
		
		long elapsedTime = result.gearbox.getTime() - lastPlanned.getTime();
		if( elapsedTime > 0)
			return new MovementPlan().setAhead(direction).setTurn(rotation).setNumberOfTicks(elapsedTime).setTime(lastPlanned.getTime());
		
		return null;
	}

	private class WaveHitResult
	{
		IGearbox gearbox;
		boolean hitWall;

		public WaveHitResult(IGearbox g, boolean h)
		{
			gearbox = g;
			hitWall = h;
		}
	}
	
	private WaveHitResult run_until_wave_hits(Wave wave, double rotation, double direction) {
		IGearbox lastPlanned = _movementController.getLastPlannedState();
		
		MovementPlan planToTest = new MovementPlan().setAhead(direction).setTurn(rotation).setNumberOfTicks(Integer.MAX_VALUE).setTime(lastPlanned.getTime());
		
		PlannedMovementController copiedPlans = new PlannedMovementController(lastPlanned);
		copiedPlans.Copy(_movementController);
		copiedPlans.setMovement(planToTest);

		boolean hitWall = false;
		if(!wave.hasHit(lastPlanned))
		{
			for( SimulatedGearbox simulation: copiedPlans.predict_future_position())
			{
				if(simulation.getHitWall())
					hitWall = true;
				if( wave.hasHit(simulation))
				{
					return new WaveHitResult(simulation, hitWall);
				}
			}
		}
		return new WaveHitResult(lastPlanned, false);
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
