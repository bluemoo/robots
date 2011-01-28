package ncj.tests;

import static org.junit.Assert.*;

import ncj.EnemyAnalysis;
import ncj.EnemyState;
import ncj.FiringSolution;
import ncj.Vector2D;
import ncj.Wave;

import org.junit.Before;
import org.junit.Test;

public class EnemyAnalysisTests {
	
	EnemyAnalysis _enemy;
	EnemyState _latestEnemyState;
	FiringSolution _solution = new FiringSolution().setEnemyPoint(new Vector2D(0,0)).setFiringPoint(new Vector2D(0, 0)).setWaveVector(new Vector2D(1, 1));
	
	@Before public void Setup() {
		_enemy = new EnemyAnalysis();
		_latestEnemyState = new EnemyState().setEnergy(100).setPosition(50, 60).setTime(10);
		_enemy.update(_latestEnemyState);
	}
	
	@Test public void ShouldIndicateNoBulletFiredIfFirstViewOfEnemy() {

		assertEquals(false, _enemy.bulletFired());
	}
	
	@Test public void ShouldIndicateBulletFiredIfEnergyDropBigEnough() {
		
		_enemy.update(new EnemyState().setEnergy(99.9));
		assertEquals(true, _enemy.bulletFired());
	}
	
	@Test public void ShouldIndicateNoBulletFiredIfEnergyDropTooBig() {
		
		_enemy.update(new EnemyState().setEnergy(96.9));
		assertEquals(false, _enemy.bulletFired());
	}
	
	@Test public void ShouldCreateNewWaveIfBulletFired() {
		_enemy.update(new EnemyState().setEnergy(99));
		
		assertEquals(1, _enemy.waves.size());
		assertEquals(1, _enemy.waves.get(0).getPower(), .0001);
		assertEquals(50, _enemy.waves.get(0).getX(), .00001);
		assertEquals(60, _enemy.waves.get(0).getY(), .00001);
		assertEquals(10, _enemy.waves.get(0).getTime());
	}
	
	@Test public void ShouldRemoveOldWaves() {
		_enemy.waves.add(new Wave(3, new EnemyState()));
		_enemy.waves.add(new Wave(3, new EnemyState().setTime(1)));
		
		_enemy.update(new EnemyState().setTime(101));
		
		assertEquals(1, _enemy.waves.size());
	}
	
	@Test public void ShouldReturnTheLatestWave() {
		Wave wave = new Wave();
		_enemy.waves.add(new Wave());
		_enemy.waves.add(wave);
		
		assertSame(wave, _enemy.getLatestWave());
	}
	
	@Test public void ShouldKnowTheCurrentEnemyState() {
		assertSame(_latestEnemyState, _enemy.getCurrentState());
	}
	
	@Test public void ShouldRemoveWaveWhenItHits() {
		_enemy.waves.add(new Wave(3, new EnemyState().setPosition(300, 300).setTime(3)).setFiringSolution(_solution));
		_enemy.update_hit_by_bullet(13, 411, 300);
		
		assertEquals(0, _enemy.waves.size());
	}
	
	@Test public void ShouldNotRemoveWaveIfItsTooFarAway() {
		_enemy.waves.add(new Wave(3, new EnemyState().setPosition(300, 400).setTime(3)));
		_enemy.update_hit_by_bullet(6, 411, 300);
		
		assertEquals(1, _enemy.waves.size());
	}
	
	@Test public void DontThrowIfNoMatchingWaveFound() {
		_enemy.update_hit_by_bullet(3, 10, 20);
	}
	
	@Test public void ShouldRemoveWaveIfBulletShotDown() {
		_enemy.waves.add(new Wave(3, new EnemyState().setPosition(200, 100).setTime(10)).setFiringSolution(_solution));
		_enemy.update_bullet_hit_bullet(11, 211, 100);
		
		assertEquals(0, _enemy.waves.size());
	}
	
	@Test public void ShouldNotMakeAWaveIfEnergyDropDueToUsShootingEnemy() {
		_enemy.update_bullet_hit(.1);
		_enemy.update(new EnemyState().setEnergy(99.7));
		
		assertEquals(0, _enemy.waves.size());
	}
}
