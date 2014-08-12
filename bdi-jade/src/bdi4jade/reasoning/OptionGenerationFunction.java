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

import bdi4jade.core.GoalUpdateSet;

/**
 * This interface defines the option generation functions to be used in the
 * BDI-interpreter, within the scope of a capability. This strategy is used for
 * creating new goals or to drop existing ones.
 * 
 * @author Ingrid Nunes
 */
public interface OptionGenerationFunction extends ReasoningStrategy {

	/**
	 * This method is responsible for analyzing goals dispatched by a capability
	 * that have not been achieved yet, and choosing those to be dropped. It may
	 * also generate new goals to be achieved.
	 * 
	 * The parameter of this method is a {@link GoalUpdateSet}, which contains
	 * three sets: (i) the set of current goals dispatched by the capability
	 * associated with this strategy and their status; (ii) the set of generated
	 * goals; and (ii) the set of dropped goals. The last two sets are outputs
	 * of this methods.
	 * 
	 * @param goalUpdateSet
	 *            a three-set object containing current goals with their status,
	 *            and dropped and generated goals.
	 */
	public void generateGoals(GoalUpdateSet goalUpdateSet);

}
