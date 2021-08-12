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
	
	@Override
	public Object getBeliefValue() {
		double value = 0.0;
		if (super.getBeliefValue() instanceof Integer) {
			value = ((Integer) super.getBeliefValue()).doubleValue();
		} else if (super.getBeliefValue() instanceof Double) {
			value = ((Double) super.getBeliefValue()).doubleValue();
		}
		return value;
	}
	
}
