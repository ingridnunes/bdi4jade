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
// http://www.inf.puc-rio.br/~ionunes/
//
//----------------------------------------------------------------------------

package br.pucrio.inf.les.bdi4jade.examples.subgoal;

import java.util.HashSet;
import java.util.Set;

import br.pucrio.inf.les.bdi4jade.core.BeliefBase;
import br.pucrio.inf.les.bdi4jade.core.Capability;
import br.pucrio.inf.les.bdi4jade.core.PlanLibrary;
import br.pucrio.inf.les.bdi4jade.plan.Plan;
import br.pucrio.inf.les.bdi4jade.util.plan.SimplePlan;

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
