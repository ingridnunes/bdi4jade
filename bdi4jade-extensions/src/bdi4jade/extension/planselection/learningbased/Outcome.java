package bdi4jade.extension.planselection.learningbased;

/**
 * An abstraction of an outcome. It represents a measurement that can be taken
 * during and/or after a plan execution.
 * 
 * @author João Faccin
 */
public abstract class Outcome {
	
	private double min;
	private double max;
	
	public Outcome() {
		this.min = 0.0;
		this.max = 0.0;
	}

	/**
	 * Gets the final measurement of an outcome value.
	 * 
	 * @return An outcome measurement.
	 */
	public abstract double getMeasurement();

	public double getMin() {
		return min;
	}

	public void setMin(double min) {
		this.min = min;
	}

	public double getMax() {
		return max;
	}

	public void setMax(double max) {
		this.max = max;
	}

	/**
	 * Used in cases that a measurement is an interval between two values, e.g.
	 * difference between final and initial time.
	 */
	public void startMeasurement() {
	}

	/**
	 * Used in cases that a measurement is an interval between two values, e.g.
	 * difference between final and initial time.
	 */
	public void endMeasurement() {
	};
	
	@Override
	public String toString() {
		String outcome = "@attribute " + this.getClass().getSimpleName() + " numeric";
		return outcome;
	}

}