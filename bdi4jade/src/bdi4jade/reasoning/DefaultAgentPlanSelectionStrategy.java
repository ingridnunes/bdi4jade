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

package bdi4jade.reasoning;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;

/**
 * This class is the default implementation of the strategy
 * {@link AgentPlanSelectionStrategy}.
 * 
 * @author Ingrid Nunes
 */
public class DefaultAgentPlanSelectionStrategy extends
		AbstractAgentReasoningStrategy implements AgentPlanSelectionStrategy {

	/**
	 * This default implementation requests each of its capabilities to select
	 * one of its plans, and this method selects one of them, randomly.
	 * 
	 * @see AgentPlanSelectionStrategy#selectPlan(Goal, java.util.Map)
	 */
	@Override
	public Plan selectPlan(Goal goal, Map<Capability, Set<Plan>> capabilityPlans) {
		Set<Plan> preselectedPlans = new HashSet<>();
		for (Capability capability : capabilityPlans.keySet()) {
			Plan preselectedPlan = capability.getPlanSelectionStrategy()
					.selectPlan(goal, capabilityPlans.get(capability));
			if (preselectedPlan != null) {
				preselectedPlans.add(preselectedPlan);
			}
		}

		if (preselectedPlans.isEmpty()) {
			return null;
		} else {
			return preselectedPlans.iterator().next();
		}
	}

}
