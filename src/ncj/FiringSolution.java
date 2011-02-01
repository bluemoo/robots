package ncj;

public class FiringSolution {

	private Vector2D _vInterceptingBullet;
	private long _time;
	private Vector2D _pEnemy;
	private Vector2D _pRobot;
	private Vector2D _pHit;
	private long _timeHit;
	private double _timeUntilIntercept;
	private long _timeWaveStarted;
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

	public FiringSolution setTimeBetweenFireAndIntercept(double time) {
		_timeUntilIntercept = time;
		return this;
	}
	
	public double getTimeBetweenFireAndIntercept() {
		return _timeUntilIntercept;
	}

	public long getTickOfIntercept() {
		return (long) Math.ceil(getTimeToFire()+getTimeBetweenFireAndIntercept());
	}
	
	
	public double getPower() {
		return (20 - getIntersectingBullet().magnitude())/3;
	}
	
	public Vector2D getInterceptPoint() {
		return getPointToFireFrom().plus(getIntersectingBullet().times(getTimeBetweenFireAndIntercept()));
	}

	//This changes the power, and thus speed, of our bullet to cause the middle of the bullet
	//to lie on the enemy bullet vector we are trying to intersect.
	public void adjust() {
		double interceptTime = getTimeBetweenFireAndIntercept();
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
		setTimeBetweenFireAndIntercept(interceptTime);
	}
	
	public void adjust2() {
		double desiredTime = Math.ceil(getTimeBetweenFireAndIntercept() - .5) + .5;
		double desiredSpeed = (getTimeBetweenFireAndIntercept() * getIntersectingBullet().magnitude())/desiredTime;
		
		if( Math.floor(desiredTime) == Math.floor(getTimeBetweenFireAndIntercept())) {
			setIntersectingBullet(getIntersectingBullet().unit().times(desiredSpeed));
			setTimeBetweenFireAndIntercept(desiredTime);
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
	
	public FiringSolution setTimeWaveStarted(long time)
	{
		_timeWaveStarted = time;
		return this;
	}
	
	public long getTimeWaveStarted()
	{
		return _timeWaveStarted;
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

		Vector2D uEnemyToHead = getShadowTopVector();
		Vector2D uEnemyToTail = getShadowBottomVector();
		double shadowAngularWidth = Math.acos(uEnemyToHead.dot(uEnemyToTail));
		
		return shadowAngularWidth/botAngularWidth;
		
	}
	
	public Vector2D getShadowTopVector()
	{
		Vector2D pEnemy = getEnemyPoint();
		double elapsed = Math.ceil(getTimeBetweenFireAndIntercept());
		Vector2D pHead = getPointToFireFrom().plus(getIntersectingBullet().times(elapsed));

		return pHead.minus(pEnemy).unit();		
	}
	
	public Vector2D getShadowBottomVector()
	{
		Vector2D pEnemy = getEnemyPoint();
		double elapsed = Math.ceil(getTimeBetweenFireAndIntercept());
		Vector2D pHead = getPointToFireFrom().plus(getIntersectingBullet().times(elapsed));
		Vector2D pTail = pHead.minus(getIntersectingBullet());

		return pTail.minus(pEnemy).unit();
	}
}
