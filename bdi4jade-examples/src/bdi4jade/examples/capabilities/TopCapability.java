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
import bdi4jade.annotation.PartCapability;
import bdi4jade.annotation.TransientBelief;
import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.Plan;

/**
 * @author Ingrid Nunes
 */
public class TopCapability extends TopParentCapability {

	@GoalOwner(capability = TopCapability.class, internal = false)
	public static class TopExternalGoal implements Goal {
		private static final long serialVersionUID = 1371943799864265143L;
	}

	@GoalOwner(capability = TopCapability.class, internal = true)
	public static class TopInternalGoal implements Goal {
		private static final long serialVersionUID = 1371943799864265143L;
	}

	private static final long serialVersionUID = -8981563986693758609L;

	@AssociatedCapability
	private Capability associatedTopCapability = new AssociatedTopCapability();

	@bdi4jade.annotation.Plan
	private Plan externalGoalPlan = new DefaultPlan(TopExternalGoal.class,
			SuccessPlanBody.class);

	@bdi4jade.annotation.Plan
	private Plan internalGoalPlan = new DefaultPlan(TopInternalGoal.class,
			SuccessPlanBody.class);

	@PartCapability
	private Capability middle1Capability = new Middle1Capability();

	@PartCapability
	private Capability middle2Capability = new Middle2Capability();

	@TransientBelief
	private String topBelief = "TOP_BELIEF";

}
