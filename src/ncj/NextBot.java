package ncj;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Date;

import ncj.Movement.MovementControllerBase;
import ncj.Movement.PerpendicularMovementController;
import ncj.Movement.WallSmoothing;

import robocode.AdvancedRobot;
import robocode.BattleEndedEvent;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;
import robocode.util.Utils;

public class NextBot extends AdvancedRobot {

	MovementControllerBase _movementController;
	EnemyAnalysis _enemy;
	Gearbox _gearbox;
	GunController _gun;
	private static LogFile _log;
	
	public void run()
	{
		System.out.println(this.getDataDirectory());
		if( _log == null)
			_log = new LogFile(this.getDataFile("enemy_history" + new Date().getTime() + ".txt"));
		
		setAdjustGunForRobotTurn(true);
		setAdjustRadarForGunTurn(true);
		setTurnRadarLeftRadians(Double.POSITIVE_INFINITY);

		_gearbox = new Gearbox(this);
		_enemy = new EnemyAnalysis(_log);
		
		_movementController = new PerpendicularMovementController(_gearbox, _enemy, new WallSmoothing());
		_gun = new GunController(_gearbox, _enemy, new TargetingComputer(_movementController));
		
		while(true)
		{
			scan();
			execute();
		}
	}
	
	public void onScannedRobot(ScannedRobotEvent e) {

		adjustRadar(e);
		_enemy.update(new EnemyState(e, _gearbox));
		_movementController.next();
		_gun.next();
	}

	public void onBulletHit(BulletHitEvent e) {
		_enemy.update_bullet_hit(e.getBullet().getPower());
	}

	public void onHitByBullet(HitByBulletEvent e) {
		_enemy.update_hit_by_bullet(e.getTime(), e.getBullet().getX(), e.getBullet().getY());
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
			if(getTime() <= solution.getTime() || solution.getHitTime() < getTime())
				continue;
			
			Vector2D pFire = solution.getFiringPoint();
			Vector2D vBullet = solution.getVector();

			long elapsed = this.getTime() - solution.getTime();
			Vector2D pHead = pFire.plus(vBullet.times(elapsed));
			Vector2D pTail = pHead.minus(vBullet);
			
			g.setColor(Color.RED);
			g.drawLine((int)pHead.getX(), (int)pHead.getY(), (int)pTail.getX(), (int)pTail.getY());

			g.setColor(Color.YELLOW);
			
			Vector2D pHeadIntercept = pFire.plus(vBullet.times(Math.ceil(solution.getTimeUntilIntercept())));
			Vector2D pTailIntercept = pHeadIntercept.minus(vBullet);
			elapsed = solution.getHitTime()-wave.getTime();
			Vector2D pWaveStart = new Vector2D(wave.getX(), wave.getY());
			Vector2D pShadowEdgeHead = pWaveStart.plus(pHeadIntercept.minus(pWaveStart).unit().times(wave.getVelocity()*elapsed));
			Vector2D pShadowEdgeTail = pWaveStart.plus(pTailIntercept.minus(pWaveStart).unit().times(wave.getVelocity()*elapsed));
			g.drawLine((int)wave.getX(), (int)wave.getY(), (int)pShadowEdgeHead.getX(), (int)pShadowEdgeHead.getY());
			g.drawLine((int)wave.getX(), (int)wave.getY(), (int)pShadowEdgeTail.getX(), (int)pShadowEdgeTail.getY());
			
			
			Vector2D pHit = solution.getHitPoint();
			g.drawRect((int)pHit.getX()-18, (int)pHit.getY() - 18, 35, 35);
			
		}
		
		g.setColor(Color.green);
		for(IGearbox gearbox : _movementController.predict_future_position()) {
			g.drawRect((int)gearbox.getX()-2, (int)gearbox.getY() - 2, 5, 5);
		}
	}
	
	public void onBattleEnded(BattleEndedEvent e) {
		_log.close();
	}

}
