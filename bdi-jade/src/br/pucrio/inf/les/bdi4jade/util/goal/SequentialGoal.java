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

package br.pucrio.inf.les.bdi4jade.util.goal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.pucrio.inf.les.bdi4jade.goal.Goal;

/**
 * This class represents a goal that aims at achieving all goals that compose
 * this goal in a sequential way.
 * 
 * @author ingrid
 */
public class SequentialGoal extends CompositeGoal {

	private static final long serialVersionUID = -8594724445200990207L;

	/**
	 * Creates a new SequentialGoal.
	 * 
	 * @see CompositeGoal#CompositeGoal(Goal[])
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 */
	public SequentialGoal(Goal[] goals) {
		super(goals);
	}

	/**
	 * Creates a new SequentialGoal.
	 * 
	 * @see CompositeGoal#CompositeGoal(Collection)
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 */
	public SequentialGoal(List<Goal> goals) {
		super(goals);
	}

	/**
	 * @see br.pucrio.inf.les.bdi4jade.util.goal.CompositeGoal#createGoals()
	 */
	@Override
	protected Collection<Goal> createGoals(int size) {
		return new ArrayList<Goal>(size);
	}

}
