package ncj;

public class GunController {

	private IGearbox _gearbox;
	private TargetingComputer _targetingComputer;
	private EnemyAnalysis _enemy;
	
	public GunController(IGearbox gearbox, EnemyAnalysis enemy, TargetingComputer targetingComputer) {
		_gearbox = gearbox;
		_targetingComputer = targetingComputer;
		_enemy = enemy;
	}
	
	public void next() {
		Wave wave = null;
		if(_enemy.getNumberActiveWaves() > 0)
		{
			wave = waveToTarget();
			fire_if_time(wave);
		}
		
		FiringSolution solution;
		if( wave == null || wave.getFiringSolution() != null)
		{
			wave = new Wave(3, _enemy.getCurrentState());
			solution = _targetingComputer.calculate_firing_solution(wave); 
		}
		else
		{
			solution = _targetingComputer.calculate_firing_solution(wave); 
			wave.setFiringSolution(solution);
		}
		
		double angle = required_gun_heading(solution);
		_gearbox.setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle( angle - _gearbox.getGunHeadingRadians()));		
	}

	private double required_gun_heading(FiringSolution solution)
	{
		Vector2D vBullet = solution.getVector();
		return vBullet.bearing();		
	}
	
	private void fire_if_time(Wave wave) {
		FiringSolution latestSolution = wave.getFiringSolution();
		if(latestSolution != null && latestSolution.getTime() == _gearbox.getTime()) {
			if(_gearbox.getGunTurnRemainingRadians() == 0)
				_gearbox.setFire(latestSolution.getPower());
			else
			{
				wave.setFiringSolution(null);
				System.out.println("Skipped firing solution at turn: " + _gearbox.getTime());

			}
		}
	}

	public Wave waveToTarget() {
		return _enemy.getLatestWave();
	}


}
