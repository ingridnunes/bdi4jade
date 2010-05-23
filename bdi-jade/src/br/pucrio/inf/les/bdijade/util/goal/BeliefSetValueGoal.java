/*
 * Created on 27/01/2010 15:19:27 
 */
package br.pucrio.inf.les.bdijade.util.goal;

import br.pucrio.inf.les.bdijade.belief.BeliefSet;
import br.pucrio.inf.les.bdijade.core.BeliefBase;
import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * This class represents the goal of an agent believe in a belief that contains
 * a certain value, i.e. the agent has a belief set whose name is specified in
 * this goal and it contains the specified value..
 * 
 * @author ingrid
 */
public class BeliefSetValueGoal<T> implements Goal {

	private static final long serialVersionUID = 2493877854717226283L;

	private String beliefSetName;
	private T value;

	/**
	 * Creates a new BeliefSetValueGoal with the provided belief name and a
	 * value. This value represents the one that should be part of the belief
	 * set.
	 * 
	 * @param beliefSetName
	 *            the belief name.
	 * @param value
	 *            the value that is target of this goal.
	 */
	public BeliefSetValueGoal(String beliefSetName, T value) {
		this.beliefSetName = beliefSetName;
		this.value = value;
	}

	/**
	 * @return the beliefSetName
	 */
	public String getBeliefSetName() {
		return beliefSetName;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Checks if this goal is achieved by verifying if the provided belief set
	 * contains the Value of this goal.
	 * 
	 * @param beliefBase
	 *            the belief base to be checked.
	 * @return true if the belief sey contains the value of this goal.
	 */
	@SuppressWarnings("unchecked")
	public boolean isAchieved(BeliefBase beliefBase) {
		BeliefSet<T> beliefSet = (BeliefSet<T>) beliefBase
				.getBelief(beliefSetName);
		if (beliefSet == null) {
			return false;
		} else {
			return beliefSet.hasValue(value);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Belief '" + beliefSetName + "' has value " + value;
	}

}
