package ncj;

import java.util.ArrayList;

public class GunController {

	private IGearbox _gearbox;
	private TargetingComputer _targetingComputer;
	private EnemyAnalysis _enemy;
	private ArrayList<FiringSolution> _solutions = new ArrayList<FiringSolution>();
	
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
			_solutions.add(solution);
		}
		
		removeOldSolutions(_solutions);
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
				System.out.println("Skipped firing solution.");

			}
		}
	}

	private void removeOldSolutions(ArrayList<FiringSolution> solutions) {
		ArrayList<FiringSolution> old = new ArrayList<FiringSolution>();
		for(FiringSolution solution : solutions) {
			if( solution.getHitTime() < _gearbox.getTime())
				old.add(solution);
		}
		solutions.removeAll(old);
	}

	public Wave waveToTarget() {
		return _enemy.getLatestWave();
	}

	public ArrayList<FiringSolution> getActiveSolutions() {
		return _solutions ;
	}

	public FiringSolution getLatestSolution() {
		if(_solutions.size() == 0)
			return null;
		return _solutions.get(_solutions.size() - 1);
	}


}
