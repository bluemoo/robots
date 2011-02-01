package ncj.tests;

import ncj.Solver;
import ncj.Vector2D;

import org.junit.Test;
import static org.junit.Assert.*;

public class SolverTests {
	
	@Test public void ShouldReturnThePointAtTheRightDistance()
	{
		Vector2D v = new Vector2D(8, 0);
		Vector2D pTail = new Vector2D(10, 10);
		Vector2D pEnemy = new Vector2D(50, 10);
		
		assertEquals(new Vector2D(11, 10), Solver.findPointInVectorAtDistance(pTail, v, pEnemy, 39));
	}
	
	@Test public void ShouldReturnThePointAtTheRightDistanceGoingTheOtherWay()
	{
		Vector2D v = new Vector2D(0, -8);
		Vector2D pTail = new Vector2D(10, 50);
		Vector2D pEnemy = new Vector2D(10, 10);
		
		assertEquals(new Vector2D(10,49), Solver.findPointInVectorAtDistance(pTail, v, pEnemy, 39));
	}
	
	@Test public void ShouldReturnNullIfTheVectorIsFullyContained()
	{
		Vector2D v = new Vector2D(8, 0);
		Vector2D pTail = new Vector2D(10, 11);
		Vector2D pEnemy = new Vector2D(10, 10);
		
		assertEquals(null, Solver.findPointInVectorAtDistance(pTail, v, pEnemy, 39));
	}
	
	@Test public void ShouldReturnNullIfTheVectorIsFullyExcluded()
	{
		Vector2D v = new Vector2D(8, 0);
		Vector2D pTail = new Vector2D(10, 100);
		Vector2D pEnemy = new Vector2D(10, 10);
		
		assertEquals(null, Solver.findPointInVectorAtDistance(pTail, v, pEnemy, 0));
	}
	
	@Test public void ShouldReturnTheFrontAndBackOfVector()
	{
		Vector2D v = new Vector2D(1, 2);
		Vector2D pTail = new Vector2D(10, 10);
		Vector2D pEnemy = new Vector2D(10, 15);
		
		Vector2D[] points = Solver.findFrontAndBackPointsInRange(pTail, v, pEnemy, 0, 10);
		Vector2D p1 = points[0];
		Vector2D p2 = points[1];
		assertEquals(new Vector2D(10,10), p1);
		assertEquals(new Vector2D(11, 12), p2);
	}
	
	@Test public void ShouldReturnTheMidpointAndHeadOfVector()
	{
		Vector2D v = new Vector2D(-2, 0);
		Vector2D pTail = new Vector2D(20, 10);
		Vector2D pEnemy = new Vector2D(15, 10);
		
		Vector2D[] points = Solver.findFrontAndBackPointsInRange(pTail, v, pEnemy, 0, 4);
		Vector2D p1 = points[0];
		Vector2D p2 = points[1];
		assertEquals(new Vector2D(19,10), p1);
		assertEquals(new Vector2D(18, 10), p2);
	}
	
	@Test public void ShouldReturnTheMidpointAndTailOfVector()
	{
		Vector2D v = new Vector2D(-2, 0);
		Vector2D pTail = new Vector2D(20, 10);
		Vector2D pEnemy = new Vector2D(15, 10);
		
		Vector2D[] points = Solver.findFrontAndBackPointsInRange(pTail, v, pEnemy, 4, 8);
		Vector2D p1 = points[0];
		Vector2D p2 = points[1];
		assertEquals(new Vector2D(20,10), p1);
		assertEquals(new Vector2D(19, 10), p2);
	}

	@Test public void ShouldReturnTwoPointsInTheVectorIfItSpansTheRanges()
	{
		Vector2D v = new Vector2D(-4, 0);
		Vector2D pTail = new Vector2D(20, 10);
		Vector2D pEnemy = new Vector2D(15, 10);
		
		Vector2D[] points = Solver.findFrontAndBackPointsInRange(pTail, v, pEnemy, 2, 4);
		Vector2D p1 = points[0];
		Vector2D p2 = points[1];
		assertEquals(new Vector2D(19,10), p1);
		assertEquals(new Vector2D(17, 10), p2);
	}
}
