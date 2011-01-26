package ncj.Movement;

import ncj.EnemyAnalysis;
import ncj.IGearbox;
import ncj.SimulatedGearbox;
import ncj.Vector2D;
import ncj.Wave;
import robocode.util.Utils;

public class PerpendicularMovementController extends PlannedMovementController {

	private EnemyAnalysis _enemy;
	private double _currentDirection = Double.POSITIVE_INFINITY;
	
	public PerpendicularMovementController(IGearbox gearbox, EnemyAnalysis enemyAnalysis) {
		this(gearbox, enemyAnalysis, null);
	}

	public PerpendicularMovementController(IGearbox gearbox,
			EnemyAnalysis enemyAnalysis, IWallSmoothing wallSmoothing) {
		super(gearbox, wallSmoothing);
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
	
	public MovementPlan calculatePlan(Wave wave) {
		
		IGearbox lastPlanned = getLastPlannedState();
		double rotation = calculateRotation(wave, lastPlanned);			
	
		boolean ranIntoWall = false;
		SimulatedGearbox simulation = run_until_wave_hits_or_wall(wave, rotation);	
		ranIntoWall = simulation.getHitWall();
		
		if(ranIntoWall) {
			double smoothedRotation = smoothed_rotation(simulation, lastPlanned.getHeadingRadians());
			
			simulation = run_until_wave_hits_or_wall(wave, smoothedRotation);	
			ranIntoWall = simulation.getHitWall();
				
			if(ranIntoWall == false)
				rotation = smoothedRotation;
		}
		
		if(ranIntoWall) {
			_currentDirection *= -1;	
			simulation = run_until_wave_hits_or_wall(wave, rotation);
		}
		
		long elapsedTime = simulation.getTime() - lastPlanned.getTime();
		if( elapsedTime > 0)
			return new MovementPlan().setAhead(getCurrentDirection()).setTurn(rotation).setNumberOfTicks(elapsedTime).setTime(lastPlanned.getTime());
		
		return null;
	}

	private SimulatedGearbox run_until_wave_hits_or_wall(Wave wave, double rotation) {
		IGearbox lastPlanned = getLastPlannedState();
		SimulatedGearbox simulation = prepare_simulation(lastPlanned, rotation);
		MovementPlan planToTest = new MovementPlan().setAhead(getCurrentDirection()).setTurn(rotation).setNumberOfTicks(Integer.MAX_VALUE).setTime(lastPlanned.getTime());
		
		PlannedMovementController copiedPlans = new PlannedMovementController(_gearbox);
		copiedPlans.Copy(this);
		copiedPlans.setMovement(planToTest);

		boolean hitWall = false;
		if(!wave.hasHit(simulation))
		{
			for( IGearbox gearbox : copiedPlans.predict_future_position())
			{
				simulation = (SimulatedGearbox)gearbox;
				if(simulation.getHitWall())
					hitWall = true;
				if( wave.hasHit(simulation))
					break;
			}
		}
		simulation.setHitWall(hitWall);
		return simulation;
	}

	public double smoothed_rotation(IGearbox gearbox, double heading) {
		double effectiveHeading = heading;
		if(getCurrentDirection() < 0)
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
		simulation.setAhead(getCurrentDirection());
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

	public double getCurrentDirection() {
		return _currentDirection;
	}
	
	public void setCurrentDirection(double ahead) {
		_currentDirection = ahead;
	}
	
}
