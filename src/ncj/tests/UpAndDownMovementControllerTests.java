package ncj.tests;

import static org.junit.Assert.*;

import ncj.FakeGearbox;
import ncj.MovementControllerBase;
import ncj.UpAndDownMovementController;

import org.junit.Before;
import org.junit.Test;


public class UpAndDownMovementControllerTests {
	
	FakeGearbox _gearbox;
	MovementControllerBase _controller;
	
	@Before public void Setup()
	{
		_gearbox = new FakeGearbox();
		_controller = new UpAndDownMovementController(_gearbox);
	}
	
	@Test public void ShouldOrientBotVertically()
	{
		_gearbox.setHeading(1.2);
		_controller.next();
		
		assertEquals(-1.2, _gearbox.getTurnRemainingRadians(), .00001);
	}
	
	@Test public void ShouldDriveNorthIfNearSouthEdge()
	{
		_gearbox.setPosition(500, 49);
		
		_controller.next();
		assertEquals(Double.POSITIVE_INFINITY, _gearbox.getDistanceRemaining(), .001);
	}
	
	@Test public void ShouldDriveSouthIfNearNorthEdge()
	{
		_gearbox.setPosition(500, 551);
		
		_controller.next();
		assertEquals(Double.NEGATIVE_INFINITY, _gearbox.getDistanceRemaining(), .001);
	}
	
	@Test public void ShouldNotChangeDirectionIfRobotIsHeadedSouthAndNotNearEdge()
	{
		_gearbox.setPosition(500, 51).setAhead(Double.NEGATIVE_INFINITY);
		
		_controller.next();
		assertEquals(Double.NEGATIVE_INFINITY, _gearbox.getDistanceRemaining(), .001);
		
	}
	
	@Test public void ShouldWaitForRotationToFinishBeforeMoving()
	{
		_gearbox.setHeading(1.2);
		_controller.next();
		assertEquals(0, _gearbox.getDistanceRemaining(), .0001);
	}
	
	@Test public void ShouldBeginMovingWhenAligned()
	{
		_gearbox.setPosition(100, 300);
		_controller.next();
		assertEquals(Double.POSITIVE_INFINITY, _gearbox.getDistanceRemaining(), .001);
	}
	
}
