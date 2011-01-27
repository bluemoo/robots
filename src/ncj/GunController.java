package ncj;

import java.util.ArrayList;

public class GunController {

	private IGearbox _gearbox;
	private TargetingComputer _targetingComputer;
	private EnemyAnalysis _enemy;
	private boolean _shouldFire;
	private ArrayList<FiringSolution> _solutions = new ArrayList<FiringSolution>();
	
	public GunController(IGearbox gearbox, EnemyAnalysis enemy, TargetingComputer targetingComputer) {
		_gearbox = gearbox;
		_targetingComputer = targetingComputer;
		_enemy = enemy;
	}
	
	public void next() {
		if(_shouldFire) {
			_gearbox.setFire(getLatestSolution().getPower());
			System.out.println("Power: " + getLatestSolution().getPower() + " Speed: " + getLatestSolution().getVector().magnitude());
			
		}
		
		Wave wave = waveToTarget();
		FiringSolution solution = _targetingComputer.calculate_firing_solution(wave); 
		wave.setFiringSolution(solution);

		removeOldSolutions(_solutions);
		
		if(_shouldFire)
			_solutions.add(solution);

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
			_shouldFire = true;
			return _enemy.getLatestWave();
		}
		
		_shouldFire = false;
		return new Wave(3, _enemy.getCurrentState());
	}

	public ArrayList<FiringSolution> getActiveSolutions() {
		// TODO Auto-generated method stub
		return _solutions ;
	}

	public FiringSolution getLatestSolution() {
		return _solutions.get(_solutions.size() - 1);
	}


}
