package ncj.tests;

import static org.junit.Assert.*;
import ncj.EnemyAnalysis;
import ncj.EnemyState;
import ncj.FakeGearbox;
import ncj.IGearbox;
import ncj.TargetingComputer;
import ncj.Wave;
import ncj.Movement.MovementPlan;
import ncj.Movement.PerpendicularMovementController;

import org.junit.Before;
import org.junit.Test;

public class PerpendicularMovementTests {
	FakeGearbox _gearbox;
	PerpendicularMovementController _controller;
	
	@Before public void Setup() {
		EnemyAnalysis enemyAnalysis = new EnemyAnalysis();
		_gearbox = new FakeGearbox().setPosition(300, 200);
		_controller = new PerpendicularMovementController(_gearbox, enemyAnalysis);
		
		enemyAnalysis.update(new EnemyState().setPosition(300, 50).setEnergy(100));
		enemyAnalysis.update(new EnemyState().setPosition(300, 50).setEnergy(97));		
	}
	
	@Test public void ShouldMovePerpendicularToEnemyFire() {
		_gearbox.setHeading(Math.PI/2);
		
		_controller.next();
		MovementPlan plan = _controller.getPlans().get((long)0);
		assertEquals(1, _controller.getPlans().size());
		assertEquals(Double.POSITIVE_INFINITY, plan.getAhead(), .0001);
		assertEquals(0, plan.getTurn(), .0001);
		assertEquals(17, plan.getNumberOfTicks());
	}
	
	@Test public void ShouldStartPlanningAtEndOfPreviousPlan() {
		_gearbox.setHeading(Math.PI/2);
		_controller.next();
		_gearbox.setPosition(310, 200).setVelocity(4).setTime(4);
		
		MovementPlan plan = _controller.calculatePlan(new Wave(3, new EnemyState().setPosition(310, 50).setTime(4)));
		assertEquals(17, plan.getTime());
		
	}
	
	@Test public void ShouldOnlyPlanUntilWaveHits() {
		_gearbox.setHeading(Math.PI/2).setTime(113);
		
		MovementPlan plan = _controller.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 50).setTime(100)));
		assertEquals(1, plan.getNumberOfTicks());
	}
	
	@Test public void ShouldRotateIfNecessary() {
		_gearbox.setHeading(Math.PI/4).setTime(113);
		
		MovementPlan plan = _controller.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 50).setTime(100)));
		assertEquals(Math.PI/4, plan.getTurn(), .0001);
	}
	
	@Test public void ShouldRotateToHeadSouth() {
		_gearbox.setHeading(Math.PI/2);
		MovementPlan plan = _controller.calculatePlan(new Wave(3, new EnemyState().setPosition(50, 200).setTime(100)));
		
		assertEquals(Math.PI/2, plan.getTurn(), .0001);
	}
	
	@Test public void ShouldChooseShortestRotation() {
		_gearbox.setHeading(Math.PI/4);
		MovementPlan plan = _controller.calculatePlan(new Wave(3, new EnemyState().setPosition(50, 200)));
		
		assertEquals(-Math.PI/4, plan.getTurn(), .0001);
	}
	
	@Test public void ShouldReturnNullIfAlreadyPlannedPastImpact() {
		_gearbox.setTime(10);
		MovementPlan plan = _controller.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 200)));
		
		assertEquals(null, plan);
	}

	@Test public void ShouldReturnLastPlannedLocationForHitAt() {
		TargetingComputer targeting = new TargetingComputer(_controller);
		IGearbox gearbox = targeting.hits_at(new Wave(3, new EnemyState().setPosition(300, 100)));
		
		assertEquals(10, gearbox.getTime());
	}
	
	@Test public void ShouldTravelBackwardsIfPlanWouldTakeRobotIntoWall() {
		_gearbox.setPosition(200, 550).setVelocity(8);
		MovementPlan plan = _controller.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 550)));
		
		assertEquals(Double.NEGATIVE_INFINITY, plan.getAhead(), .001);
		assertEquals(Double.NEGATIVE_INFINITY, _controller.getCurrentDirection(), .0001);
	}
	
	@Test public void ShouldInitiallyTravelForwards() {
		assertEquals(Double.POSITIVE_INFINITY, _controller.getCurrentDirection(), .0001);
	}
	
	@Test public void ShouldTravelForwardsIfPlanWouldTakeRobotIntoWall() {
		_gearbox.setPosition(200, 550).setVelocity(-8).setHeading(Math.PI);
		_controller.setCurrentDirection(Double.NEGATIVE_INFINITY);
		_controller.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 550)));
		
		assertEquals(Double.POSITIVE_INFINITY, _controller.getCurrentDirection(), .0001);	
	}
	
	@Test public void ShouldContinueInNegativeDirection() {
		_gearbox.setPosition(200, 300);
		_controller.setCurrentDirection(Double.NEGATIVE_INFINITY);
		_controller.calculatePlan(new Wave(3, new EnemyState().setPosition(300, 300)));
		
		assertEquals(Double.NEGATIVE_INFINITY, _controller.getCurrentDirection(), .0001);	
	}
	
}
