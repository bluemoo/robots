package ncj.tests;

import ncj.Movement.IRandomNumber;

public class FakeRandomNumber implements IRandomNumber {
	double _next;
	
	public double next()
	{
		return _next;
	}
	
	public void setNext(double next)
	{
		_next = next;
	}
}
