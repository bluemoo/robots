package ncj.tests;

import static org.junit.Assert.*;

import ncj.FakeGearbox;
import ncj.IGearbox;
import ncj.Movement.MovementControllerBase;
import ncj.Movement.UpAndDownMovementController;

import org.junit.Test;

public class FuturePositionTests {
	@Test public void FirstValueShouldNextStateWithMovementControlsApplied() {
		FakeGearbox gearbox = new FakeGearbox().setPosition(300, 400);
		MovementControllerBase controller = new UpAndDownMovementController(gearbox);
		java.util.Iterator<IGearbox> itr = controller.predict_future_position().iterator();
		
		IGearbox first = itr.next();
		assertEquals(300, first.getX(), .0001);
		assertEquals(401, first.getY(), .0001);
		assertEquals(Double.POSITIVE_INFINITY, first.getDistanceRemaining(), .0001);
		assertEquals(0, gearbox.getDistanceRemaining(), .0001);
		assertEquals(true, itr.hasNext());
	}
	

}
