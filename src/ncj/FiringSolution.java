package ncj;

public class FiringSolution {

	private Vector2D _vector;
	private long _time;
	private Vector2D _pRobot;
	private Vector2D _pHit;
	private long _timeHit;
	private double _timeUntilIntercept;
	private Vector2D _vWave;

	public Vector2D getVector() {
		return _vector;
	}

	public FiringSolution setVector(Vector2D v) {
		_vector = v;
		return this;
	}

	public FiringSolution setTime(long time) {
		_time = time;
		return this;
	}
	
	public long getTime() {
		return _time;
	}

	public FiringSolution setFiringPoint(Vector2D pRobot) {
		_pRobot = pRobot;
		return this;
	}
	
	public Vector2D getFiringPoint() {
		return _pRobot;
	}

	public FiringSolution setHitPoint(Vector2D pHit) {
		_pHit = pHit;
		
		return this;
	}
	
	public Vector2D getHitPoint() {
		return _pHit;
	}

	public FiringSolution setHitTime(long time) {
		_timeHit = time;
		return this;
	}
	
	public long getHitTime() {
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
		return (20 - getVector().magnitude())/3;
	}
	
	public Vector2D getInterceptPoint() {
		return getFiringPoint().plus(getVector().times(getTimeUntilIntercept()));
	}

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
			
			Vector2D vBulletPath = pIntercept.minus(getFiringPoint());
			speed = vBulletPath.magnitude()/interceptTime;
			vBullet = vBulletPath.unit().times(speed);
		} while(speed > 19.7);
		
		setVector(vBullet);
		setTimeUntilIntercept(interceptTime);
	}
	
	public void adjust2() {
		double desiredTime = Math.ceil(getTimeUntilIntercept() - .5) + .5;
		double desiredSpeed = (getTimeUntilIntercept() * getVector().magnitude())/desiredTime;
		
		if( Math.floor(desiredTime) == Math.floor(getTimeUntilIntercept())) {
			setVector(getVector().unit().times(desiredSpeed));
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
}
