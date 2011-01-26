package ncj.tests;

import static org.junit.Assert.*;
import ncj.FakeGearbox;
import ncj.Movement.WallSmoothing;

import org.junit.Before;
import org.junit.Test;

public class WallSmoothingTests {

	FakeGearbox _gearbox;
	WallSmoothing _wallSmoothing = new WallSmoothing();
	
	@Before public void Setup()
	{
		_gearbox = new FakeGearbox().setPosition(300, 300).setHeading(1);
		_gearbox.setAhead(Double.POSITIVE_INFINITY);
	}
	
	@Test public void ShouldDoNothingIfRobotIsInCenterOfField()
	{
		_wallSmoothing.smooth(_gearbox);

		assertEquals(0.0, _gearbox.getTurnRemainingRadians(), .01);
	}
	
	@Test public void ShouldRotateNorthIfRobotIsHeadingIntoTheWestWall()
	{
		_gearbox.setPosition(130, 300).setHeading(Math.PI*7.0/4.0);
		
		_wallSmoothing.smooth(_gearbox);
	
		assertEquals(Math.PI/4.0, _gearbox.getTurnRemainingRadians(), .001);
	}
	
	@Test public void ShouldRotateSouthIfRobotIsHeadingIntoTheWestWallGoingSouth()
	{
		_gearbox.setPosition(130, 300).setHeading(Math.PI*5.5/4.0);
		
		_wallSmoothing.smooth(_gearbox);
	
		assertEquals(-Math.PI*1.5/4.0, _gearbox.getTurnRemainingRadians(), .001);
	
	}
	
	@Test public void ShouldNotModifyRotationIfTheRobotIsHeadingAwayFromTheWall()
	{
		_gearbox.setPosition(130, 300).setHeading(Math.PI*7/4.0).setTurnRightRadians(Math.PI);
		
		_wallSmoothing.smooth(_gearbox);
	
		assertEquals(Math.PI, _gearbox.getTurnRemainingRadians(), .00001);
	}
	
	@Test public void ShouldNotModifyRotationIfTheRobotIsHeadingAwayFromTheWallSouth()
	{
		_gearbox.setPosition(130, 300).setHeading(Math.PI*5/4.0).setTurnRightRadians(-Math.PI);
		
		_wallSmoothing.smooth(_gearbox);
	
		assertEquals(-Math.PI, _gearbox.getTurnRemainingRadians(), .00001);
	}
	
	@Test public void ShouldRotateNorthIfRobotIsHeadingIntoTheWestWallWithANegativeDirection()
	{
		_gearbox.setPosition(130, 300).setHeading(Math.PI*3.0/4.0);
		_gearbox.setAhead(Double.NEGATIVE_INFINITY);
		
		_wallSmoothing.smooth(_gearbox);
	
		assertEquals(Math.PI/4.0, _gearbox.getTurnRemainingRadians(), .001);
	}
	
	@Test public void ShouldRotateNorthIfRobotIsHeadingIntoTheEastWallHeadingNorth()
	{
		_gearbox.setPosition(670, 300).setHeading(Math.PI*1.0/4.0);
		
		_wallSmoothing.smooth(_gearbox);
	
		assertEquals(-Math.PI/4.0, _gearbox.getTurnRemainingRadians(), .001);
	}
	
	@Test public void ShouldRotateWestIfRobotIsHeadedNWIntoTheNorthWall()
	{
		_gearbox.setPosition(300, 470).setHeading(Math.PI*7.0/4.0);
		
		_wallSmoothing.smooth(_gearbox);
	
		assertEquals(-Math.PI/4.0, _gearbox.getTurnRemainingRadians(), .001);
	}
	
	@Test public void ShouldRotateWestIfRobotIsHeadedSWIntoTheSouthWall()
	{
		_gearbox.setPosition(300, 130).setHeading(Math.PI*5.0/4.0);
		
		_wallSmoothing.smooth(_gearbox);
	
		assertEquals(Math.PI/4.0, _gearbox.getTurnRemainingRadians(), .001);
	}
}
