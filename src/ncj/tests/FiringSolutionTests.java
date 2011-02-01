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
		                                              .setTimeBetweenFireAndIntercept(4.5);
		Vector2D pIntercept = solution.getInterceptPoint();
		assertEquals(75, pIntercept.getX(), .00001);
		assertEquals(130, pIntercept.getY(), .00001);		
	}
	
	@Test public void ShouldAdjustMyBulletSpeedToPlaceCenterOfBulletAlongDirectPath() {
		
		FiringSolution solution = new FiringSolution()
									.setPointToFireFrom(new Vector2D(0, 0))
									.setTimeBetweenFireAndIntercept(10.4)
									.setIntersectingBullet(new Vector2D(15, 0))
									.setWaveVector(new Vector2D(-15, 0));
		
		solution.adjust();
		assertEquals(10.5, solution.getTimeBetweenFireAndIntercept(), .00001);
		assertEquals(14.71428, solution.getIntersectingBullet().getX(), .001);
	}
	
	@Test public void ShouldAdjustBulletOneTickLater() {
		FiringSolution solution = new FiringSolution()
									.setPointToFireFrom(new Vector2D(0, 0))
									.setTimeBetweenFireAndIntercept(10.6)
									.setIntersectingBullet(new Vector2D(19.7, 0))
									.setWaveVector(new Vector2D(-11, 0));
		
		solution.adjust();
		assertEquals(11.5, solution.getTimeBetweenFireAndIntercept(), .00001);
		assertEquals(17.2973, solution.getIntersectingBullet().getX(), .0001);
	}
	
	@Test public void ShouldCalculateWaveAngleOfFourtyFiveDegrees() {
		FiringSolution solution = new FiringSolution()
									.setWaveVector(new Vector2D(.5,.5))
									.setPointToFireFrom(new Vector2D(10, 0))
									.setEnemyPoint(new Vector2D(0, 0));
		
		assertEquals(Math.PI/4, solution.getMyAngularDisplacement(), .00002);
	}
	
	@Test public void ShouldCalculateShadowVectorsCorrectly()
	{
		FiringSolution solution = new FiringSolution().setPointToFireFrom(new Vector2D(7, 6))
											        .setIntersectingBullet(new Vector2D(3,4))
											        .setTimeBetweenFireAndIntercept(2.3)
											        .setEnemyPoint(new Vector2D(30,10))
											        .setTimeWaveStarted(3)
											        .setTimeToFire(2)
											        .setWaveVector(new Vector2D(10, 0));
											        //.setPointEnemyBulletHits(new Vector2D(100,100));
											        //.setTimeEnemyBulletHits(0)

		assertEquals(1, solution.getShadowTopVector().magnitude(),.0001);
		assertEquals(1, solution.getShadowBottomVector().magnitude(),.0001);
		
		assertEquals(new Vector2D(-.868243, .4961389), solution.getShadowTopVector());
		assertEquals(new Vector2D(-.973417,.22903), solution.getShadowBottomVector());		
	}
	
	@Test public void ShouldCalculateRoughShadowPercentage() {
		FiringSolution solution = new FiringSolution().setPointToFireFrom(new Vector2D(100, 96))
												        .setIntersectingBullet(new Vector2D(0,8))
												        .setTimeBetweenFireAndIntercept(1)
												        .setEnemyPoint(new Vector2D(180,100))
												        .setTimeToFire(0)
												        .setWaveVector(new Vector2D(1,0))
												        .setPointEnemyBulletHits(new Vector2D(100,100));
												        //.setTimeEnemyBulletHits(0)
		//angle blocked = 5.724810452 degrees
		//using bot 'size' of 44
		//     Math.atan(22/distance)*2 = 30.7525025 degrees
		double percent = solution.getShadowPercentage();
		assertEquals(.18615, percent,.00001);
	}
	
	
}
