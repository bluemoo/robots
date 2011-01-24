package ncj;

import java.util.Hashtable;

public class PlannedMovementController extends MovementControllerBase {
	private Hashtable<Long, MovementPlan> _plans = new Hashtable<Long, MovementPlan>(); 
	private long _lastPlannedTime = -1;
	
	public PlannedMovementController(IGearbox gearbox) {
		super(gearbox);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void set_controls(IGearbox gearbox) {
		//We never remove old plans...
		MovementPlan plan = _plans.get(gearbox.getTime());
		if(plan != null) {
			gearbox.setAhead(plan.getAhead());
			gearbox.setTurnRightRadians(plan.getTurn());
		}
	}

	public void setMovement( MovementPlan plan) {
		if( plan == null)
			return;
		_lastPlannedTime = plan.getTime() + plan.getNumberOfTicks() - 1;
		_plans.put(plan.getTime(), plan);
		System.out.println("new plan for time: " + plan.getTime() + " - " + _lastPlannedTime);
	}
	
	public IGearbox getLastPlannedState() {
		IGearbox result = new FakeGearbox().Copy(_gearbox);
		for(IGearbox gearbox : predict_future_position()) {
			result = gearbox;
		}
		return result;
	}
	
	@Override
	protected boolean hasPlan(long time) {
		return time <= _lastPlannedTime;
	}

	public Hashtable<Long, MovementPlan> getPlans() {
		return _plans;
	}
}
