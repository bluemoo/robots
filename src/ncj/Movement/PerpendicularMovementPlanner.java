package ncj.Movement;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;

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
		
		ArrayList<MovementPlan> plans = calculatePlansToTry(wave);
		Dictionary<MovementPlan, WaveHitResult> planResults = testPlans(plans, wave);
		ArrayList<MovementPlan> plansThatDontHitAWall = findPlansThatDontHitAWall(plans, planResults);
		
		MovementPlan planToUse;
		if(plansThatDontHitAWall.size() > 0)
		{ 
			planToUse = selectPlan(plansThatDontHitAWall);
		}
		else
		{ 
			planToUse = selectPlan(plans);
			System.out.println("Actually PLANNING to hit a wall. Get a better algorithm.");
		} 
						
		if( planToUse != null && planToUse.getNumberOfTicks() > 0)
			return planToUse;
		
		return null;
	}

	private MovementPlan selectPlan(
			ArrayList<MovementPlan> plansThatDontHitAWall) {
		return plansThatDontHitAWall.get(0);
	}

	private ArrayList<MovementPlan> findPlansThatDontHitAWall(
			ArrayList<MovementPlan> plans,
			Dictionary<MovementPlan, WaveHitResult> planResults) {
		ArrayList<MovementPlan> plansThatDontHitAWall = new ArrayList<MovementPlan>();
		for(Iterator<MovementPlan> e = plans.iterator(); e.hasNext();)
		{
			MovementPlan plan = e.next();
			if(planResults.get(plan).hitWall == false)
				plansThatDontHitAWall.add(plan);
		}
		return plansThatDontHitAWall;
	}

	private ArrayList<MovementPlan> calculatePlansToTry(Wave wave) {
		IGearbox lastPlanned = _movementController.getLastPlannedState();
		double direction = lastPlanned.getDistanceRemaining();
		if(direction == 0)
			direction = Double.POSITIVE_INFINITY;
		double rotation = calculateRotation(wave, lastPlanned);			

		ArrayList<MovementPlan> plans = new ArrayList<MovementPlan>();
		plans.add(new MovementPlan().setAhead(direction).setTurn(rotation).setTime(lastPlanned.getTime()));
		plans.add(new MovementPlan().setAhead(direction*-1).setTurn(rotation).setTime(lastPlanned.getTime()));
		return plans;
	}

	private Dictionary<MovementPlan, WaveHitResult> testPlans(ArrayList<MovementPlan> plans, Wave wave)
	{
		Dictionary<MovementPlan, WaveHitResult> results = new Hashtable<MovementPlan, WaveHitResult>();
			
		for(int i = 0; i < plans.size(); i++)
		{
			MovementPlan plan = plans.get(i);
			plan.setNumberOfTicks(Integer.MAX_VALUE);
			WaveHitResult result = buildControllerToTest(plan).run_until_wave_hits(wave);
			long elapsedTime = result.timeOfHit - plan.getTime();
			plan.setNumberOfTicks(elapsedTime);
			results.put(plan, result);
		}
		
		return results;
	}
	
	private PlannedMovementController buildControllerToTest(MovementPlan plan)
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
