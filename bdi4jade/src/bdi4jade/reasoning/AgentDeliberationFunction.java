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
import bdi4jade.core.GoalUpdateSet.GoalDescription;
import bdi4jade.goal.Goal;

/**
 * This interface defines the deliberation function to be used by a BDI agent.
 * This strategy is used for selecting a set of goals that must be tried
 * (intentions) from the set of goals.
 * 
 * @author Ingrid Nunes
 */
public interface AgentDeliberationFunction extends AgentReasoningStrategy {

	/**
	 * This method is responsible for selecting a set of goals that must be
	 * tried to be achieved (intentions) from the set of goals.
	 * 
	 * @param agentGoals
	 *            the set of agent goals, which are goals not dispatched within
	 *            the scope of a capability.
	 * @param capabilityGoals
	 *            the map from capabilities to their set of goals.
	 * 
	 * @return the list of selected goals, which will become intentions.
	 */
	public Set<Goal> filter(Set<GoalDescription> agentGoals,
			Map<Capability, Set<GoalDescription>> capabilityGoals);

}
