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

package bdi4jade.goal;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * This class represents a goal that aims at achieving all goals that compose
 * this goal in a parallel way.
 * 
 * @author Ingrid Nunes
 */
public class ParallelGoal extends CompositeGoal {

	private static final long serialVersionUID = -8594724445200990207L;

	/**
	 * Creates a new ParallelGoal.
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 * 
	 * @see CompositeGoal#CompositeGoal(Goal[])
	 */
	public ParallelGoal(Goal[] goals) {
		super(goals);
	}

	/**
	 * Creates a new ParallelGoal.
	 * 
	 * @param goals
	 *            the goals that compose this goal.
	 * 
	 * @see CompositeGoal#CompositeGoal(Collection)
	 */
	public ParallelGoal(Set<Goal> goals) {
		super(goals);
	}

	/**
	 * @see CompositeGoal#createGoals(int)
	 */
	@Override
	protected Collection<Goal> createGoals(int size) {
		return new HashSet<Goal>(size);
	}

}
