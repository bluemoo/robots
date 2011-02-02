package ncj.Movement;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Iterator;

import ncj.EnemyAnalysis;
import ncj.IGearbox;
import ncj.Wave;
import ncj.Movement.PlannedMovementController.WaveHitResult;

public abstract class MovementPlanner {

	protected PlannedMovementController _movementController;
	protected EnemyAnalysis _enemy;

	public abstract MovementPlan selectPlan(ArrayList<MovementPlan> plansThatDontHitAWall, Wave wave);
	public abstract ArrayList<MovementPlan> calculatePlansToTry(Wave wave);

	public MovementPlanner(EnemyAnalysis enemyAnalysis, PlannedMovementController movementController) {
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
			planToUse = selectPlan(plansThatDontHitAWall, wave);
		}
		else
		{ 
			planToUse = selectPlan(plans, wave);
			System.out.println("Actually PLANNING to hit a wall. Get a better algorithm.");
		} 
						
		if( planToUse != null && planToUse.getNumberOfTicks() > 0)
			return planToUse;
		
		return null;
	}

	private ArrayList<MovementPlan> findPlansThatDontHitAWall(ArrayList<MovementPlan> plans, Dictionary<MovementPlan, WaveHitResult> planResults) {
		ArrayList<MovementPlan> plansThatDontHitAWall = new ArrayList<MovementPlan>();
		for(Iterator<MovementPlan> e = plans.iterator(); e.hasNext();)
		{
			MovementPlan plan = e.next();
			if(planResults.get(plan).hitWall == false)
				plansThatDontHitAWall.add(plan);
		}
		return plansThatDontHitAWall;
	}

	private Dictionary<MovementPlan, WaveHitResult> testPlans(ArrayList<MovementPlan> plans, Wave wave) {
		Dictionary<MovementPlan, WaveHitResult> results = new Hashtable<MovementPlan, WaveHitResult>();
		IGearbox lastPlanned = _movementController.getLastPlannedState();
		long time = lastPlanned.getTime();
		
		for(int i = 0; i < plans.size(); i++)
		{
			MovementPlan plan = plans.get(i);
			plan.setNumberOfTicks(Integer.MAX_VALUE);
			plan.setTime(time);
			WaveHitResult result = buildControllerToTest(plan).run_until_wave_hits(wave);
			long elapsedTime = result.timeOfHit - plan.getTime();
			plan.setNumberOfTicks(elapsedTime);
			results.put(plan, result);
		}
		
		return results;
	}

	protected PlannedMovementController buildControllerToTest(MovementPlan plan) {
		PlannedMovementController copiedPlans = new PlannedMovementController(_movementController.getCurrentGearbox());
		copiedPlans.Copy(_movementController);
		copiedPlans.setMovement(plan);
		return copiedPlans;
	}

}