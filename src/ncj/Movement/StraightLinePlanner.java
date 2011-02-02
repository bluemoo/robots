package ncj.Movement;

import java.util.ArrayList;

import ncj.EnemyAnalysis;
import ncj.SimulatedGearbox;
import ncj.Wave;
import ncj.Movement.PlannedMovementController.WaveHitResult;

public class StraightLinePlanner extends ThreeSixtyMovementPlanner {

	public StraightLinePlanner(EnemyAnalysis enemyAnalysis,
			PlannedMovementController movementController) {
		super(enemyAnalysis, movementController);
	}
	
	@Override
	public ArrayList<MovementPlan> calculatePlansToTry(Wave wave) {
		ArrayList<MovementPlan> plans = new ArrayList<MovementPlan>();
		
		double lastDistance = 2000;
		double distanceAtHit = 0;
		MovementPlan plan = new MovementPlan().setTurn(0).setAhead(lastDistance);
		PlannedMovementController controller = buildControllerToTest(plan);
		for( SimulatedGearbox simulation: controller.predict_future_position())
		{
			distanceAtHit = simulation.getDistanceRemaining();
			if( wave.hasHit(simulation))
				break;
		}
		double span = lastDistance - distanceAtHit;
		for(double d = 0; d <= span; d++)
		{
			plans.add(new MovementPlan().setTurn(0).setAhead(d));
		}

		lastDistance = -2000;
		distanceAtHit = 0;
		plan = new MovementPlan().setTurn(0).setAhead(lastDistance);
		controller = buildControllerToTest(plan);
		for( SimulatedGearbox simulation: controller.predict_future_position())
		{
			distanceAtHit = simulation.getDistanceRemaining();
			if( wave.hasHit(simulation))
				break;
		}
		span = lastDistance - distanceAtHit;
		for(double d = 0; d >= span; d--)
		{
			plans.add(new MovementPlan().setTurn(0).setAhead(d));
		}
		

		return plans;
	}

}
