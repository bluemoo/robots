package ncj.Movement;

import ncj.IGearbox;

public class FakeWallSmoothing implements IWallSmoothing {

	public void smooth(IGearbox gearbox) {
		gearbox.setTurnRightRadians(2);
	}

}
