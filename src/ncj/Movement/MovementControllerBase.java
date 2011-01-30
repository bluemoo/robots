package ncj.Movement;

import ncj.FakeGearbox;
import ncj.IGearbox;
import ncj.SimulatedGearbox;

public abstract class MovementControllerBase {

	protected IGearbox _gearbox;

	protected abstract void set_controls(IGearbox gearbox);

	public MovementControllerBase(IGearbox gearbox) {
		_gearbox = gearbox;
	}

	public void next() {
		set_controls(_gearbox);
	}

	public IGearbox getLastPlannedState() {
		return null;
	}
	
	public FuturePosition predict_future_position() {
		return new FuturePosition();
	}
	
	protected boolean hasPlan(long time) {
		return true;
	}
	
	public IGearbox getCurrentGearbox()
	{
		return _gearbox;
	}
	
	public class FuturePosition implements Iterable<SimulatedGearbox> {
		
		public class Iterator implements java.util.Iterator<SimulatedGearbox> {
			SimulatedGearbox _gearbox;
			
			public Iterator(IGearbox gearbox) {
				_gearbox = new SimulatedGearbox();
				_gearbox.Copy(gearbox);
			}
			
			@Override
			public boolean hasNext() {
				return hasPlan(_gearbox.getTime());
			}
	
			@Override
			public SimulatedGearbox next() {
				set_controls(_gearbox);
				_gearbox.next();
				return _gearbox;
			}
	
			@Override
			public void remove() {
			}
			
		}

		@Override
		public java.util.Iterator<SimulatedGearbox> iterator() {
			return new Iterator(new FakeGearbox().Copy(_gearbox));
		}
	}
}