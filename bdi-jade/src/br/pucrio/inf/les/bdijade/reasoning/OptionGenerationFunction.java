/*
 * Created on 21/12/2009 12:21:09 
 */
package br.pucrio.inf.les.bdijade.reasoning;

import java.util.Map;
import java.util.Set;

import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.goal.GoalStatus;

/**
 * This interface defines the option generation functions to be used in the
 * BDI-interpreter. This strategy is used for creating new goals or to drop
 * existing ones.
 * 
 * @author ingrid
 */
public interface OptionGenerationFunction {

	/**
	 * The goals parameter is a map of all goals of the agent (that might be
	 * intentions) with their corresponding status. A set is returned of this
	 * function indicating the creating of new goals and the ones that continue
	 * to be goals. The non-selected goals will be no longer desired.
	 * 
	 * @param goals
	 *            the current goals with their status.
	 * @return the list of selected goals.
	 */
	public Set<Goal> generateGoals(Map<Goal, GoalStatus> goals);

}
