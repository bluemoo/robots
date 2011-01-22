package ncj.tests;

import static org.junit.Assert.*;

import ncj.SimulatedGearbox;

import org.junit.Test;

public class SimulatedGearboxTests {

	@Test public void ShouldPredictNextState()
	{
		SimulatedGearbox gearbox = new SimulatedGearbox();
		gearbox.setPosition(200,300);
		gearbox.setTurnLeftRadians(30);
		gearbox.setAhead(100);
		
		gearbox.next();
		
		assertEquals(1.0, gearbox.getVelocity(), .00001);
		assertEquals(199.8263518, gearbox.getX(), .000001);
		assertEquals(300.9848077, gearbox.getY(), .000001);
		assertEquals(6.10865238, gearbox.getHeadingRadians(), .000001);
		assertEquals(99, gearbox.getDistanceRemaining(), .000001);
		assertEquals(-29.8254670, gearbox.getTurnRemainingRadians(), .0000001);
		assertEquals(1, gearbox.getTime());
		assertEquals(false, gearbox.getHitWall());
	}
	
	@Test public void ShouldMarkWhenBotHitsWall() {
		SimulatedGearbox gearbox = new SimulatedGearbox();
		gearbox.setPosition(100, 19);
		gearbox.setVelocity(-8);
		gearbox.setAhead(-100);
		
		gearbox.next();
		
		assertEquals(true, gearbox.getHitWall());
		
		gearbox.next();
		
		assertEquals(false, gearbox.getHitWall());
	}
	
	@Test public void ShouldHandleTurningOverMultipleTurns() {
		SimulatedGearbox gearbox = new SimulatedGearbox();
		gearbox.setPosition(100, 100);
		gearbox.setTurnRightRadians(100);
		
		gearbox.next();
		gearbox.next();
		assertEquals(20*Math.PI/180.0, gearbox.getHeadingRadians(), .0001);
	}
}
