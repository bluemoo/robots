package ncj.tests;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import ncj.EnemyAnalysis;
import ncj.EnemyState;
import ncj.FakeGearbox;
import ncj.Wave;
import ncj.Movement.MovementPlan;
import ncj.Movement.OptimalRandomPlanner;
import ncj.Movement.PlannedMovementController;

import org.junit.Test;

public class OptimalRandomPlannerTests {
	@Test public void ShouldSelectTheFirstSecondOrThirdPlanAsTheRandomNumberSuggests()
	{
		FakeGearbox gearbox = new FakeGearbox().setPosition(400, 300).setTime(0);
		PlannedMovementController controller = new PlannedMovementController(gearbox);
		FakeRandomNumber random = new FakeRandomNumber();
		OptimalRandomPlanner planner = new OptimalRandomPlanner(new EnemyAnalysis(), controller, random);
		
		ArrayList<MovementPlan> plans = new ArrayList<MovementPlan>();
		plans.add(new MovementPlan().setAhead(Double.POSITIVE_INFINITY).setNumberOfTicks(Integer.MAX_VALUE).setTime(0));
		plans.add(new MovementPlan().setAhead(Double.POSITIVE_INFINITY).setNumberOfTicks(Integer.MAX_VALUE).setTime(0));
		plans.add(new MovementPlan().setAhead(Double.POSITIVE_INFINITY).setNumberOfTicks(Integer.MAX_VALUE).setTime(0));
		
		random.setNext(.1);
		MovementPlan plan = planner.selectPlan(plans, new Wave(3, new EnemyState().setPosition(700, 300).setTime(0)));
		assertEquals(plans.get(0), plan);
		
		random.setNext(.5);
		plan = planner.selectPlan(plans, new Wave(3, new EnemyState().setPosition(700, 300).setTime(0)));
		assertEquals(plans.get(1), plan);

		random.setNext(.9);
		plan = planner.selectPlan(plans, new Wave(3, new EnemyState().setPosition(700, 300).setTime(0)));
		assertEquals(plans.get(2), plan);
	}
	
	@Test public void ShouldSelectTheFirstPlanBecauseItHasAHigherWeightAndRandomNumberIndicatesIt()
	{
		FakeGearbox gearbox = new FakeGearbox().setPosition(400, 300).setTime(0);
		PlannedMovementController controller = new PlannedMovementController(gearbox);
		FakeRandomNumber random = new FakeRandomNumber();
		OptimalRandomPlanner planner = new OptimalRandomPlanner(new EnemyAnalysis(), controller, random);
		
		ArrayList<MovementPlan> plans = new ArrayList<MovementPlan>();
		plans.add(new MovementPlan().setAhead(Double.POSITIVE_INFINITY).setNumberOfTicks(Integer.MAX_VALUE).setTime(0));
		plans.add(new MovementPlan().setAhead(0).setNumberOfTicks(Integer.MAX_VALUE).setTime(0));
		
		random.setNext(.9);
		MovementPlan plan = planner.selectPlan(plans, new Wave(3, new EnemyState().setPosition(700, 300).setTime(0)));
		assertEquals(plans.get(0), plan);
	}
}
