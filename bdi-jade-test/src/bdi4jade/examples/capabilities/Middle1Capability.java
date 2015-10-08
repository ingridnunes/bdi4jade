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

package bdi4jade.examples.capabilities;

import bdi4jade.annotation.AssociatedCapability;
import bdi4jade.annotation.GoalOwner;
import bdi4jade.annotation.TransientBelief;
import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.Plan;

/**
 * @author Ingrid Nunes
 */
public class Middle1Capability extends Middle1ParentCapability {

	@GoalOwner(capability = Middle1Capability.class, internal = false)
	public static class Middle1ExternalGoal implements Goal {
		private static final long serialVersionUID = -5054184951317760743L;
	}

	@GoalOwner(capability = Middle1Capability.class, internal = true)
	public static class Middle1InternalGoal implements Goal {
		private static final long serialVersionUID = -5054184951317760743L;
	}

	@GoalOwner(capability = Middle1Capability.class, internal = false)
	public static class TestGoal implements Goal {
		private static final long serialVersionUID = -5054184951317760743L;
	}

	private static final long serialVersionUID = -2281419044730158505L;

	@bdi4jade.annotation.Plan
	private Plan externalGoalPlan = new DefaultPlan(Middle1ExternalGoal.class,
			SuccessPlanBody.class);

	@bdi4jade.annotation.Plan
	private Plan internalGoalPlan = new DefaultPlan(Middle1InternalGoal.class,
			SuccessPlanBody.class);

	@TransientBelief
	private String middle1Belief = "MIDDLE1_BELIEF";

	@AssociatedCapability
	private Capability associatedMiddle1Capability = new AssociatedMiddle1Capability();

	@bdi4jade.annotation.Plan
	private Plan testPlan = new DefaultPlan(TestGoal.class, TestPlanBody.class);

}
