/*
 * Created on 21/12/2009 12:33:07 
 */
package br.pucrio.inf.les.bdijade.util.reasoning;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.goal.GoalStatus;
import br.pucrio.inf.les.bdijade.reasoning.OptionGenerationFunction;

/**
 * The default implementation of the {@link OptionGenerationFunction}. It
 * selects all goals, therefore none is dropped or created.
 * 
 * @author ingrid
 */
public class DefaultOptionGenerationFunction implements
		OptionGenerationFunction {

	/**
	 * @see br.pucrio.inf.les.bdijade.reasoning.OptionGenerationFunction#generateGoals(java.util.Map)
	 */
	@Override
	public Set<Goal> generateGoals(Map<Goal, GoalStatus> goals) {
		return new HashSet<Goal>(goals.keySet());
	}

}
