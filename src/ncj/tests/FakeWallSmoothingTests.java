package ncj.tests;

import static org.junit.Assert.*;
import ncj.FakeGearbox;
import ncj.Movement.FakeWallSmoothing;
import ncj.Movement.PlannedMovementController;

import org.junit.Test;

public class FakeWallSmoothingTests {
	
	@Test public void ShouldMoveUpBecauseThatsWhatTheWallSmootherSaysItShouldDo() {
		FakeGearbox gearbox = new FakeGearbox().setPosition(300, 200);
		PlannedMovementController controller = new PlannedMovementController(gearbox, new FakeWallSmoothing());

		controller.next();
		assertEquals(2, gearbox.getTurnRemainingRadians(), .001);
	}
}
