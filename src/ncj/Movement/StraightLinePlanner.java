package ncj.Movement;

import java.util.ArrayList;

import ncj.EnemyAnalysis;
import ncj.Wave;

public class StraightLinePlanner extends ThreeSixtyMovementPlanner {

	public StraightLinePlanner(EnemyAnalysis enemyAnalysis,
			PlannedMovementController movementController) {
		super(enemyAnalysis, movementController);
	}
	
	@Override
	public ArrayList<MovementPlan> calculatePlansToTry(Wave wave) {
		ArrayList<MovementPlan> plans = new ArrayList<MovementPlan>();
		
		for(double d = -600; d<=600; d+=3)
		{
			plans.add(new MovementPlan().setTurn(0).setAhead(d));
		}
		return plans;
	}

}
