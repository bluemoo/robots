package ncj.tests;

import static org.junit.Assert.*;

import ncj.EnemyState;
import ncj.FakeGearbox;
import ncj.Wave;

import org.junit.Test;

public class WaveTests {
	@Test public void ShouldCalculateCorrectVelocityForPower() {
		Wave wave = new Wave(3, new EnemyState());
		assertEquals(11, wave.getVelocity(), .00001);
	}
	
	@Test public void ShouldNotHaveHitWhenWaveNotYetAtRobot() {
		FakeGearbox gearbox = new FakeGearbox().setPosition(100, 200);
		Wave wave = new Wave(3, new EnemyState().setPosition(100, 100));
		
		assertEquals(false, wave.hasHit(gearbox));
		
		gearbox.setTime(10);
		assertEquals(true, wave.hasHit(gearbox));
	}
}
