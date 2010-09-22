/*
 * Created on 22/09/2010 11:53:30. 
 */
package br.pucrio.inf.les.bdijade.exception;

import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * @author ingridnunes
 * 
 */
public class GoalFailedException extends Exception {

	private static final long serialVersionUID = -6082968354395705561L;

	private Goal goal;

	/**
	 * Creates a new instance of GoalFailedException.
	 * 
	 * @param goal
	 *            the goal that failed.
	 */
	public GoalFailedException(Goal goal) {
		this.goal = goal;
	}

	/**
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return "Goal failed exception: " + goal;
	}

}