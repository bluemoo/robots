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
		FiringSolution latestSolution = getLatestSolution();
		if(latestSolution != null && latestSolution.getTime() == _gearbox.getTime()) {
			_gearbox.setFire(latestSolution.getPower());			
		}
		
		Wave wave = waveToTarget();
		FiringSolution solution;
		if( wave == null)
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
		Vector2D vBullet = solution.getVector();
		_gearbox.setTurnGunRightRadians(robocode.util.Utils.normalRelativeAngle(vBullet.bearing() - _gearbox.getGunHeadingRadians()));		
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
		if(_enemy.bulletFired()) {
			return _enemy.getLatestWave();
		}
		return null;
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
