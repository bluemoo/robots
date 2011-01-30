package ncj.tests;

import static org.junit.Assert.*;
import ncj.EnemyAnalysis;
import ncj.EnemyState;
import ncj.FakeGearbox;
import ncj.IGearbox;
import ncj.TargetingComputer;
import ncj.Wave;
import ncj.Movement.MovementPlan;
import ncj.Movement.PerpendicularMovementPlanner;
import ncj.Movement.PlannedMovementController;

import org.junit.Before;
import org.junit.Test;

public class PerpendicularMovementTests {
	FakeGearbox _gearbox;
	PerpendicularMovementPlanner _planner;
	PlannedMovementController _controller;
	
	@Before public void Setup() {
		EnemyAnalysis enemyAnalysis = new EnemyAnalysis();
		_gearbox = new FakeGearbox().setPosition(300, 200);
		_controller = new PlannedMovementController(_gearbox);
		_planner = new PerpendicularMovementPlanner(enemyAnalysis, _controller);
		
		enemyAnalysis.update(new EnemyState().setPosition(300, 50).setEnergy(100));
		enemyAnalysis.update(new EnemyState().setPosition(300, 50).setEnergy(97));		
	}
	
	@Test public void ShouldMovePerpendicularToEnemyFire() {
		_gearbox.setHeading(Math.PI/2);
		
		_planner.plan();
		MovementPlan plan = _controller.getPlans().get((long)0);
		assertEquals(1, _controller.getPlans().size());
		assertEquals(Double.POSITIVE_INFINITY, plan.getAhead(), .0001);
		assertEquals(0, plan.getTurn(), .0001);
		assertEquals(14, plan.getNumberOfTicks());
	}
	
	@Test public void ShouldStartPlanningAtEndOfPreviousPlan() {
		_gearbox.setHeading(Math.PI/2);
		_planner.plan();
		_gearbox.setPosition(310, 200).setVelocity(4).setTime(4);
		
		MovementPlan plan = _planner.calculatePlan(new Wave(3, new EnemyState().setPosition(310, 50).setTime(4)));
		assertEquals(14, plan.getTime());
		
	}
	
	@Test public void ShouldOnlyPlanUntilWaveHits() {
		_gearbox.setHeading(Math.PI/2).setTime(113);
		
		MovementPlan plan = _planner.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 50).setTime(101)));
		assertEquals(1, plan.getNumberOfTicks());
	}
	
	@Test public void ShouldRotateIfNecessary() {
		_gearbox.setHeading(Math.PI/4).setTime(113);
		
		MovementPlan plan = _planner.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 50).setTime(101)));
		assertEquals(Math.PI/4, plan.getTurn(), .0001);
	}
	
	@Test public void ShouldRotateToHeadSouth() {
		_gearbox.setHeading(Math.PI/2);
		MovementPlan plan = _planner.calculatePlan(new Wave(3, new EnemyState().setPosition(50, 200).setTime(100)));
		
		assertEquals(Math.PI/2, plan.getTurn(), .0001);
	}
	
	@Test public void ShouldChooseShortestRotation() {
		_gearbox.setHeading(Math.PI/4);
		MovementPlan plan = _planner.calculatePlan(new Wave(3, new EnemyState().setPosition(50, 200)));
		
		assertEquals(-Math.PI/4, plan.getTurn(), .0001);
	}
	
	@Test public void ShouldReturnNullIfAlreadyPlannedPastImpact() {
		_gearbox.setTime(10);
		MovementPlan plan = _planner.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 200)));
		
		assertEquals(null, plan);
	}

	@Test public void ShouldReturnLastPlannedLocationForHitAt() {
		TargetingComputer targeting = new TargetingComputer(_controller);
		IGearbox gearbox = targeting.hits_at(new Wave(3, new EnemyState().setPosition(300, 100)));
		
		assertEquals(8, gearbox.getTime());
	}
	
	@Test public void ShouldTravelBackwardsIfPlanWouldTakeRobotIntoWall() {
		_gearbox.setPosition(200, 550).setVelocity(8).setAhead(Double.POSITIVE_INFINITY);
		MovementPlan plan = _planner.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 550)));
		
		assertEquals(Double.NEGATIVE_INFINITY, plan.getAhead(), .001);
	}
	
	@Test public void ShouldInitiallyTravelForwards() {
		MovementPlan plan = _planner.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 550)));
		
		assertEquals(Double.POSITIVE_INFINITY, plan.getAhead(), .0001);
	}
	
	@Test public void ShouldTravelForwardsIfPlanWouldTakeRobotIntoWall() {
		_gearbox.setPosition(200, 550).setVelocity(-8).setHeading(Math.PI).setAhead(Double.NEGATIVE_INFINITY);
		MovementPlan plan = _planner.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 550)));
		
		assertEquals(Double.POSITIVE_INFINITY, plan.getAhead(), .0001);	
	}
	
	@Test public void ShouldContinueInNegativeDirection() {
		_gearbox.setPosition(200, 300).setAhead(Double.NEGATIVE_INFINITY);
		MovementPlan plan = _planner.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 300)));
		
		assertEquals(Double.NEGATIVE_INFINITY, plan.getAhead(), .0001);	
	}
	
}
