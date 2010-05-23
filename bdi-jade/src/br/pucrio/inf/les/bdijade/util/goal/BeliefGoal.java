/*
 * Created on 27/01/2010 12:16:16 
 */
package br.pucrio.inf.les.bdijade.util.goal;

import br.pucrio.inf.les.bdijade.core.BeliefBase;
import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * This class represents the goal of an agent believe in a certain belief, i.e.
 * the agent has a belief whose name is specified in this goal.
 * 
 * @author ingrid
 */
public class BeliefGoal implements Goal {

	private static final long serialVersionUID = 2493877854717226283L;

	private String beliefName;

	/**
	 * Creates a new BeliefGoal. It considers that the belief name is string
	 * returned from the toString() method of the beliefValue.
	 * 
	 * @param beliefValue
	 *            the belief value whose toString() is the belief name.
	 */
	public BeliefGoal(Object beliefValue) {
		this.beliefName = beliefValue.toString();
	}

	/**
	 * Creates a new BeliefGoal with the provided belief name.
	 * 
	 * @param beliefName
	 *            the belief name.
	 */
	public BeliefGoal(String beliefName) {
		this.beliefName = beliefName;
	}

	/**
	 * @return the beliefName
	 */
	public String getBeliefName() {
		return beliefName;
	}

	/**
	 * Checks if this goal is achieved by verifying if the provided belief base
	 * contains the belief of this goal.
	 * 
	 * @param beliefBase
	 *            the belief base to be checked.
	 * @return true if the belief base contains the belief of this goal.
	 */
	public boolean isAchieved(BeliefBase beliefBase) {
		return beliefBase.hasBelief(beliefName);
	}

}
