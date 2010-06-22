/*
 * Created on 31/01/2010 18:32:06 
 */
package br.pucrio.inf.les.bdijade.examples.subgoal;

import java.util.HashSet;
import java.util.Set;

import br.pucrio.inf.les.bdijade.core.BeliefBase;
import br.pucrio.inf.les.bdijade.core.Capability;
import br.pucrio.inf.les.bdijade.core.PlanLibrary;
import br.pucrio.inf.les.bdijade.plan.Plan;
import br.pucrio.inf.les.bdijade.util.plan.SimplePlan;

/**
 * @author ingrid
 * 
 */
public class SubgoalCapability extends Capability {

	private static final long serialVersionUID = -4388902481688697669L;

	private static Set<Plan> getPlans() {
		Set<Plan> plans = new HashSet<Plan>();
		plans.add(new SimplePlan(TopLevelGoal.class, ParentPlan.class));
		plans.add(new SimplePlan(TopLevelGoal.class, MyPlan.class));
		plans.add(new SimplePlan(Subgoal.class, ChildPlan.class));
		return plans;
	}

	public SubgoalCapability() {
		super(new BeliefBase(), new PlanLibrary(getPlans()));
	}

	@Override
	protected void setup() {
		myAgent.addGoal(new TopLevelGoal());
	}

}
