package ncj.tests;

import static org.junit.Assert.*;

import java.util.Iterator;

import ncj.FakeGearbox;
import ncj.IGearbox;
import ncj.MovementPlan;
import ncj.PlannedMovementController;

import org.junit.Before;
import org.junit.Test;


public class PlannedMovementControllerTests {
	PlannedMovementController _controller;
	FakeGearbox _gearbox;
	
	@Before public void Setup() {
		_gearbox = new FakeGearbox();
		_controller = new PlannedMovementController(_gearbox);
	}
	
	@Test public void ShouldReplaySpecifiedMovement() {
		_controller.setMovement( new MovementPlan().setAhead(1).setTurn(Math.PI));
		_controller.next();
		
		assertEquals(1, _gearbox.getDistanceRemaining(), .0001);
		assertEquals(Math.PI, _gearbox.getTurnRemainingRadians(), .0001);
	}
	
	@Test public void DontReplayMovementIfTheTimeIsWrong() {
		_controller.setMovement(new MovementPlan().setTime(1).setAhead(1).setTurn(Math.PI));
		_controller.next();
		
		assertEquals(0, _gearbox.getDistanceRemaining(), .0001);
	}
	
	@Test public void PlayTheCorrectMovementAtTheCorrectTime() {
		_controller.setMovement( new MovementPlan().setTime(3).setAhead(3).setTurn(Math.PI));
		_controller.setMovement( new MovementPlan().setTime(2).setAhead(4).setTurn(Math.PI));
		_gearbox.setTime(3);
		_controller.next();
		
		assertEquals(3, _gearbox.getDistanceRemaining(), .0001);
	}
	
	@Test public void ShouldStopIteratingIfThereIsNoPlan() {
		Iterator<IGearbox> iter = _controller.predict_future_position().iterator();
		assertEquals(false, iter.hasNext());
	}
	
	@Test public void ShouldIterateIfThereIsPlan() {
		_controller.setMovement(new MovementPlan());
		Iterator<IGearbox> iter = _controller.predict_future_position().iterator();
		assertEquals(true, iter.hasNext());
	}
	
	@Test public void ShouldNotIterateIfPlanIsInThePast() {
		_controller.setMovement(new MovementPlan().setTime(1));
		_gearbox.setTime(2);
		Iterator<IGearbox> iter = _controller.predict_future_position().iterator();
		assertEquals(false, iter.hasNext());	
	}
	
	@Test public void ShouldIterateUntilEndOfPlannedTime() {
		_controller.setMovement( new MovementPlan().setTime(3).setNumberOfTicks(3));
		_gearbox.setTime(5);
		Iterator<IGearbox> iter = _controller.predict_future_position().iterator();
		
		assertEquals(true, iter.hasNext());
	
		iter.next();
		assertEquals(false, iter.hasNext());
	}
	
	@Test public void ShouldGetLastPlannedState() {
		_controller.setMovement(new MovementPlan().setTime(10).setNumberOfTicks(20));
		IGearbox gearbox = _controller.getLastPlannedState();
		
		assertEquals(30, gearbox.getTime());
	}
	
	@Test public void ShouldPlayMovementsCorrectlyInSimulation() {
		_controller.setMovement(new MovementPlan().setTime(0).setTurn(100).setNumberOfTicks(3));
		_controller.setMovement(new MovementPlan().setTime(7).setTurn(-100).setNumberOfTicks(3));
		
		IGearbox last = null;
		for(IGearbox gearbox : _controller.predict_future_position()) {
			last = gearbox;
		}
		assertEquals(10, last.getTime());
		assertEquals(.698131, last.getHeadingRadians(), .0001);
	}
	
	@Test public void ShouldIgnoreNullPlans() {
		PlannedMovementController _controller = new PlannedMovementController(new FakeGearbox());
		_controller.setMovement(null);
	}
}
