/*
 * Created on 04/02/2010 18:45:09 
 */
package br.pucrio.inf.les.bdijade.util.goal;

import java.util.Collection;
import java.util.List;

import br.pucrio.inf.les.bdijade.event.GoalFinishedEvent;
import br.pucrio.inf.les.bdijade.goal.Goal;

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
