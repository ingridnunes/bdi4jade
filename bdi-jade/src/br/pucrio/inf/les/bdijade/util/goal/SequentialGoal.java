/*
 * Created on 04/02/2010 18:40:58 
 */
package br.pucrio.inf.les.bdijade.util.goal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * This class represents a goal that aims at achieving all goals that compose
 * this goal in a sequential way.
 * 
 * @author ingrid
 */
public class SequentialGoal extends CompositeGoal {

	private static final long serialVersionUID = -8594724445200990207L;

	/**
	 * Creates a new SequentialGoal.
	 * 
	 * @see CompositeGoal#CompositeGoal(Goal[])
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 */
	public SequentialGoal(Goal[] goals) {
		super(goals);
	}

	/**
	 * Creates a new SequentialGoal.
	 * 
	 * @see CompositeGoal#CompositeGoal(Collection)
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 */
	public SequentialGoal(List<Goal> goals) {
		super(goals);
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.util.goal.CompositeGoal#createGoals()
	 */
	@Override
	protected Collection<Goal> createGoals(int size) {
		return new ArrayList<Goal>(size);
	}

}
