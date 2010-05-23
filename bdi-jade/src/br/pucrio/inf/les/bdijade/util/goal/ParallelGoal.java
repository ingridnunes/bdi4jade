/*
 * Created on 13/12/2009 01:37:21 
 */
package br.pucrio.inf.les.bdijade.util.goal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * This class represents a goal that aims at achieving all goals that compose
 * this goal in a parallel way.
 * 
 * @author ingrid
 */
public class ParallelGoal extends CompositeGoal {

	private static final long serialVersionUID = -8594724445200990207L;

	/**
	 * Creates a new ParallelGoal.
	 * 
	 * @see CompositeGoal#CompositeGoal(Goal[])
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 */
	public ParallelGoal(Goal[] goals) {
		super(goals);
	}

	/**
	 * Creates a new ParallelGoal.
	 * 
	 * @see CompositeGoal#CompositeGoal(Collection)
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 */
	public ParallelGoal(Set<Goal> goals) {
		super(goals);
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.util.goal.CompositeGoal#createGoals()
	 */
	@Override
	protected Collection<Goal> createGoals(int size) {
		return new HashSet<Goal>(size);
	}

}
