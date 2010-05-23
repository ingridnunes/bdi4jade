/*
 * Created on 27/01/2010 00:00:15 
 */
package br.pucrio.inf.les.bdijade.util.reasoning;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.goal.GoalStatus;
import br.pucrio.inf.les.bdijade.reasoning.DeliberationFunction;

/**
 * The default implementation of the {@link DeliberationFunction}. It selects
 * all intentions to be tried.
 * 
 * @author ingrid
 */
public class DefaultDeliberationFunction implements DeliberationFunction {

	/**
	 * @see br.pucrio.inf.les.bdijade.reasoning.DeliberationFunction#filter(java.util.Map)
	 */
	@Override
	public Set<Goal> filter(Map<Goal, GoalStatus> goals) {
		return new HashSet<Goal>(goals.keySet());
	}

}
