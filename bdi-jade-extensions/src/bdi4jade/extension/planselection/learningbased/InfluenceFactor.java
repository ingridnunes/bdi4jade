package bdi4jade.extension.planselection.learningbased;

import bdi4jade.belief.Belief;

public class InfluenceFactor {

	private Belief<?, ?> belief;

	public InfluenceFactor(Belief<?, ?> belief) {
		this.belief = belief;
	}

	public String getBeliefName() {
		return (String) this.belief.getName();
	}

	public Object getBeliefValue() {
		return this.belief.getValue();
	}

}