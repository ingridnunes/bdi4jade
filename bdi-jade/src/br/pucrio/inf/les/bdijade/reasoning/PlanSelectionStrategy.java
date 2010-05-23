/*
 * Created on 21/12/2009 12:21:26 
 */
package br.pucrio.inf.les.bdijade.reasoning;

import java.util.Set;

import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.plan.Plan;

/**
 * This interface defines the plan selection strategy to be used in a
 * capability. This strategy is used for selecting a plan from a set of possible
 * options.
 * 
 * @author ingrid
 */
public interface PlanSelectionStrategy {

	/**
	 * Selects a plan to be executed to achieve the given goal.
	 * 
	 * @param goal
	 *            the goal that must be achieved.
	 * @param plans
	 *            the plans that can achieve the goal.
	 * @return the selected plan.
	 */
	public Plan selectPlan(Goal goal, Set<Plan> plans);

}
