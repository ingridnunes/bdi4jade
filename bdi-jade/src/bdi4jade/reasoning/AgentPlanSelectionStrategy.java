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

import java.util.Map;
import java.util.Set;

import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;

/**
 * This interface defines the plan selection strategy to be used by a BDI agent.
 * This strategy is used for selecting a plan from a set of possible candidate
 * plans of a capability.
 * 
 * @author Ingrid Nunes
 */
public interface AgentPlanSelectionStrategy extends AgentReasoningStrategy {

	/**
	 * This method is responsible for selecting plans to achieve a goals of this
	 * agent.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 * @param capabilityPlans
	 *            the set of candidate plans of each capability, as a map.
	 * 
	 * @return the selected plan.
	 */
	public Plan selectPlan(Goal goal, Map<Capability, Set<Plan>> capabilityPlans);

}
