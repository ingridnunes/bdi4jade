package bdi4jade.extension.planselection.learningbased;

import java.util.ArrayList;

import bdi4jade.belief.Belief;

/**
 * An implementation of the abstract class {@link} InfluenceFactor to allow the
 * use of discrete values as influence factor.
 * 
 * @author João Faccin
 */
public class NominalInfluenceFactor extends InfluenceFactor {

	private ArrayList<String> possibleValues;

	public NominalInfluenceFactor(Belief<?, ?> belief) {
		super(belief);
		this.possibleValues = new ArrayList<String>();
	}

	/**
	 * Creates a new influence factor and maps it to a belief. Also, define a
	 * set of possible values that this influence factor can have.
	 * 
	 * @param belief
	 *            A belief to be mapped.
	 * @param possibleValues
	 *            A set of values that the influence factor can have.
	 */
	public NominalInfluenceFactor(Belief<?, ?> belief,
			ArrayList<String> possibleValues) {
		super(belief);
		this.possibleValues = possibleValues;
	}

	/**
	 * Adds a new value to the set of possible values that the influence factor
	 * can have.
	 * 
	 * @param possibleValue
	 *            A new possible value.
	 */
	public void addPossibleValue(String possibleValue) {
		possibleValues.add(possibleValue);
	}

	@Override
	public String toString() {
		String influenceFactor = "@attribute " + getBeliefName() + " {"
				+ possibleValues.get(0);
		for (int i = 1; i < possibleValues.size(); i++) {
			influenceFactor += "," + possibleValues.get(i);
		}
		influenceFactor += "}";

		return influenceFactor;
	}

}
