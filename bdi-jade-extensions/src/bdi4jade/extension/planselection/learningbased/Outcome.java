package bdi4jade.extension.planselection.learningbased;

public abstract class Outcome {

	private String name;

	public Outcome(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	/*
	 * It may be a function that return the measurement of the outcome. If it's
	 * time measurement, it can be done through calculation of the difference
	 * between final and initial time execution.
	 */
	public abstract double getMeasurement();
	
	public void startMeasurement() {}
	
	public void endMeasurement() {}
	
}