package ncj.tests;

import static org.junit.Assert.*;

import ncj.EnemyAnalysis;
import ncj.EnemyState;
import ncj.FakeGearbox;
import ncj.FiringSolution;
import ncj.GunController;
import ncj.Vector2D;
import ncj.Wave;

import org.junit.Before;
import org.junit.Test;

public class GunControllerTests {
	
	FakeGearbox _current;
	FakeTargetingComputer _targetingComputer;
	private GunController _gun;
	private EnemyAnalysis _enemy;
	private Wave _lastWave = new Wave();
	private boolean _bulletFired;
	
	@Before public void Setup() {
		_current = new FakeGearbox().setPosition(100, 100).setTime(3);
		_targetingComputer = new FakeTargetingComputer(new FiringSolution()
													   .setVector( new Vector2D(Math.cos(Math.PI*1.25), Math.sin(Math.PI*1.25)))
													   .setTime(4)
													   .setHitTime(10)
													   );
		
		_enemy = new EnemyAnalysis() {
			@Override public Wave getLatestWave() {
				return _lastWave;
			};
			@Override
			public Boolean bulletFired() {
				return _bulletFired;
			}
			@Override
			public EnemyState getCurrentState() {
				return new EnemyState().setPosition(200, 300);
			}
		};
		_bulletFired = true;
		
		_gun = new GunController(_current, _enemy, _targetingComputer);
	}

	@Test public void ShouldSetGunAngleToDownLeft() {

		_gun.next();
		assertEquals(Math.PI*-0.75, _current.getGunTurnRemainingRadians(), .00001 );
		
		_current.setGunHeading(Math.PI);
		_gun.next();
		assertEquals(Math.PI/4.0, _current.getGunTurnRemainingRadians(), .00002);
	}
	
	@Test public void ShouldTargetTheLatestWave()
	{
		Wave wave = _gun.waveToTarget();
		assertSame(_lastWave, wave);
	}
	
	@Test public void ShouldTargetFakeWave() {
		_bulletFired = false;
		Wave wave = _gun.waveToTarget();
		assertEquals(3, wave.getPower(), .0001);
		assertEquals(200, wave.getX(), .0001);
	}
	
	@Test public void ShouldUseSelectedWave() {
		_gun.next();
		assertEquals(_enemy.getLatestWave(), _targetingComputer.getWaveTargeted());
	}
	
	@Test public void ShouldNotFireIfLastTickDidNotHaveRealWave() {
		_bulletFired = false;
		_gun.next();
		_gun.next();
		assertEquals(false, _current.getWasFired());
	}
	
	@Test public void ShouldFireOnlyIfLastTickHadRealWave() {
		_gun.next();
		_bulletFired = false;
		_gun.next();
		assertEquals(true, _current.getWasFired());
		
		_current.clearFired();
		_gun.next();
		assertEquals(false, _current.getWasFired());
	}
	
	@Test public void ShouldStoreRealFiringSolutionsInList() {
		_gun.next();
		assertEquals(1, _gun.getActiveSolutions().size());
		_bulletFired = false;
		_gun.next();
		assertEquals(1, _gun.getActiveSolutions().size());
	}
	
	@Test public void ShouldPruneOldFiringSolutions() {
		FiringSolution solution = new FiringSolution().setHitTime(2);
		FiringSolution solution2 = new FiringSolution().setHitTime(2);
		_gun.getActiveSolutions().add(solution);
		_gun.getActiveSolutions().add(solution2);
		
		_gun.next();
		assertEquals(1, _gun.getActiveSolutions().size());
	}
	
	@Test public void ShouldGetLastActiveSolution() {
		FiringSolution solution = new FiringSolution();
		FiringSolution solution2 = new FiringSolution();
		_gun.getActiveSolutions().add(solution);
		_gun.getActiveSolutions().add(solution2);
		
		assertSame(solution2, _gun.getLatestSolution());
	}
	
	@Test public void ShouldUseSpecifiedBulletPower() {
		_gun.next();
		_bulletFired = false;
		FiringSolution solution =  new FiringSolution().setVector(new Vector2D(11, 0));
		_gun.getActiveSolutions().add(solution);
		_gun.next();
		assertEquals(3, _current.getFired(), .00001);
	}

}