package ncj;

import java.awt.Color;
import java.awt.Graphics2D;

import ncj.Movement.MovementPlanner;
import ncj.Movement.OptimalRandomPlanner;
import ncj.Movement.PlannedMovementController;
import ncj.Movement.WallSmoothing;

import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.HitByBulletEvent;
import robocode.RoundEndedEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class MoxieBot extends AdvancedRobot {

	PlannedMovementController _movementController;
	MovementPlanner _movementPlanner;
	EnemyAnalysis _enemy;
	CachedGearbox _gearbox;
	GunController _gun;
	private static ILogFile _log;
	private double _damageTaken = 0;
	
	public void run()
	{
		System.out.println(this.getDataDirectory());
		if( _log == null)
			_log = new FakeLogFile();
		//_log = new LogFile(this.getDataFile("enemy_history" + new Date().getTime() + ".txt"));
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setTurnRadarLeftRadians(Double.POSITIVE_INFINITY);

		_gearbox = new CachedGearbox(new Gearbox(this));
		_enemy = new EnemyAnalysis(_log);
		
		_movementController = new PlannedMovementController(_gearbox,  new WallSmoothing());
		//_movementPlanner = new PerpendicularMovementPlanner(_enemy, _movementController);
		//_movementPlanner = new ThreeSixtyMovementPlanner(_enemy, _movementController);
		//_movementPlanner = new StraightLinePlanner(_enemy, _movementController);
		_movementPlanner = new OptimalRandomPlanner(_enemy, _movementController, new RandomNumber());
		
		_gun = new GunController(_gearbox, _enemy, new TargetingComputer(_movementController));
		
		while(true)
		{
			scan();
			execute();
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {
		_gearbox.update();
		adjustRadar(e);
		_enemy.update(new EnemyState(e, _gearbox));
		_movementPlanner.plan();
		_movementController.next();
		_gun.next();
	}

	public void onBulletHit(BulletHitEvent e) {
		_enemy.update_bullet_hit(e.getBullet().getPower());
	}

	public void onHitByBullet(HitByBulletEvent e) {
		_enemy.update_hit_by_bullet(e.getTime(), e.getBullet().getX(), e.getBullet().getY());
		_damageTaken += robocode.Rules.getBulletDamage(e.getPower());
	}

	public void onBulletHitBullet(BulletHitBulletEvent e) {
		_enemy.update_bullet_hit_bullet(e.getTime(), e.getBullet().getX(), e.getBullet().getY());
	}

	
	// This function taken directly from http://robowiki.net/wiki/Radar
	public void adjustRadar(ScannedRobotEvent e) {
	    double radarTurn =
	        // Absolute bearing to target
	        getHeadingRadians() + e.getBearingRadians()
	        // Subtract current radar heading to get turn required
	        - getRadarHeadingRadians();

	    setTurnRadarRightRadians(Utils.normalRelativeAngle(radarTurn));		
	}
	
	public void onPaint(Graphics2D g) {
		g.setColor(Color.gray);
		
		for (Wave wave : _enemy.waves) {
			long elapsed = this.getTime() - wave.getTime();
			double radius = elapsed * wave.getVelocity();
			double x = wave.getX() - radius;
			double y = wave.getY() - radius;
			double insideRadius = radius  - wave.getVelocity();
			double x2 = wave.getX() - insideRadius;
			double y2 = wave.getY() - insideRadius;
			g.drawOval((int)x, (int)y, 2 * (int)radius, 2 * (int)radius);
			g.drawOval((int)x2, (int)y2, 2 * (int)insideRadius, 2 * (int)insideRadius);
		}
		
		for (Wave wave : _enemy.waves) {
			FiringSolution solution = wave.getFiringSolution();
			if(getTime() <= solution.getTimeToFire() || solution.getTimeEnemyBulletHits() < getTime())
				continue;
			
			Vector2D pFire = solution.getPointToFireFrom();
			Vector2D vBullet = solution.getIntersectingBullet();

			long elapsed = this.getTime() - solution.getTimeToFire();
			Vector2D pHead = pFire.plus(vBullet.times(elapsed));
			Vector2D pTail = pHead.minus(vBullet);
			
			g.setColor(Color.RED);
			g.drawLine((int)pHead.getX(), (int)pHead.getY(), (int)pTail.getX(), (int)pTail.getY());

			g.setColor(Color.YELLOW);
			
			Vector2D pHeadIntercept = pFire.plus(vBullet.times(Math.ceil(solution.getTimeBetweenFireAndIntercept())));
			Vector2D pTailIntercept = pHeadIntercept.minus(vBullet);
			elapsed = solution.getTimeEnemyBulletHits()-wave.getTime();
			Vector2D pWaveStart = new Vector2D(wave.getX(), wave.getY());
			Vector2D pShadowEdgeHead = pWaveStart.plus(solution.getShadowTopVector().times(wave.getVelocity()*elapsed));
			Vector2D pShadowEdgeTail = pWaveStart.plus(solution.getShadowBottomVector().times(wave.getVelocity()*elapsed));
			g.drawLine((int)wave.getX(), (int)wave.getY(), (int)pShadowEdgeHead.getX(), (int)pShadowEdgeHead.getY());
			g.drawLine((int)wave.getX(), (int)wave.getY(), (int)pShadowEdgeTail.getX(), (int)pShadowEdgeTail.getY());
			
			g.setColor(Color.BLUE);
			elapsed = this.getTime() - wave.getTime();
			Vector2D vShadowHeadBullet = pHeadIntercept.minus(pWaveStart).unit().times(wave.getVelocity());
			Vector2D vShadowTailBullet = pTailIntercept.minus(pWaveStart).unit().times(wave.getVelocity());
			Vector2D p1Head = pWaveStart.plus(vShadowHeadBullet.times(elapsed));
			Vector2D p1Tail = p1Head.minus(vShadowHeadBullet);
			Vector2D p2Head = pWaveStart.plus(vShadowTailBullet.times(elapsed));
			Vector2D p2Tail = p2Head.minus(vShadowTailBullet);
			g.drawLine((int)p1Head.getX(), (int)p1Head.getY(), (int)p1Tail.getX(), (int)p1Tail.getY());
			g.drawLine((int)p2Head.getX(), (int)p2Head.getY(), (int)p2Tail.getX(), (int)p2Tail.getY());
			
			g.setColor(Color.WHITE);
			Vector2D pHit = solution.getPointEnemyBulletHits(); 
			g.drawRect((int)pHit.getX()-18, (int)pHit.getY() - 18, 35, 35);
		}
		
		g.setColor(Color.green);
		for(IGearbox gearbox : _movementController.predict_future_position()) {
			g.drawRect((int)gearbox.getX()-2, (int)gearbox.getY() - 2, 5, 5);
		}
	}
		
	@Override
	public void onRoundEnded(RoundEndedEvent event) {
		System.out.println("Damage Taken: " + _damageTaken);
		System.out.println("Energy Fired: " + _gun.getEnergyFired());
	}
	
	public void onBattleEnded(BattleEndedEvent e) {
		_log.close();
	}

}
