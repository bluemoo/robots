package ncj.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import ncj.EnemyAnalysis;
import ncj.EnemyState;
import ncj.FakeGearbox;
import ncj.Wave;
import ncj.Movement.MovementPlan;
import ncj.Movement.PlannedMovementController;
import ncj.Movement.ThreeSixtyMovementPlanner;

import org.junit.Test;

public class ThreeSixtyMovementPlannerTests {
	
	@Test public void ShouldReturnFourtyEightPossiblePlansEveryFifteenDegreesAndBothDirections() {
		FakeGearbox gearbox = new FakeGearbox();
		ThreeSixtyMovementPlanner planner = new ThreeSixtyMovementPlanner(new EnemyAnalysis(), new PlannedMovementController(gearbox));

		ArrayList<MovementPlan> plans = planner.calculatePlansToTry(new Wave());
		assertEquals(50, plans.size() );
		assertEquals(Math.PI, plans.get(49).getTurn(), .0001);
	}

	@Test public void ShouldSelectThePlanWithTheBestShadowPercentage()
	{
		FakeGearbox gearbox = new FakeGearbox().setPosition(400, 300).setTime(0);
		PlannedMovementController controller = new PlannedMovementController(gearbox);
		ThreeSixtyMovementPlanner planner = new ThreeSixtyMovementPlanner(new EnemyAnalysis(), controller);
		
		ArrayList<MovementPlan> plans = new ArrayList<MovementPlan>();
		MovementPlan expected = new MovementPlan().setAhead(Double.POSITIVE_INFINITY).setNumberOfTicks(Integer.MAX_VALUE).setTime(0);
		plans.add(new MovementPlan().setAhead(0).setNumberOfTicks(Integer.MAX_VALUE).setTime(0));
		plans.add(expected);
		plans.add(new MovementPlan().setAhead(40).setNumberOfTicks(Integer.MAX_VALUE).setTime(0));
		
		MovementPlan plan = planner.selectPlan(plans, new Wave(3, new EnemyState().setPosition(700, 300).setTime(0)));
		assertEquals(expected, plan);
	}
}
