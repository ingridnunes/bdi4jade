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

import bdi4jade.core.Capability;
import bdi4jade.core.GoalUpdateSet;

/**
 * This interface defines the option generation functions to be used in the
 * BDI-interpreter, by a BDI agent. This strategy is used for creating new goals
 * or to drop existing ones.
 * 
 * @author Ingrid Nunes
 */
public interface AgentOptionGenerationFunction extends AgentReasoningStrategy {

	/**
	 * This method is responsible for generating new goals or dropping existing
	 * ones.
	 * 
	 * @param agentGoalUpdateSet
	 *            the {@link GoalUpdateSet} that contains the set of agent
	 *            current goals. It has also a set of dropped goals and
	 *            generated goals, which are used as outputs of this method.
	 * @param capabilityGoalUpdateSets
	 *            the map from capabilities to their goal update set.
	 */
	public void generateGoals(GoalUpdateSet agentGoalUpdateSet,
			Map<Capability, GoalUpdateSet> capabilityGoalUpdateSets);

}
