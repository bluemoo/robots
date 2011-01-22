package ncj.tests;

import static org.junit.Assert.*;

import ncj.EnemyState;
import ncj.FakeGearbox;
import ncj.FiringSolution;
import ncj.IGearbox;
import ncj.MovementControllerBase;
import ncj.UpAndDownMovementController;
import ncj.TargetingComputer;
import ncj.Vector2D;
import ncj.Wave;

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
		
		assertEquals(5, hit.getTime());
	}
	
	@Test public void ShouldCalculateFiringSolution()
	{
		_current.setVelocity(8).setAhead(10000);
		Wave wave = new Wave(3, new EnemyState().setPosition(209, 180).setTime(3));
		
		FiringSolution fs = _targeting.solve(wave, _targeting.calculate_firing_location());
		
		Vector2D vector = fs.getVector();
		assertEquals(11.12708, vector.getX(), .00001);
		assertEquals(16.25663, vector.getY(), .00001);
		assertEquals((long)4, fs.getTime());
		assertEquals(100, fs.getFiringPoint().getX(), .0001);
		assertEquals(180, fs.getHitPoint().getY(), .0001);
		assertEquals(13, fs.getHitTime());
		assertSame(wave, fs.getWave());
		assertEquals(4.4289, fs.getTimeUntilIntercept(),.0001);
		assertEquals(-11, fs.getWaveVector().getX(), .0001);
	}

	@Test public void ShouldCalculateInterceptionVectorForAnImmobileRobot()
	{
		Wave wave = new Wave(3, new EnemyState().setPosition(100, 80).setTime(3));
		
		FiringSolution fs = _targeting.solve(wave, _targeting.calculate_firing_location());
		
		Vector2D vector = fs.getVector();
		assertEquals(0, vector.getX(), .00001);
		assertEquals(-19.7, vector.getY(), .00001);
	}

	@Test public void ShouldCalculateInterceptionVectorForRobotLevelWithFiringCoordinateMovingUp()
	{
		_current.setVelocity(8).setPosition(100, 108).setTime(4).setAhead(10000);
		Wave wave = new Wave(3, new EnemyState().setPosition(50, 100).setTime(3));
		
		FiringSolution fs = _targeting.solve(wave, _targeting.calculate_firing_location());
		
		Vector2D vector = fs.getVector();
		assertEquals(8.49689, vector.getY(), .00001);
	}
	
	@Test public void ShouldAdjustFiringSolutionForAccuracy()
	{
		_current.setVelocity(8).setPosition(100, 108).setTime(4).setAhead(10000);
		Wave wave = new Wave(3, new EnemyState().setPosition(50, 100).setTime(3));
		
		FiringSolution fs = _targeting.calculate_firing_solution(wave);
		
		assertEquals(1.5, fs.getTimeUntilIntercept(), .00001);
	}
}
