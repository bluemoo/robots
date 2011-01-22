package ncj.tests;

import static org.junit.Assert.*;

import ncj.EnemyState;
import ncj.FakeGearbox;
import ncj.IGearbox;

import org.junit.Test;

public class EnemyStateTests {
	@Test public void ShouldCalculateEnemyCoordinatesCorrectlyWhenOrientedNorthWithEnemyEast() {
		IGearbox gearbox = new FakeGearbox().setPosition(100, 200);

		double bearingToEnemy = Math.PI/2.0;
		double distance = 10;
		EnemyState state = new EnemyState().setPosition(gearbox, bearingToEnemy, distance);
		
		assertEquals(110, state.getX(), .00001);
		assertEquals(200, state.getY(), .00001);
	}
	
	@Test public void ShouldCalculateEnemyCoordinatesCorrectlyWhenOrientedNorthWithEnemySouth() {
		IGearbox gearbox = new FakeGearbox().setPosition(100, 200);

		double bearingToEnemy = -1 * Math.PI;
		double distance = 20;
		EnemyState state = new EnemyState().setPosition(gearbox, bearingToEnemy, distance);
		
		assertEquals(100, state.getX(), .00001);
		assertEquals(180, state.getY(), .00001);
	}

	@Test public void ShouldCalculateEnemyCoordinatesCorrectlyWhenOrientedNorthEastWithEnemyWest() {
		IGearbox gearbox = new FakeGearbox().setPosition(300, 200).setHeading(Math.PI / 4);

		double bearingToEnemy = -3 * Math.PI / 4.0;
		double distance = 20;
		EnemyState state = new EnemyState().setPosition(gearbox, bearingToEnemy, distance);
		
		assertEquals(280, state.getX(), .00001);
		assertEquals(200, state.getY(), .00001);
	}
	
}
