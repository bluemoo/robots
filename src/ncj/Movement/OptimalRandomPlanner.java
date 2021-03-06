package ncj.Movement;

import java.util.ArrayList;
import java.util.Iterator;

import ncj.EnemyAnalysis;
import ncj.FiringSolution;
import ncj.TargetingComputer;
import ncj.Wave;

public class OptimalRandomPlanner extends StraightLinePlanner {

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
		
		return plans.get(selectWeightedIndex(percents, _random.next()));
	}
	
	public int selectWeightedIndex(ArrayList<Double> weights, double randomValue)
	{
		double total = 0;
		for(int i = 0 ; i < weights.size(); i++)
		{
			total += weights.get(i);
		}

		ArrayList<Double> thresholds = new ArrayList<Double>();
		for(int i = 0 ; i < weights.size(); i++)
		{
			thresholds.add(weights.get(i)/total);
		}
		
		double v = _random.next();
		double threshold = 0;
		for(int i = 0 ; i < thresholds.size(); i++)
		{
			threshold += thresholds.get(i);
			if(v <= threshold)
				return i;	
		}
		return 0;
	}
}
