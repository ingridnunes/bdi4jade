/*
 * Created on 04/02/2010 15:53:08 
 */
package br.pucrio.inf.les.bdijade.util;

import br.pucrio.inf.les.bdijade.core.BDIAgent;
import br.pucrio.inf.les.bdijade.core.Capability;
import br.pucrio.inf.les.bdijade.util.goal.ParallelGoal;
import br.pucrio.inf.les.bdijade.util.goal.SequentialGoal;
import br.pucrio.inf.les.bdijade.util.plan.ParallelGoalPlan;
import br.pucrio.inf.les.bdijade.util.plan.SequentialGoalPlan;
import br.pucrio.inf.les.bdijade.util.plan.SimplePlan;

/**
 * This capability is added in all {@link BDIAgent}. It provides default plans.
 * 
 * @author ingrid
 */
public class DefaultCapability extends Capability {

	private static final long serialVersionUID = -2230280269621396198L;

	/**
	 * @see br.pucrio.inf.les.bdijade.core.Capability#setup()
	 */
	@Override
	protected void setup() {
		this.getPlanLibrary().addPlan(
				new SimplePlan(SequentialGoal.class, SequentialGoalPlan.class));
		this.getPlanLibrary().addPlan(
				new SimplePlan(ParallelGoal.class, ParallelGoalPlan.class));
	}

}
