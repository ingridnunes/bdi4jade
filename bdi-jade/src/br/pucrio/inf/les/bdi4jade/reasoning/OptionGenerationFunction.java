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

import java.util.Map;
import java.util.Set;

import br.pucrio.inf.les.bdi4jade.goal.Goal;
import br.pucrio.inf.les.bdi4jade.goal.GoalStatus;

/**
 * This interface defines the option generation functions to be used in the
 * BDI-interpreter. This strategy is used for creating new goals or to drop
 * existing ones.
 * 
 * @author ingrid
 */
public interface OptionGenerationFunction {

	/**
	 * The goals parameter is a map of all goals of the agent (that might be
	 * intentions) with their corresponding status. A set is returned of this
	 * function indicating the creating of new goals and the ones that continue
	 * to be goals. The non-selected goals will be no longer desired.
	 * 
	 * @param goals
	 *            the current goals with their status.
	 * @return the list of selected goals.
	 */
	public Set<Goal> generateGoals(Map<Goal, GoalStatus> goals);

}
