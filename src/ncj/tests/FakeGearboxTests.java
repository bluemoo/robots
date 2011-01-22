package ncj.tests;

import static org.junit.Assert.*;

import ncj.FakeGearbox;
import ncj.IGearbox;

import org.junit.Test;

public class FakeGearboxTests {
	@Test public void ShouldBeAbleToDuplicateAnIGearbox() {
		IGearbox gearbox = new FakeGearbox().setPosition(1, 2).setHeading(3).setVelocity(4).setTime(7);
		gearbox.setAhead(5);
		gearbox.setTurnLeftRadians(6);
		
		FakeGearbox copy = new FakeGearbox().Copy(gearbox);
		assertEquals(1, copy.getX(), .00001);
		assertEquals(2, copy.getY(), .00001);
		assertEquals(3, copy.getHeadingRadians(), .0001);
		assertEquals(4, copy.getVelocity(), .0001);
		assertEquals(5, copy.getDistanceRemaining(), .00001);
		assertEquals(-6, copy.getTurnRemainingRadians(), .00001);
		assertEquals(7, copy.getTime());
	}
}
