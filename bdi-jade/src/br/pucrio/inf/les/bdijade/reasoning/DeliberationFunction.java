/*
 * Created on 26/01/2010 23:55:34 
 */
package br.pucrio.inf.les.bdijade.reasoning;

import java.util.Map;
import java.util.Set;

import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.goal.GoalStatus;

/**
 * This interface defines the deliberation function to be used in an agent. This
 * strategy is used for selecting a set of goals that must be tried (intentions)
 * from the set of goals.
 * 
 * @author ingrid
 */
public interface DeliberationFunction {

	/**
	 * Selects the goals that must be tried to achieve and the ones that will be
	 * in the waiting status.
	 * 
	 * @param goals
	 *            the list of current goals (that might be intentions).
	 * @return the list of selected goals.
	 */
	public Set<Goal> filter(Map<Goal, GoalStatus> goals);

}
