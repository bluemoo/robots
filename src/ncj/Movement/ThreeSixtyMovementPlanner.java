package ncj.Movement;

import java.util.ArrayList;
import java.util.Iterator;

import ncj.EnemyAnalysis;
import ncj.FiringSolution;
import ncj.TargetingComputer;
import ncj.Wave;

public class ThreeSixtyMovementPlanner extends PerpendicularMovementPlanner {

	public ThreeSixtyMovementPlanner(EnemyAnalysis enemyAnalysis,
			PlannedMovementController movementController) {
		super(enemyAnalysis, movementController);
	}
	
	@Override
	public ArrayList<MovementPlan> calculatePlansToTry(Wave wave) {
		ArrayList<MovementPlan> plans = new ArrayList<MovementPlan>();
		
		for(double angle = -Math.PI; angle < Math.PI + .001; angle += Math.PI/12)
		{
			plans.add(new MovementPlan().setTurn(angle).setAhead(Double.POSITIVE_INFINITY));
			plans.add(new MovementPlan().setTurn(angle).setAhead(Double.NEGATIVE_INFINITY));
		}
		return plans;
	}
	
	@Override
	public MovementPlan selectPlan( ArrayList<MovementPlan> plansThatDontHitAWall, Wave wave) {
		double bestP = 0;
		MovementPlan result = null;
		for( Iterator<MovementPlan> iter = plansThatDontHitAWall.iterator(); iter.hasNext();)
		{
			MovementPlan plan = iter.next();
			PlannedMovementController controller = buildControllerToTest(plan);
			TargetingComputer computer = new TargetingComputer(controller);
			FiringSolution solution = computer.calculate_firing_solution(wave);
			double percent = solution.getShadowPercentage();
			if(bestP <= percent)
			{
				bestP = percent;
				result = plan;
			}
		}
		return result;
	}
}
