package bdi4jade.extension.planselection.learningbased;

import bdi4jade.belief.Belief;

/**
 * An implementation of the abstract class {@link} InfluenceFactor to allow the
 * use of continuous values as influence factor.
 * 
 * @author João Faccin
 */
public class NumericInfluenceFactor extends InfluenceFactor {

	public NumericInfluenceFactor(Belief<?, ?> belief) {
		super(belief);
	}

	@Override
	public String toString() {
		String influenceFactor = "@attribute " + getBeliefName() + " numeric";
		return influenceFactor;
	}
	
}
