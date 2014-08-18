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
import bdi4jade.annotation.PartCapability;
import bdi4jade.annotation.TransientBelief;
import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.Plan;

/**
 * @author Ingrid Nunes
 */
public class Middle1ParentCapability extends Capability {

	@GoalOwner(capability = Middle1ParentCapability.class, internal = true)
	public static class Middle1ParentInternalGoal implements Goal {
		private static final long serialVersionUID = -5054184951317760743L;
	}

	private static final long serialVersionUID = -2281419044730158505L;

	@PartCapability
	private Capability bottomCapability = new BottomCapability();

	@bdi4jade.annotation.Plan
	private Plan internalGoalParentPlan = new DefaultPlan(
			Middle1ParentInternalGoal.class, SuccessPlanBody.class);

	@TransientBelief
	private String middle1ParentBelief = "MIDDLE1_PARENT_BELIEF";

}
