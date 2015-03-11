package bdi4jade.extension.planselection.learningbased;

import bdi4jade.belief.Belief;

/**
 * An abstraction of an influence factor which is context variable and maps to a
 * belief. An influence factor affects plan outcomes.
 * 
 * @author João Faccin
 */
public abstract class InfluenceFactor {

	private Belief<?, ?> belief;

	/**
	 * Creates a new influence factor and maps it to a belief.
	 * 
	 * @param belief
	 *            A belief to be mapped.
	 */
	public InfluenceFactor(Belief<?, ?> belief) {
		this.belief = belief;
	}

	/**
	 * Returns the name of the mapped belief.
	 * 
	 * @return Belief's name
	 */
	public String getBeliefName() {
		return (String) this.belief.getName();
	}

	/**
	 * Returns the value of mapped belief.
	 * 
	 * @return Belief's value.
	 */
	public Object getBeliefValue() {
		return this.belief.getValue();
	}

}