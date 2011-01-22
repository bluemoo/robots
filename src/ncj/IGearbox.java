package ncj;

public interface IGearbox {
	
	public double getHeadingRadians();
	
	public double getTurnRemainingRadians();

	public void setTurnLeftRadians(double radians);

	public void setAhead(double distance);

	public double getY();

	public double getX();

	public double getDistanceRemaining();

	public double getVelocity();

	public double getBattleFieldWidth();

	public double getBattleFieldHeight();

	public long getTime();
	
	public void setTurnGunRightRadians(double radians);
	
	public double getGunTurnRemainingRadians();
	
	public double getGunHeadingRadians();
	
	public void setFire(double fire);

	public void setTurnRightRadians(double radians);
}
