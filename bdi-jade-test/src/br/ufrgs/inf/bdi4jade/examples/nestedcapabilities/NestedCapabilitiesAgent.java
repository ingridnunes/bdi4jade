/*
 * Created on 19 Oct 2011 14:42:24 
 */
package br.ufrgs.inf.bdi4jade.examples.nestedcapabilities;

import jade.core.behaviours.Behaviour;
import br.ufrgs.inf.bdi4jade.belief.TransientBelief;
import br.ufrgs.inf.bdi4jade.core.BDIAgent;
import br.ufrgs.inf.bdi4jade.core.Capability;
import br.ufrgs.inf.bdi4jade.goal.Goal;
import br.ufrgs.inf.bdi4jade.util.plan.SimplePlan;

class ChildGoal implements Goal {
	private static final long serialVersionUID = 7656633869373580240L;
}

class MyGoal implements Goal {
	private static final long serialVersionUID = -5054184951317760743L;
}

public class NestedCapabilitiesAgent extends BDIAgent {

	public enum Belief {

		CHILD_BELIEF, MY_BELIEF, PARENT_BELIEF, SIBLING_BELIEF;

	}

	private static final long serialVersionUID = 2712019445290687786L;

	private void addBelief(Capability capability, Belief belief) {
		capability.getBeliefBase().addBelief(
				new TransientBelief<String>(belief.name(), belief.name()));
	}

	private void addPlan(Capability capability, Class<? extends Goal> goal,
			Class<? extends Behaviour> planBody) {
		capability.getPlanLibrary().addPlan(new SimplePlan(goal, planBody));
	}

	protected void init() {
		addBelief(getRootCapability(), Belief.PARENT_BELIEF);
		addPlan(getRootCapability(), ParentGoal.class, SuccessPlanBody.class);

		Capability capability = new Capability();
		addBelief(capability, Belief.MY_BELIEF);
		addPlan(capability, TestGoal.class, TestPlanBody.class);
		addPlan(capability, MyGoal.class, SuccessPlanBody.class);

		Capability sibling = new Capability();
		addBelief(sibling, Belief.SIBLING_BELIEF);
		addPlan(sibling, SiblingGoal.class, SuccessPlanBody.class);

		Capability child = new Capability();
		addBelief(child, Belief.CHILD_BELIEF);
		addPlan(child, ChildGoal.class, SuccessPlanBody.class);

		getRootCapability().addChild(capability);
		getRootCapability().addChild(sibling);
		capability.addChild(child);

		addGoal(new TestGoal());
	}
}

class ParentGoal implements Goal {
	private static final long serialVersionUID = 1371943799864265143L;
}

class SiblingGoal implements Goal {
	private static final long serialVersionUID = 7250708504253085098L;
}

class TestGoal implements Goal {
	private static final long serialVersionUID = -5054184951317760743L;
}
