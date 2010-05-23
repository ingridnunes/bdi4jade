/*
 * Created on 25/01/2010 22:00:29 
 */
package br.pucrio.inf.les.bdijade.event;

import jade.content.AgentAction;
import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * This class represents an event performed over a goal.
 * 
 * @author ingrid
 */
public class GoalEvent implements AgentAction {

	private static final long serialVersionUID = 8315524257754153164L;

	protected Goal goal;

	/**
	 * Default constructor.
	 */
	public GoalEvent() {

	}

	/**
	 * Creates a new goal event with a goal.
	 * 
	 * @param goal
	 *            the goal of this event.
	 */
	public GoalEvent(Goal goal) {
		this.goal = goal;
	}

	/**
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * @param goal
	 *            the goal to set
	 */
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

}
