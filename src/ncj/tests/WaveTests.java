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
	
	@Test public void ShouldHitRobotWhenWaveCoversTopRightCorner() {
		Wave wave = new Wave(3, new EnemyState().setPosition(118+10.9, 118));
		FakeGearbox gearbox = new FakeGearbox().setPosition(100, 100);
		gearbox.setTime(1);
		
		assertEquals(true, wave.hasHit(gearbox));
	}
	
	@Test public void ShouldHitRobotWhenWaveCoversTopLeftCorner() {
		Wave wave = new Wave(3, new EnemyState().setPosition(82-10.9, 118));
		FakeGearbox gearbox = new FakeGearbox().setPosition(100, 100);
		gearbox.setTime(1);
		
		assertEquals(true, wave.hasHit(gearbox));
	}
	
	@Test public void ShouldHitRobotWhenWaveCoversBottomLeftCorner() {
		Wave wave = new Wave(3, new EnemyState().setPosition(82, 82 - 10.9));
		FakeGearbox gearbox = new FakeGearbox().setPosition(100,100);
		gearbox.setTime(1);
		
		boolean hashit = wave.hasHit(gearbox);
		assertEquals(true, hashit);
	}

	@Test public void ShouldHitRobotWhenWaveCoversBottomRightCorner() {
		Wave wave = new Wave(3, new EnemyState().setPosition(118, 82 - 10.9));
		FakeGearbox gearbox = new FakeGearbox().setPosition(100,100);
		gearbox.setTime(1);
		
		assertEquals(true, wave.hasHit(gearbox));
	}
}
