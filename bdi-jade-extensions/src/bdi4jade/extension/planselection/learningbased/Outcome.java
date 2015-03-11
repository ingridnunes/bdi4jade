package bdi4jade.extension.planselection.learningbased;

/**
 * An abstraction of an outcome. It represents a measurement that can be taken
 * during and/or after a plan execution.
 * 
 * @author João Faccin
 */
public abstract class Outcome {

	/**
	 * Gets the final measurement of an outcome value.
	 * 
	 * @return An outcome measurement.
	 */
	public abstract double getMeasurement();

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