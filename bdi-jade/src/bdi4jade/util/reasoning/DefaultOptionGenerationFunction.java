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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.util.reasoning;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.reasoning.OptionGenerationFunction;

/**
 * The default implementation of the {@link OptionGenerationFunction}. It
 * selects all goals, therefore none is dropped or created.
 * 
 * @author ingrid
 */
public class DefaultOptionGenerationFunction implements
		OptionGenerationFunction {

	/**
	 * @see bdi4jade.reasoning.OptionGenerationFunction#generateGoals(java.util.Map)
	 */
	@Override
	public Set<Goal> generateGoals(Map<Goal, GoalStatus> goals) {
		return new HashSet<Goal>(goals.keySet());
	}

}
