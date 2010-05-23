/*
 * Created on 31/01/2010 17:45:40 
 */
package br.pucrio.inf.les.bdijade.event;

import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.goal.GoalStatus;

/**
 * @author ingrid
 * 
 */
public class GoalFinishedEvent extends GoalEvent {

	private static final long serialVersionUID = -4790145097443747163L;

	protected GoalStatus status;

	/**
	 * Default constructor.
	 */
	public GoalFinishedEvent() {

	}

	/**
	 * Creates a new goal event with a goal and its status.
	 * 
	 * @param goal
	 *            the goal of this event.
	 * @param status
	 *            the status of the goal.
	 */
	public GoalFinishedEvent(Goal goal, GoalStatus status) {
		super(goal);
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public GoalStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(GoalStatus status) {
		this.status = status;
	}

}
