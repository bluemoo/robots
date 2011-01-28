package ncj.tests;

import static org.junit.Assert.*;

import ncj.FiringSolution;
import ncj.Vector2D;

import org.junit.Test;


public class FiringSolutionTests {
	@Test public void ShouldCalculateCorrectBulletPowerForVector() {
		FiringSolution solution = new FiringSolution().setVector(new Vector2D(0, 14));
		assertEquals(2, solution.getPower(), .0001);
	}
	
	@Test public void ShouldCalculateCorrectInterceptPoint() {
		FiringSolution solution = new FiringSolution().setVector(new Vector2D(10, 20))
		                                              .setFiringPoint(new Vector2D(30, 40))
		                                              .setTimeUntilIntercept(4.5);
		Vector2D pIntercept = solution.getInterceptPoint();
		assertEquals(75, pIntercept.getX(), .00001);
		assertEquals(130, pIntercept.getY(), .00001);		
	}
	
	@Test public void ShouldAdjustMyBulletSpeedToPlaceCenterOfBulletAlongDirectPath() {
		
		FiringSolution solution = new FiringSolution()
									.setFiringPoint(new Vector2D(0, 0))
									.setTimeUntilIntercept(10.4)
									.setVector(new Vector2D(15, 0))
									.setWaveVector(new Vector2D(-15, 0));
		
		solution.adjust();
		assertEquals(10.5, solution.getTimeUntilIntercept(), .00001);
		assertEquals(14.71428, solution.getVector().getX(), .001);
	}
	
	@Test public void ShouldAdjustBulletOneTickLater() {
		FiringSolution solution = new FiringSolution()
									.setFiringPoint(new Vector2D(0, 0))
									.setTimeUntilIntercept(10.6)
									.setVector(new Vector2D(19.7, 0))
									.setWaveVector(new Vector2D(-11, 0));
		
		solution.adjust();
		assertEquals(11.5, solution.getTimeUntilIntercept(), .00001);
		assertEquals(17.2973, solution.getVector().getX(), .0001);
	}
	
	@Test public void ShouldCalculateWaveAngleOfFourtyFiveDegrees() {
		FiringSolution solution = new FiringSolution()
									.setWaveVector(new Vector2D(.5,.5))
									.setFiringPoint(new Vector2D(10, 0))
									.setEnemyPoint(new Vector2D(0, 0));
		
		assertEquals(Math.PI/4, solution.getFiringAngle(), .00002);
	}
}
