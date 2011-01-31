package ncj.Movement;

import java.util.ArrayList;
import java.util.Iterator;

import ncj.EnemyAnalysis;
import ncj.FiringSolution;
import ncj.TargetingComputer;
import ncj.Wave;

public class OptimalRandomPlanner extends ThreeSixtyMovementPlanner {

	IRandomNumber _random;
	
	public OptimalRandomPlanner(EnemyAnalysis enemyAnalysis,
			PlannedMovementController movementController, IRandomNumber random) {
		super(enemyAnalysis, movementController);
		_random = random;
	}

	@Override
	public MovementPlan selectPlan(
			ArrayList<MovementPlan> plans, Wave wave) {
		
		ArrayList<Double> percents = new ArrayList<Double>();
		for( Iterator<MovementPlan> iter = plans.iterator(); iter.hasNext();)
		{
			MovementPlan plan = iter.next();
			PlannedMovementController controller = buildControllerToTest(plan);
			TargetingComputer computer = new TargetingComputer(controller);
			FiringSolution solution = computer.calculate_firing_solution(wave);
			double percent = solution.getShadowPercentage();
			percents.add(percent);
		}
		
		double total = 0;
		for(int i = 0 ; i < plans.size(); i++)
		{
			total += percents.get(i);
		}

		ArrayList<Double> thresholds = new ArrayList<Double>();
		for(int i = 0 ; i < plans.size(); i++)
		{
			thresholds.add(percents.get(i)/total);
		}
		
		double v = _random.next();
		double threshold = 0;
		for(int i = 0 ; i < plans.size(); i++)
		{
			threshold += thresholds.get(i);
			if(v <= threshold)
			return plans.get(i);	
		}
		
		return null;
	}

}
