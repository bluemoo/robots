package ncj.tests;

import ncj.CachedGearbox;
import ncj.FakeGearbox;

import org.junit.Test;
import static org.junit.Assert.*;

public class CachedGearboxTests {

	@Test public void ShouldReturnChangedValueOfX()
	{
		FakeGearbox fake = new FakeGearbox().setPosition(1, 2).setHeading(4);
		fake.setAhead(3);
		fake.setTurnRightRadians(6);
		fake.setVelocity(7);
		
		CachedGearbox gearbox = new CachedGearbox(fake);

		assertEquals(1, gearbox.getX(),.0001);
		assertEquals(2, gearbox.getY(),.0001);
		assertEquals(3, gearbox.getDistanceRemaining(),.0001);
		assertEquals(4, gearbox.getHeadingRadians(),.0001);
		assertEquals(6, gearbox.getTurnRemainingRadians(),.0001);
		assertEquals(7, gearbox.getVelocity(),.0001);

		fake.setPosition(3,4);
		fake.setAhead(1);
		fake.setHeading(1);
		fake.setTurnRightRadians(1);
		fake.setVelocity(1);
		
		assertEquals(1, gearbox.getX(),.0001);
		assertEquals(2, gearbox.getY(),.0001);
		assertEquals(3, gearbox.getDistanceRemaining(),.0001);
		assertEquals(4, gearbox.getHeadingRadians(),.0001);
		assertEquals(6, gearbox.getTurnRemainingRadians(),.0001);
		assertEquals(7, gearbox.getVelocity(),.0001);
	
		gearbox.setAhead(1);
		assertEquals(1, gearbox.getDistanceRemaining(),.0001);
		
		gearbox.setTurnRightRadians(11);
		assertEquals(11, gearbox.getTurnRemainingRadians(),.0001);

		gearbox.setTurnLeftRadians(12);
		assertEquals(-12, gearbox.getTurnRemainingRadians(),.0001);

	}
}
