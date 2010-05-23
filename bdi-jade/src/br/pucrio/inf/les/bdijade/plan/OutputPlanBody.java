/*
 * Created on 27/01/2010 21:44:09 
 */
package br.pucrio.inf.les.bdijade.plan;

import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * This interface defines that a {@link PlanBody} provides output for a goal
 * that is being achieved. These outputs that are properties of the goal may be
 * set during the plan body execution, but this interface defines a method for
 * excplicit performing this taks of setting outpust.
 * 
 * @author ingrid
 */
public interface OutputPlanBody {

	/**
	 * Sets the output parameters in the goal.
	 * 
	 * @param goal
	 *            the goal whose output parameters are to be set.
	 */
	public void setGoalOutput(Goal goal);

}
