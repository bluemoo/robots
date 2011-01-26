package ncj.tests;

import static org.junit.Assert.*;
import ncj.EnemyAnalysis;
import ncj.FakeGearbox;
import ncj.FakeWallSmoothing;
import ncj.PerpendicularMovementController;

import org.junit.Test;

public class FakeWallSmoothingTests {
	
	@Test public void ShouldMoveUpBecauseThatsWhatTheWallSmootherSaysItShould() {
		EnemyAnalysis enemyAnalysis = new EnemyAnalysis();
		FakeGearbox gearbox = new FakeGearbox().setPosition(300, 200);
		PerpendicularMovementController controller = new PerpendicularMovementController(gearbox, enemyAnalysis, new FakeWallSmoothing());

		controller.next();
		assertEquals(2, gearbox.getTurnRemainingRadians(), .001);
	}
}
