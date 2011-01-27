package ncj;

import ncj.Movement.MovementControllerBase;

public class TargetingComputer {

	private MovementControllerBase _movementController;

	public TargetingComputer(MovementControllerBase movementController) {
		_movementController = movementController;
	}

	public TargetingComputer() {
		// TODO Auto-generated constructor stub
	}

	public IGearbox hits_at(Wave wave) {
		
		for( IGearbox futureState : _movementController.predict_future_position())
		{
			if( wave.hasHit(futureState))
				return futureState;
		}
		
		FakeGearbox lastPlanned = new FakeGearbox().Copy(_movementController.getLastPlannedState());
		while( !wave.hasHit(lastPlanned)){
			lastPlanned.setTime(lastPlanned.getTime()+1);
		}
		return lastPlanned;
	}

	public FiringSolution calculate_interception_vector(Wave wave) {
		//Calculates a firing solution for next turn, so the gun has time to aim
		IGearbox nextLocation = calculate_firing_location();
		return solve(wave, nextLocation);
	}
	
	public FiringSolution solve(Wave wave,
			IGearbox firingLocation) {
		long elapsed = firingLocation.getTime() - wave.getTime();
		
		IGearbox hit = hits_at(wave);
		Vector2D pHit = new Vector2D(hit.getX(), hit.getY());
		Vector2D pWaveStart = new Vector2D(wave.getX(), wave.getY());
		Vector2D pRobot = new Vector2D(firingLocation.getX(), firingLocation.getY());
		
		Vector2D vWave = pHit.minus(pWaveStart).unit().times(wave.getVelocity());
		Vector2D pWave = pWaveStart.plus(vWave.times(elapsed));
		Vector2D vRtoW = pWave.minus(pRobot);
		Vector2D uRtoW = vRtoW.unit();
		Vector2D vNormal = uRtoW.calculatePerpendicular();
		
		double normalSpeed = vWave.dot(vNormal)/vNormal.magnitude();
		double bulletSpeed = 19.7;
		double RtoWSpeed = Math.sqrt(bulletSpeed*bulletSpeed - normalSpeed*normalSpeed);
		
		Vector2D solution = uRtoW.times(RtoWSpeed).plus(vNormal.times(normalSpeed));
		
		double waveRtoWSpeed = vWave.dot(uRtoW)/uRtoW.magnitude();
		double timeToIntercept = vRtoW.magnitude()/(RtoWSpeed - waveRtoWSpeed);
		
		return new FiringSolution().setFiringPoint(pRobot)
		                           .setVector(solution)
		                           .setTime(firingLocation.getTime())
		                           .setHitPoint(pHit)
		                           .setHitTime(hit.getTime())
		                           .setTimeUntilIntercept(timeToIntercept)
		                           .setWaveVector(vWave);
	}

	public FiringSolution calculate_firing_solution(Wave wave) {
		//Calculates a firing solution for next turn, so the gun has time to aim
		IGearbox nextLocation = calculate_firing_location();

		FiringSolution solution = solve(wave, nextLocation);
		solution.adjust();
		return solution;
	}

	public IGearbox calculate_firing_location() {
		return _movementController.predict_future_position().iterator().next();
	}

}
