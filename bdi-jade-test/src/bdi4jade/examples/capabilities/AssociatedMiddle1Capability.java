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

import bdi4jade.annotation.GoalOwner;
import bdi4jade.annotation.TransientBelief;
import bdi4jade.goal.Goal;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.Plan;

/**
 * @author Ingrid Nunes
 */
public class AssociatedMiddle1Capability extends
		AssociatedMiddle1ParentCapability {

	@GoalOwner(capability = AssociatedMiddle1Capability.class, internal = false)
	public static class AssociatedMiddle1ExternalGoal implements Goal {
		private static final long serialVersionUID = 7656633869373580240L;
	}

	@GoalOwner(capability = AssociatedMiddle1Capability.class, internal = true)
	public static class AssociatedMiddle1InternalGoal implements Goal {
		private static final long serialVersionUID = 7656633869373580240L;
	}

	private static final long serialVersionUID = 377413312476720846L;

	@TransientBelief
	private String associatedMiddle1Belief = "ASSOCIATED_MIDDLE_1_BELIEF";

	@bdi4jade.annotation.Plan
	private Plan externalGoalPlan = new DefaultPlan(
			AssociatedMiddle1ExternalGoal.class, SuccessPlanBody.class);

	@bdi4jade.annotation.Plan
	private Plan internalGoalPlan = new DefaultPlan(
			AssociatedMiddle1InternalGoal.class, SuccessPlanBody.class);

}
