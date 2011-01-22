package ncj.tests;

import ncj.FiringSolution;
import ncj.TargetingComputer;
import ncj.Wave;

public class FakeTargetingComputer extends TargetingComputer {
		
		private Wave _wave;
		private FiringSolution _solution;
		private long _time;

		public FakeTargetingComputer(FiringSolution solutionToReturn) {
			_solution = solutionToReturn;
			_time = solutionToReturn.getTime();
		}
		
		@Override
		public FiringSolution calculate_firing_solution(Wave wave) {
			_wave = wave;
			_solution.setTime(_time);
			_time++;
			return _solution;
		}
		
		public Wave getWaveTargeted() {
			return _wave;
		}
}
