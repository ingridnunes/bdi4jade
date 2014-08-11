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
// http://inf.ufrgs.br/prosoft/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.examples.subgoal;

import java.util.HashSet;
import java.util.Set;

import bdi4jade.core.Capability;
import bdi4jade.plan.Plan;
import bdi4jade.plan.SimplePlan;

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
		super(null, getPlans());
	}

	@Override
	protected void setup() {
		myAgent.addGoal(new TopLevelGoal());
	}

}
