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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.examples.nestedcapabilities;

import jade.core.behaviours.Behaviour;
import bdi4jade.belief.TransientBelief;
import bdi4jade.core.BDIAgent;
import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.util.plan.SimplePlan;

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
