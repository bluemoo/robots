package ncj;

public class FiringSolution {

	private Vector2D _vInterceptingBullet;
	private long _time;
	private Vector2D _pEnemy;
	private Vector2D _pRobot;
	private Vector2D _pHit;
	private long _timeHit;
	private double _timeUntilIntercept;
	private Vector2D _vWave;

	public Vector2D getIntersectingBullet() {
		return _vInterceptingBullet;
	}

	public FiringSolution setIntersectingBullet(Vector2D v) {
		_vInterceptingBullet = v;
		return this;
	}

	public FiringSolution setTimeToFire(long time) {
		_time = time;
		return this;
	}
	
	public long getTimeToFire() {
		return _time;
	}

	public FiringSolution setPointToFireFrom(Vector2D pRobot) {
		_pRobot = pRobot;
		return this;
	}
	
	public Vector2D getPointToFireFrom() {
		return _pRobot;
	}

	public FiringSolution setPointEnemyBulletHits(Vector2D pHit) {
		_pHit = pHit;
		
		return this;
	}
	
	public Vector2D getPointEnemyBulletHits() {
		return _pHit;
	}

	public FiringSolution setTimeEnemyBulletHits(long time) {
		_timeHit = time;
		return this;
	}
	
	public long getTimeEnemyBulletHits() {
		return _timeHit;
	}

	public FiringSolution setTimeUntilIntercept(double time) {
		_timeUntilIntercept = time;
		return this;
	}
	
	public double getTimeUntilIntercept() {
		return _timeUntilIntercept;
	}

	public double getPower() {
		return (20 - getIntersectingBullet().magnitude())/3;
	}
	
	public Vector2D getInterceptPoint() {
		return getPointToFireFrom().plus(getIntersectingBullet().times(getTimeUntilIntercept()));
	}

	//This changes the power, and thus speed, of our bullet to cause the middle of the bullet
	//to lie on the enemy bullet vector we are trying to intersect.
	public void adjust() {
		double interceptTime = getTimeUntilIntercept();
		Vector2D pIntercept = getInterceptPoint();
		
		double tOffset = Math.floor(interceptTime) - interceptTime - .5;
		interceptTime += tOffset;
		pIntercept = pIntercept.plus(_vWave.times(tOffset));
		
		double speed;
		Vector2D vBullet;
		do {
			interceptTime += 1;
			pIntercept = pIntercept.plus(_vWave);
			
			Vector2D vBulletPath = pIntercept.minus(getPointToFireFrom());
			speed = vBulletPath.magnitude()/interceptTime;
			vBullet = vBulletPath.unit().times(speed);
		} while(speed > 19.7);
		
		setIntersectingBullet(vBullet);
		setTimeUntilIntercept(interceptTime);
	}
	
	public void adjust2() {
		double desiredTime = Math.ceil(getTimeUntilIntercept() - .5) + .5;
		double desiredSpeed = (getTimeUntilIntercept() * getIntersectingBullet().magnitude())/desiredTime;
		
		if( Math.floor(desiredTime) == Math.floor(getTimeUntilIntercept())) {
			setIntersectingBullet(getIntersectingBullet().unit().times(desiredSpeed));
			setTimeUntilIntercept(desiredTime);
		}
	}

	public FiringSolution setWaveVector(Vector2D vWave) {
		_vWave = vWave;
		return this;
	}
	
	public Vector2D getWaveVector() {
		return _vWave;
	}

	public Vector2D getEnemyPoint() {
		return _pEnemy;
	}

	public FiringSolution setEnemyPoint(Vector2D pWaveStart) {
		_pEnemy = pWaveStart;
		return this;
	}

	public double getMyAngularDisplacement() {
		Vector2D vEtoR = _pRobot.minus(_pEnemy);
		double dotProduct = vEtoR.unit().dot(_vWave.unit());
		return Math.acos(dotProduct);
	}

	public double getShadowPercentage() {
		double HALF_BOT_WIDTH = 22;
		
		Vector2D pEnemy = getEnemyPoint();
		double botToWaveDistance = getPointEnemyBulletHits().minus(pEnemy).magnitude();
		double botAngularWidth = Math.atan(HALF_BOT_WIDTH/botToWaveDistance)*2;
		
		double elapsed = getTimeUntilIntercept();
		Vector2D pHead = getPointToFireFrom().plus(getIntersectingBullet().times(elapsed));
		Vector2D pTail = pHead.minus(getIntersectingBullet());

		Vector2D uEnemyToHead = pHead.minus(pEnemy).unit();
		Vector2D uEnemyToTail = pTail.minus(pEnemy).unit();
		double shadowAngularWidth = Math.acos(uEnemyToHead.dot(uEnemyToTail));
		
		return shadowAngularWidth/botAngularWidth;
		
	}
}
