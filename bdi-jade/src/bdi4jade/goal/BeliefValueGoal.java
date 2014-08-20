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

import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.belief.Belief;
import bdi4jade.belief.BeliefBase;

/**
 * This class represents the goal of an agent to believe in a certain belief
 * with an specific value, that is, the agent has a belief whose name and value
 * are specified in this goal.
 * 
 * @param <T>
 *            the type of the belief value.
 * 
 * @author Ingrid Nunes
 */
public class BeliefValueGoal<T> extends BeliefGoal {

	private static final long serialVersionUID = 2493877854717226283L;

	private T value;

	/**
	 * Creates a new BeliefValueGoal with the provided belief name and a value.
	 * This value represents the value that is aimed to be associated with the
	 * belief.
	 * 
	 * @param beliefName
	 *            the belief name.
	 * @param value
	 *            the value that is target of this goal.
	 */
	public BeliefValueGoal(String beliefName, T value) {
		super(beliefName);
		this.value = value;
	}

	/**
	 * The belief value associated with this goal.
	 * 
	 * @return the belief value.
	 */
	@Parameter(direction = Direction.IN)
	public T getValue() {
		return value;
	}

	/**
	 * Checks whether this goal is achieved by verifying if the provided belief
	 * has the value specified in this goal.
	 * 
	 * @param beliefBase
	 *            the belief base to be checked.
	 * @return true if the belief has the value specified in this goal, false
	 *         otherwise.
	 */
	@Override
	public boolean isAchieved(BeliefBase beliefBase) {
		Belief<?> belief = (Belief<?>) beliefBase.getBelief(getBeliefName());
		if (belief == null) {
			return false;
		} else {
			return belief.getValue().equals(value);
		}
	}

	/**
	 * Returns a string representation of this goal, in the form
	 * "BeliefValueGoal: BELIEF NAME should be BELIEF VALUE".
	 * 
	 * @return the string representation of this belief value goal.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer(getClass().getName()).append(": ")
				.append(getBeliefName()).append(" should be ").append(value)
				.toString();
	}

}