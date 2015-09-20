package bdi4jade.extension.planselection.learningbased.example;

import bdi4jade.extension.planselection.learningbased.Outcome;

public class Outcome02 extends Outcome {
	
	private static double min;
	private static double max;

	@Override
	public double getMeasurement() {
		return 0;
	}
	
	@Override
	public double getMin() {
		return min;
	}
	
	@Override
	public void setMin(double min) {
		Outcome02.min = min;
	}
	
	@Override
	public double getMax() {
		return max;
	}
	
	@Override
	public void setMax(double max) {
		Outcome02.max = max;
	}
}
