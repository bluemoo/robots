package ncj.tests;

import static org.junit.Assert.*;

import ncj.Vector2D;

import org.junit.Before;
import org.junit.Test;


public class Vector2DTests {
	Vector2D _v1;
	Vector2D _v2;
	Vector2D _v3;
	
	@Before public void Setup() {
		_v1 = new Vector2D(1,2);
		_v2 = new Vector2D(3,10);
		_v3 = new Vector2D(4,5);
	}
	
	@Test public void ShouldCalculateDotProductCorrectly() {
		assertEquals(23, _v1.dot(_v2), .0001);
	}
	
	@Test public void ShouldCalculatePerpendicularCorrectly() {
		Vector2D p = _v3.calculatePerpendicular();
		
		assertEquals(5, p.getX(), .0001);
		assertEquals(-4, p.getY(), .0001);
	}
	
	@Test public void ShouldCalculateDifferenceVectorCorrectly() {
		Vector2D d = _v1.minus(_v2);
		
		assertEquals(-2, d.getX(), .001);
		assertEquals(-8, d.getY(), .0001);
	}

	@Test public void ShouldCalculateAdditionVectorCorrectly() {
		Vector2D d = _v1.plus(_v2);
		
		assertEquals(4, d.getX(), .001);
		assertEquals(12, d.getY(), .0001);
	}
	
	@Test public void ShouldCalculateMagnitudeAndUnitVectorAndAngle() {
		assertEquals(2.236, _v1.magnitude(), .001);
		assertEquals(1, _v1.unit().magnitude(), .001);
		assertEquals(3, _v1.unit().times(3.0).magnitude(), .001);
		assertEquals(-2.677945044588987, new Vector2D(-1, -2).bearing(), .0001);
	}

}
