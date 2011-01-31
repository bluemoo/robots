package ncj;

import java.util.Random;

import ncj.Movement.IRandomNumber;

public class RandomNumber implements IRandomNumber {

	Random _rand = new Random();
	
	@Override
	public double next() {
		// TODO Auto-generated method stub
		return _rand.nextDouble();
	}

}
