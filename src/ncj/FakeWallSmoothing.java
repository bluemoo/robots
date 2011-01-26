package ncj;

public class FakeWallSmoothing implements IWallSmoothing {

	public void smooth(IGearbox gearbox) {
		gearbox.setTurnRightRadians(2);
	}

}
