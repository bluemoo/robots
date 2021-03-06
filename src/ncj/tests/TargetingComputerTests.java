package ncj.tests;

import static org.junit.Assert.*;

import ncj.EnemyState;
import ncj.FakeGearbox;
import ncj.FiringSolution;
import ncj.IGearbox;
import ncj.TargetingComputer;
import ncj.Vector2D;
import ncj.Wave;
import ncj.Movement.MovementControllerBase;
import ncj.Movement.UpAndDownMovementController;

import org.junit.Before;
import org.junit.Test;

public class TargetingComputerTests {
	FakeGearbox _current;
	MovementControllerBase _movementController;
	TargetingComputer _targeting;
	
	@Before public void Setup() {
		_current = new FakeGearbox().setPosition(100, 100).setTime(3);
		_movementController = new UpAndDownMovementController(_current);
		_targeting = new TargetingComputer(_movementController);
	}
	

	@Test public void ShouldCalculateStateOfRobotWhenWaveHits() {
		Wave wave = new Wave(3, new EnemyState().setPosition(120, 100).setTime(3));
		IGearbox hit = _targeting.hits_at(wave);
		
		assertEquals(4, hit.getTime());
	}
	
	@Test public void ShouldCalculateFiringSolution()
	{
		_current.setVelocity(8).setAhead(10000);
		Wave wave = new Wave(3, new EnemyState().setPosition(209, 180).setTime(3));
		
		FiringSolution fs = _targeting.solve(wave, _targeting.calculate_firing_location());
		
		Vector2D vector = fs.getIntersectingBullet();
		assertEquals(11.82905, vector.getX(), .00001);
		assertEquals(15.7532, vector.getY(), .00001);
		assertEquals((long)4, fs.getTimeToFire());
		assertEquals((long)3, fs.getTimeWaveStarted());
		assertEquals(209, fs.getEnemyPoint().getX(), .00001);
		assertEquals(100, fs.getPointToFireFrom().getX(), .0001);
		assertEquals(172, fs.getPointEnemyBulletHits().getY(), .0001);
		assertEquals(12, fs.getTimeEnemyBulletHits());
		assertEquals(4.2996, fs.getTimeBetweenFireAndIntercept(),.0001);
		assertEquals(9, fs.getTickOfIntercept());
		assertEquals(-10.9704, fs.getWaveVector().getX(), .0001);
	}

	@Test public void ShouldCalculateInterceptionVectorForAnImmobileRobot()
	{
		Wave wave = new Wave(3, new EnemyState().setPosition(100, 80).setTime(3));
		
		FiringSolution fs = _targeting.solve(wave, _targeting.calculate_firing_location());
		
		Vector2D vector = fs.getIntersectingBullet();
		assertEquals(0, vector.getX(), .00001);
		assertEquals(-19.7, vector.getY(), .00001);
	}

	@Test public void ShouldCalculateInterceptionVectorForRobotLevelWithFiringCoordinateMovingUp()
	{
		_current.setVelocity(8).setPosition(118, 90).setTime(4).setAhead(10000);
		Wave wave = new Wave(3, new EnemyState().setPosition(50, 100).setTime(3));
		
		FiringSolution fs = _targeting.solve(wave, _targeting.calculate_firing_location());
		
		Vector2D vector = fs.getIntersectingBullet();
		assertEquals(8.63638, vector.getY(), .00001);
	}
	
//	@Test public void ShouldAdjustFiringSolutionForAccuracy()
//	{
//		_current.setVelocity(8).setPosition(100, 108).setTime(4).setAhead(10000);
//		Wave wave = new Wave(3, new EnemyState().setPosition(50, 100).setTime(3));
//		
//		FiringSolution fs = _targeting.calculate_firing_solution(wave);
//		
//		assertEquals(1.5, fs.getTimeUntilIntercept(), .00001);
//	}

	@Test public void ShouldNotAdjustFiringSolutionForAccuracy()
	{
		_current.setVelocity(8).setPosition(100, 108).setTime(4).setAhead(10000);
		Wave wave = new Wave(3, new EnemyState().setPosition(50, 100).setTime(3));
		
		FiringSolution fs = _targeting.calculate_firing_solution(wave);
		
		assertEquals(1.02077, fs.getTimeBetweenFireAndIntercept(), .00001);
	}
}
