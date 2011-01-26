package ncj;

import java.util.Hashtable;

public class PlannedMovementController extends MovementControllerBase {
	private Hashtable<Long, MovementPlan> _plans = new Hashtable<Long, MovementPlan>(); 
	private long _lastPlannedTime = -1;
	private IWallSmoothing _wallSmoothing;
	
	public PlannedMovementController(IGearbox gearbox) {
		this(gearbox, null);
	}

	public PlannedMovementController(IGearbox gearbox,
			IWallSmoothing wallSmoothing) {
		super(gearbox);
		_wallSmoothing = wallSmoothing;
	}

	@Override
	protected void set_controls(IGearbox gearbox) {
		//We never remove old plans...
		MovementPlan plan = _plans.get(gearbox.getTime());
		if(plan != null) {
			gearbox.setAhead(plan.getAhead());
			gearbox.setTurnRightRadians(plan.getTurn());
		}
		
		if(_wallSmoothing != null)
		{
			_wallSmoothing.smooth(gearbox);
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

	public long getLastPlannedTime() {
		return _lastPlannedTime;
	}
	
	@SuppressWarnings("unchecked")
	public void Copy(PlannedMovementController controller) {
		_plans = (Hashtable<Long, MovementPlan>) controller._plans.clone();
		_lastPlannedTime = controller._lastPlannedTime;
		_wallSmoothing = controller._wallSmoothing;
	}
}
