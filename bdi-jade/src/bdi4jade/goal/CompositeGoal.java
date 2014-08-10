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

package bdi4jade.goal;

import java.util.Collection;
import java.util.List;

import bdi4jade.event.GoalEvent;

/**
 * This class represents a goal that is a composition of other goals (subgoals).
 * It has two main subclasses, which indicate if the goals must be achieved in a
 * parallel or sequential way.
 * 
 * @author Ingrid Nunes
 */
public abstract class CompositeGoal implements Goal {

	private static final long serialVersionUID = -8253189774672851571L;

	protected List<Goal> completedGoals;
	protected GoalEvent failedGoal;
	protected final Collection<Goal> goals;

	/**
	 * Instantiates a CompositeGoal with the provided goals.
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 */
	public CompositeGoal(Collection<Goal> goals) {
		this.goals = goals;
	}

	/**
	 * Instantiates a CompositeGoal with the provided goals array. A
	 * {@link Collection} is instantiated by the method
	 * {@link #createGoals(int)} and is initialized with the provided goals.
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 */
	public CompositeGoal(Goal[] goals) {
		this.goals = createGoals(goals.length);
		for (Goal goal : goals) {
			this.goals.add(goal);
		}
	}

	/**
	 * Creates an instance of the collection that holds the goals of this
	 * composite goal.
	 * 
	 * @param size
	 *            the size of the collection.
	 * @return the collection.
	 */
	protected abstract Collection<Goal> createGoals(int size);

	/**
	 * Returns the goals of this composite goal that successfully finished.
	 * 
	 * @return the completedGoals the goals that were achieved.
	 */
	public List<Goal> getCompletedGoals() {
		return completedGoals;
	}

	/**
	 * Returns the goal event associated with a goal that could not be achieved,
	 * if any. If it is not possible to achieve one of the goals, the remaining
	 * goals that were not completed yet will not be achieved. If there are
	 * goals part of this composite goal that are being tried to be achieved,
	 * they become no longer desired.
	 * 
	 * @return the goal that failed.
	 */
	public GoalEvent getFailedGoal() {
		return failedGoal;
	}

	/**
	 * Returns the goals associated with this composite goal.
	 * 
	 * @return the goals.
	 */
	public Collection<Goal> getGoals() {
		return goals;
	}

	/**
	 * Sets the list of completed goals.
	 * 
	 * @param completedGoals
	 *            the completedGoals to set.
	 */
	public void setCompletedGoals(List<Goal> completedGoals) {
		this.completedGoals = completedGoals;
	}

	/**
	 * Sets the goal event associated with the goal that failed.
	 * 
	 * @param failedGoal
	 *            the failedGoal to set.
	 */
	public void setFailedGoal(GoalEvent failedGoal) {
		this.failedGoal = failedGoal;
	}

}
