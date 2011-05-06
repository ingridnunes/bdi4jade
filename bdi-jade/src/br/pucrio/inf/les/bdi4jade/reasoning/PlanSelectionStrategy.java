//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes, et al.
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
// http://www.inf.puc-rio.br/~ionunes/
//
//----------------------------------------------------------------------------

package br.pucrio.inf.les.bdi4jade.reasoning;

import java.util.Set;

import br.pucrio.inf.les.bdi4jade.goal.Goal;
import br.pucrio.inf.les.bdi4jade.plan.Plan;

/**
 * This interface defines the plan selection strategy to be used in a
 * capability. This strategy is used for selecting a plan from a set of possible
 * options.
 * 
 * @author ingrid
 */
public interface PlanSelectionStrategy {

	/**
	 * Selects a plan to be executed to achieve the given goal.
	 * 
	 * @param goal
	 *            the goal that must be achieved.
	 * @param plans
	 *            the plans that can achieve the goal.
	 * @return the selected plan.
	 */
	public Plan selectPlan(Goal goal, Set<Plan> plans);

}
