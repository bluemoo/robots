package ncj.tests;

import static org.junit.Assert.*;

import ncj.FiringSolution;
import ncj.Vector2D;

import org.junit.Test;


public class FiringSolutionTests {
	@Test public void ShouldCalculateCorrectBulletPowerForVector() {
		FiringSolution solution = new FiringSolution().setIntersectingBullet(new Vector2D(0, 14));
		assertEquals(2, solution.getPower(), .0001);
	}
	
	@Test public void ShouldCalculateCorrectInterceptPoint() {
		FiringSolution solution = new FiringSolution().setIntersectingBullet(new Vector2D(10, 20))
		                                              .setPointToFireFrom(new Vector2D(30, 40))
		                                              .setTimeUntilIntercept(4.5);
		Vector2D pIntercept = solution.getInterceptPoint();
		assertEquals(75, pIntercept.getX(), .00001);
		assertEquals(130, pIntercept.getY(), .00001);		
	}
	
	@Test public void ShouldAdjustMyBulletSpeedToPlaceCenterOfBulletAlongDirectPath() {
		
		FiringSolution solution = new FiringSolution()
									.setPointToFireFrom(new Vector2D(0, 0))
									.setTimeUntilIntercept(10.4)
									.setIntersectingBullet(new Vector2D(15, 0))
									.setWaveVector(new Vector2D(-15, 0));
		
		solution.adjust();
		assertEquals(10.5, solution.getTimeUntilIntercept(), .00001);
		assertEquals(14.71428, solution.getIntersectingBullet().getX(), .001);
	}
	
	@Test public void ShouldAdjustBulletOneTickLater() {
		FiringSolution solution = new FiringSolution()
									.setPointToFireFrom(new Vector2D(0, 0))
									.setTimeUntilIntercept(10.6)
									.setIntersectingBullet(new Vector2D(19.7, 0))
									.setWaveVector(new Vector2D(-11, 0));
		
		solution.adjust();
		assertEquals(11.5, solution.getTimeUntilIntercept(), .00001);
		assertEquals(17.2973, solution.getIntersectingBullet().getX(), .0001);
	}
	
	@Test public void ShouldCalculateWaveAngleOfFourtyFiveDegrees() {
		FiringSolution solution = new FiringSolution()
									.setWaveVector(new Vector2D(.5,.5))
									.setPointToFireFrom(new Vector2D(10, 0))
									.setEnemyPoint(new Vector2D(0, 0));
		
		assertEquals(Math.PI/4, solution.getMyAngularDisplacement(), .00002);
	}
	
	@Test public void ShouldCalculateRoughShadowPercentage() {
		FiringSolution solution = new FiringSolution().setPointToFireFrom(new Vector2D(100, 96))
												        .setIntersectingBullet(new Vector2D(0,8))
												        .setTimeUntilIntercept(1)
												        .setEnemyPoint(new Vector2D(180,100))
												        .setPointEnemyBulletHits(new Vector2D(100,100));
												        //.setTimeToFire(0)
												        //.setTimeEnemyBulletHits(0)
												        //.setWaveVector(null);
		//angle blocked = 5.724810452 degrees
		//using bot 'size' of 44
		//     Math.atan(22/distance)*2 = 30.7525025 degrees
		double percent = solution.getShadowPercentage();
		assertEquals(.18615, percent,.00001);
	}
	
}
