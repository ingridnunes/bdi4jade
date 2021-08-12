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

import bdi4jade.core.GoalUpdateSet.GoalDescription;
import bdi4jade.goal.Goal;

/**
 * This interface defines the deliberation function to be used within the scope
 * of a capability. This strategy is used for selecting a set of goals that must
 * be tried (intentions) from the set of goals.
 * 
 * @author Ingrid Nunes
 */
public interface DeliberationFunction extends ReasoningStrategy {

	/**
	 * Selects the goals that must be tried to achieve and the ones. Goals that
	 * are not selected will have its status set to waiting;
	 * 
	 * @param goals
	 *            the list of current goals dispatched by the capability
	 *            associated with this strategy.
	 * 
	 * @return the list of selected goals, which will become intentions.
	 */
	public Set<Goal> filter(Set<GoalDescription> goals);

}
