package ncj;

import java.util.Hashtable;

import robocode.util.Utils;

public class PerpendicularMovementController extends MovementControllerBase {
	protected Hashtable<Long, MovementPlan> _plans = new Hashtable<Long, MovementPlan>(); 
	protected long _lastPlannedTime = -1;
	private EnemyAnalysis _enemy;
	private double _currentDirection = Double.POSITIVE_INFINITY;
	
	public PerpendicularMovementController(IGearbox gearbox, EnemyAnalysis enemyAnalysis) {
		super(gearbox);
		_enemy = enemyAnalysis;
	}

	@Override
	public void next() {
		plan(_gearbox);
		super.next();
	}
	
	private void plan(IGearbox gearbox) {
		if(_enemy.bulletFired()) {
			MovementPlan plan = calculatePlan(_enemy.getLatestWave());
			this.setMovement(plan);
		}
	}

	@Override
	protected void set_controls(IGearbox gearbox) {
		//We never remove old plans...
		MovementPlan plan = _plans.get(gearbox.getTime());
		if(plan != null) {
			gearbox.setAhead(plan.getAhead());
			gearbox.setTurnRightRadians(plan.getTurn());
		}
		
	}
	
	public MovementPlan calculatePlan(Wave wave) {
		
		IGearbox lastPlanned = getLastPlannedState();
		double rotation = calculateRotation(wave, lastPlanned);			
		
		SimulatedGearbox simulation = prepare_simulation(lastPlanned, rotation);
		boolean ranIntoWall = simulation.run_until_wave_hits(wave);
		
		if(ranIntoWall) {
			double smoothedRotation = smoothed_rotation(simulation, lastPlanned.getHeadingRadians());

			simulation = prepare_simulation(lastPlanned, smoothedRotation);
			ranIntoWall = simulation.run_until_wave_hits(wave);
			
			if(ranIntoWall == false)
				rotation = smoothedRotation;
		}
		
		if(ranIntoWall) {
			_currentDirection *= -1;	
			simulation = prepare_simulation(lastPlanned, rotation);
			simulation.run_until_wave_hits(wave);
		}
		
		long elapsedTime = simulation.getTime() - lastPlanned.getTime();
		if( elapsedTime > 0)
			return new MovementPlan().setAhead(_currentDirection).setTurn(rotation).setNumberOfTicks(elapsedTime).setTime(lastPlanned.getTime());
		
		return null;
	}

	public double smoothed_rotation(IGearbox gearbox, double heading) {
		double effectiveHeading = heading;
		if(_currentDirection < 0)
			effectiveHeading = Utils.normalRelativeAngle(heading + Math.PI);
		
		if(gearbox.getY() > 580 || gearbox.getY() < 20)
			if(effectiveHeading > 0)
				return robocode.util.Utils.normalRelativeAngle(Math.PI/2 - effectiveHeading);
			else
				return robocode.util.Utils.normalRelativeAngle(-Math.PI/2 - effectiveHeading);
		if(gearbox.getX() > 780)
			if(effectiveHeading > Math.PI/2)
				return robocode.util.Utils.normalRelativeAngle(Math.PI - effectiveHeading);
			else
				return robocode.util.Utils.normalRelativeAngle(0 - effectiveHeading);
		if(gearbox.getX() < 20)
			if(effectiveHeading < -Math.PI/2)
				return robocode.util.Utils.normalRelativeAngle(Math.PI - effectiveHeading);
			else
				return robocode.util.Utils.normalRelativeAngle(0 - effectiveHeading);
		
		return 0;
	}

	private SimulatedGearbox prepare_simulation(IGearbox lastPlanned,
			double rotation) {
		SimulatedGearbox simulation;
		simulation = new SimulatedGearbox();
		simulation.Copy(lastPlanned);
		simulation.setAhead(_currentDirection);
		simulation.setTurnRightRadians(rotation);
		return simulation;
	}

	private double calculateRotation(Wave wave, IGearbox gearbox) {
		Vector2D pRobot = new Vector2D(gearbox.getX(), gearbox.getY());
		Vector2D pWave = new Vector2D(wave.getX(), wave.getY());
		Vector2D vWtoR = pRobot.minus(pWave);
		Vector2D vDesired = vWtoR.calculatePerpendicular();
		double bearing = Math.PI/2 - Math.atan2(vDesired.getY(), vDesired.getX());
		double rotation = robocode.util.Utils.normalRelativeAngle(bearing - gearbox.getHeadingRadians());
		
		if( Math.abs(rotation) > Math.PI/2)
			rotation = robocode.util.Utils.normalRelativeAngle(rotation + Math.PI);
		return rotation;
	}
	
	public void setMovement( MovementPlan plan) {
		if( plan == null)
			return;
		_lastPlannedTime = plan.getTime() + plan.getNumberOfTicks() - 1;
		_plans.put(plan.getTime(), plan);
		System.out.println("new plan for time: " + plan.getTime() + " - " + _lastPlannedTime);
	}
	
	@Override
	protected boolean hasPlan(long time) {
		return time <= _lastPlannedTime;
	}

	public Hashtable<Long, MovementPlan> getPlans() {
		return _plans;
	}

	public IGearbox getLastPlannedState() {
		IGearbox result = new FakeGearbox().Copy(_gearbox);
		for(IGearbox gearbox : predict_future_position()) {
			result = gearbox;
		}
		return result;
	}

	public double getCurrentDirection() {
		return _currentDirection;
	}
	
	public void setCurrentDirection(double ahead) {
		_currentDirection = ahead;
	}
	
}
