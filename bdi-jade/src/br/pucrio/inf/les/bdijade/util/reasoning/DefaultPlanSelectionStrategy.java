/*
 * Created on 21/12/2009 12:33:34 
 */
package br.pucrio.inf.les.bdijade.util.reasoning;

import java.util.Set;

import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.plan.Plan;
import br.pucrio.inf.les.bdijade.reasoning.PlanSelectionStrategy;

/**
 * The default implementation of the {@link PlanSelectionStrategy}. It selects
 * the first plan of the set.
 * 
 * @author ingrid
 */
public class DefaultPlanSelectionStrategy implements PlanSelectionStrategy {

	/**
	 * @see br.pucrio.inf.les.bdijade.reasoning.PlanSelectionStrategy#selectPlan(br.pucrio.inf.les.bdijade.goal.Goal,
	 *      java.util.Set)
	 */
	@Override
	public Plan selectPlan(Goal goal, Set<Plan> plans) {
		if (plans.isEmpty())
			return null;
		else
			return plans.iterator().next();
	}

}
