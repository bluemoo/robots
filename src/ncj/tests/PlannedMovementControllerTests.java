package ncj.tests;

import ncj.FakeGearbox;
import ncj.PlannedMovementController;

import org.junit.Test;

public class PlannedMovementControllerTests {

	@Test public void ShouldIgnoreNullPlans() {
		PlannedMovementController _controller = new PlannedMovementController(new FakeGearbox());
		_controller.setMovement(null);
	}
	
}
