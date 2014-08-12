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

import java.util.Set;

import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;

/**
 * This class is the default implementation of the strategy
 * {@link PlanSelectionStrategy}. It selects the first plan of the set.
 * 
 * @author Ingrid Nunes
 */
public class DefaultPlanSelectionStrategy extends AbstractReasoningStrategy
		implements PlanSelectionStrategy {

	/**
	 * Selects the first plan of the set of candidate plans, if any.
	 * 
	 * @see bdi4jade.reasoning.PlanSelectionStrategy#selectPlan(bdi4jade.goal.Goal,
	 *      java.util.Set)
	 */
	@Override
	public Plan selectPlan(Goal goal, Set<Plan> candidatePlans) {
		if (candidatePlans.isEmpty())
			return null;
		else
			return candidatePlans.iterator().next();
	}

}
