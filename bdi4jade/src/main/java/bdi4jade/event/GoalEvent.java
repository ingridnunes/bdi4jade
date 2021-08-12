//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
// To contact the authors:
// http://inf.ufrgs.br/prosoft/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.event;

import jade.content.AgentAction;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;

/**
 * This class represents an event performed over a goal.
 * 
 * @author Ingrid Nunes
 */
public class GoalEvent implements AgentAction {

	private static final long serialVersionUID = 8315524257754153164L;

	protected Goal goal;
	protected GoalStatus status;

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
		this(goal, null);
	}

	/**
	 * Creates a new goal event with a goal.
	 * 
	 * @param goal
	 *            the goal of this event.
	 * @param status
	 *            the goal status.
	 */
	public GoalEvent(Goal goal, GoalStatus status) {
		this.goal = goal;
		this.status = status;
	}

	/**
	 * Returns the goal associated with this event.
	 * 
	 * @return the goal associated with this event.
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * Returns the goal status.
	 * 
	 * @return the status.
	 */
	public GoalStatus getStatus() {
		return status;
	}

	/**
	 * Indicates if this goal event is a goal added event.
	 * 
	 * @return true if this is an event in which a goal was added, false
	 *         otherwise.
	 */
	public boolean isGoalAdded() {
		return status == null;
	}

	/**
	 * Sets the goal associated with this event.
	 * 
	 * @param goal
	 *            the goal to set.
	 */
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

	/**
	 * Sets the goal status.
	 * 
	 * @param status
	 *            the status to set.
	 */
	public void setStatus(GoalStatus status) {
		this.status = status;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getSimpleName()).append("\n");
		sb.append("Goal: ").append(goal).append("\n");
		sb.append("Status: ").append(status);
		return sb.toString();
	}

}
