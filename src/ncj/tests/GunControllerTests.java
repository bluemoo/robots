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
	private int _numberActiveWaves = 1;
	private boolean _bulletFired;
	FiringSolution _solution;
	
	@Before public void Setup() {
		_current = new FakeGearbox().setPosition(100, 100).setTime(3);
		_solution = new FiringSolution().setVector( new Vector2D(Math.cos(Math.PI*1.25), Math.sin(Math.PI*1.25)))
									    .setTime(4)
									    .setHitTime(10);
		_targetingComputer = new FakeTargetingComputer(_solution);
		
		_enemy = new EnemyAnalysis() {
			@Override public int getNumberActiveWaves() {
				return _numberActiveWaves;
			}
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
	
	@Test public void ShouldNotThrowExceptionIfThereAreNoWaves()
	{
		_numberActiveWaves = 0;
		_gun.next();
	}
	
	@Test public void ShouldReturnLatestWave() {
		Wave wave = _gun.waveToTarget();
		assertEquals(_lastWave, wave);
	}
	
	@Test public void ShouldUseSelectedWave() {
		_gun.next();
		assertEquals(_enemy.getLatestWave(), _targetingComputer.getWaveTargeted());
	}
	
	@Test public void ShouldStoreFiringSolutionOnWave() {
		_gun.next();
		assertEquals(_solution, _enemy.getLatestWave().getFiringSolution());
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
		_current.setTime(4);
		_current.setTurnGunRightRadians(0);
		_gun.next();
		assertEquals(true, _current.getWasFired());
		
		_current.clearFired();
		_gun.next();
		assertEquals(false, _current.getWasFired());
	}
	
	@Test public void ShouldNotChangeTheSolutionForWave() {
		_gun.next();
		_bulletFired = false;
		_gun.next();
		assertEquals(_solution, _lastWave.getFiringSolution());
	}
	
	@Test public void ShouldUseSpecifiedBulletPower() {
		_gun.next();
		_bulletFired = false;
		FiringSolution solution =  new FiringSolution().setVector(new Vector2D(11, 0)).setTime(_current.getTime());
		_lastWave.setFiringSolution(solution);
		_current.setTurnGunRightRadians(0);
		_gun.next();
		assertEquals(3, _current.getFired(), .00001);
	}
	

	@Test public void ShouldDiscardSolutionAndMakeNewOneIfTurrentNotAimedCorrectly() {
		FiringSolution solution = new FiringSolution().setVector(new Vector2D(1, 2)).setTime(3);
		_lastWave.setFiringSolution(solution);
		
		_current.setTurnGunRightRadians(1);
		_gun.next();
		assertEquals(-1, _current.getFired(), .001);
		assertNotSame(solution, _lastWave.getFiringSolution());
	}
}
