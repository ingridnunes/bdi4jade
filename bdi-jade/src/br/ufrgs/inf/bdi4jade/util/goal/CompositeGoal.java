//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes, et al.
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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package br.ufrgs.inf.bdi4jade.util.goal;

import java.util.Collection;
import java.util.List;

import br.ufrgs.inf.bdi4jade.event.GoalFinishedEvent;
import br.ufrgs.inf.bdi4jade.goal.Goal;

/**
 * This class represents a goal that is a composition of other goals (subgoals).
 * It has two subclasses, which indicate if the goals must be achieved in a
 * parallel or sequential way.
 * 
 * @author ingrid
 */
public abstract class CompositeGoal implements Goal {

	private static final long serialVersionUID = -8253189774672851571L;

	protected List<Goal> completedGoals;
	protected GoalFinishedEvent failedGoal;
	protected final Collection<Goal> goals;

	/**
	 * Instantiate a CompositeGoal with the provided goals.
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 */
	public CompositeGoal(Collection<Goal> goals) {
		this.goals = goals;
	}

	/**
	 * Instantiate a CompositeGoal with the provided goals array. A
	 * {@link Collection} is instantiated by the method {@link #createGoals()}
	 * and is initialized with the provided goals.
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
	 * Creates an instance of the collection that will hold the goals of this
	 * composite goal.
	 * 
	 * @param size
	 *            the size of the collection.
	 * @return the collection.
	 */
	protected abstract Collection<Goal> createGoals(int size);

	/**
	 * @return the completedGoals
	 */
	public List<Goal> getCompletedGoals() {
		return completedGoals;
	}

	/**
	 * @return the failedGoal
	 */
	public GoalFinishedEvent getFailedGoal() {
		return failedGoal;
	}

	/**
	 * @return the goals
	 */
	public Collection<Goal> getGoals() {
		return goals;
	}

	/**
	 * @param completedGoals
	 *            the completedGoals to set
	 */
	public void setCompletedGoals(List<Goal> completedGoals) {
		this.completedGoals = completedGoals;
	}

	/**
	 * @param failedGoal
	 *            the failedGoal to set
	 */
	public void setFailedGoal(GoalFinishedEvent failedGoal) {
		this.failedGoal = failedGoal;
	}

}
