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

/**
 * This class represents the goal of an agent to believe in a certain belief
 * with an specific boolean value, that is, the agent has a belief whose name
 * and value are specified in this goal.
 * 
 * @param <K>
 *            the type of the belief name.
 * 
 * @author Ingrid Nunes
 */
public class PredicateGoal<K> extends BeliefValueGoal<K, Boolean> {

	private static final long serialVersionUID = -6711494133447825608L;

	/**
	 * Default constructor.
	 */
	public PredicateGoal() {

	}

	/**
	 * Creates a new PropositionalBeliefValueGoal with the provided belief name
	 * and a boolean value. This value represents the value that is aimed to be
	 * associated with the belief.
	 * 
	 * @param beliefName
	 *            the belief name.
	 * @param value
	 *            the boolean value that is target of this goal.
	 */
	public PredicateGoal(K beliefName, Boolean value) {
		super(beliefName, value);
	}

}
