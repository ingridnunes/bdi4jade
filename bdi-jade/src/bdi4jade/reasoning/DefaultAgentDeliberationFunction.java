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
import bdi4jade.core.GoalUpdateSet.GoalDescription;
import bdi4jade.goal.Goal;

/**
 * This class is the default implementation of the strategy
 * {@link AgentDeliberationFunction}.
 * 
 * @author Ingrid Nunes
 */
public class DefaultAgentDeliberationFunction extends
		AbstractAgentReasoningStrategy implements AgentDeliberationFunction {

	/**
	 * This default implementation selects all agent goals (those not dispatched
	 * within the scope of a capability) to be achieved, and requests each of
	 * its capabilities to filter their goals.
	 * 
	 * @see AgentDeliberationFunction#filter(Set, Map)
	 */
	@Override
	public Set<Goal> filter(Set<GoalDescription> agentGoals,
			Map<Capability, Set<GoalDescription>> capabilityGoals) {
		Set<Goal> selectedGoals = new HashSet<>();
		for (GoalDescription goalDescription : agentGoals) {
			selectedGoals.add(goalDescription.getGoal());
		}
		for (Capability capability : capabilityGoals.keySet()) {
			selectedGoals.addAll(capability.getDeliberationFunction().filter(
					capabilityGoals.get(capability)));
		}
		return selectedGoals;
	}

}
