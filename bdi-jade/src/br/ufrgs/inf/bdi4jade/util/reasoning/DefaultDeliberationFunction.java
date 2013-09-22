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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package br.ufrgs.inf.bdi4jade.util.reasoning;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import br.ufrgs.inf.bdi4jade.goal.Goal;
import br.ufrgs.inf.bdi4jade.goal.GoalStatus;
import br.ufrgs.inf.bdi4jade.reasoning.DeliberationFunction;

/**
 * The default implementation of the {@link DeliberationFunction}. It selects
 * all intentions to be tried.
 * 
 * @author ingrid
 */
public class DefaultDeliberationFunction implements DeliberationFunction {

	/**
	 * @see br.ufrgs.inf.bdi4jade.reasoning.DeliberationFunction#filter(java.util.Map)
	 */
	@Override
	public Set<Goal> filter(Map<Goal, GoalStatus> goals) {
		return new HashSet<Goal>(goals.keySet());
	}

}
