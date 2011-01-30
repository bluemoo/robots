package ncj;

import java.util.ArrayList;

public class EnemyAnalysis {

	private EnemyState _lastState;
	private EnemyState _currentState;
	private double _dEnergy;
	public ArrayList<Wave> waves = new ArrayList<Wave>();
	private ILogFile _log;
	
	public EnemyAnalysis() {
		_log = new FakeLogFile();
	}
	
	public EnemyAnalysis(ILogFile log) {
		_log = log;
	}
	
	public void update(EnemyState state) {

		_lastState = _currentState;
		_currentState = state;
		
		if( _lastState != null )
			_dEnergy = _lastState.getEnergy() - state.getEnergy();
		
		removeOldWaves();
		
		if(bulletFired())
			waves.add(new Wave(_dEnergy, _lastState));
		
	}

	private void removeOldWaves() {
		ArrayList<Wave> oldWaves = new ArrayList<Wave>();
		for (Wave wave : waves) {
			if(wave.getTime() < _currentState.getTime() - 100) {
				oldWaves.add(wave);
				_log.write("Missed,,");
			}
		}
		waves.removeAll(oldWaves);
	}

	public Boolean bulletFired() {
		if(_dEnergy >= .09 && _dEnergy <= 3.01)
			return true;
		
		return false;
	}

	public int getNumberActiveWaves()
	{
		return waves.size();
	}
	
	public Wave getLatestWave() {
		return waves.get(waves.size()-1);
	}

	public EnemyState getCurrentState() {
		return _currentState;
	}

	public void update_hit_by_bullet(long l, double x, double y) {
		Wave waveThatHit = find(l, x, y);
		waves.remove(waveThatHit);
		if(waveThatHit != null)
		{
			FiringSolution solution = waveThatHit.getFiringSolution();
			double distance = solution.getPointEnemyBulletHits().minus(solution.getEnemyPoint()).magnitude();
			_log.write("Hit," + solution.getMyAngularDisplacement() + "," + distance);
		}
		else
		{
			System.out.println("Hit by a wave that was not being correctly tracked. Fix this!");
		}
	}
	
	public void update_bullet_hit_bullet(long time, double x, double y) {
		Wave waveThatHit = null;
		FiringSolution solution = null;
		
		waveThatHit = find(time, x, y);
		if(waveThatHit != null)
		{
			waves.remove(waveThatHit);
			solution = waveThatHit.getFiringSolution();
			double distance = solution.getPointEnemyBulletHits().minus(solution.getEnemyPoint()).magnitude();
			_log.write("Intercepted," + solution.getMyAngularDisplacement() + "," + distance);		
		}
		else
		{
			System.out.println("Intercepted a wave that was not being correctly tracked. Fix this!");
		}
	}

	private Wave find(long time, double x, double y) {
		for( Wave wave : waves) {
			double waveDistance = wave.getVelocity()*(time - wave.getTime());
			double dx = x - wave.getX();
			double dy = y - wave.getY();
			double impactDistance = Math.sqrt(dx*dx + dy*dy);
			if(Math.abs(waveDistance - impactDistance) < 41) {
				return wave;
			}
		}
		return null;
	}

	public void update_bullet_hit(double power) {
		double newEnergy = _currentState.getEnergy() - robocode.Rules.getBulletDamage(power);
		_currentState.setEnergy(newEnergy);
	}


}
