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

import bdi4jade.belief.Belief;
import bdi4jade.belief.BeliefBase;

/**
 * This class represents the goal of an agent to believe in a certain belief
 * with a not null value, that is, the agent has a belief whose name is
 * specified in this goal and value can be any but null.
 * 
 * @param <K>
 *            the type of the belief name.
 * @param <V>
 *            the type of the belief value.
 * 
 * @author Ingrid Nunes
 */
public class BeliefNotNullValueGoal<K, V> extends AbstractBeliefGoal<K> {

	private static final long serialVersionUID = 2493877854717226283L;

	/**
	 * Default constructor.
	 */
	public BeliefNotNullValueGoal() {

	}

	/**
	 * Creates a new BeliefNotNullValueGoal with the provided belief.
	 * 
	 * @param beliefName
	 *            the belief name.
	 */
	public BeliefNotNullValueGoal(K beliefName) {
		super(beliefName);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BeliefNotNullValueGoal) {
			BeliefNotNullValueGoal<?, ?> bg = (BeliefNotNullValueGoal<?, ?>) obj;
			return beliefName.equals(bg.beliefName);
		}
		return false;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = BeliefNotNullValueGoal.class.hashCode();
		result = prime * result
				+ ((beliefName == null) ? 0 : beliefName.hashCode());
		return result;
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
	public boolean isAchieved(BeliefBase beliefBase) {
		Belief<?, ?> belief = beliefBase.getBelief(beliefName);
		if (belief == null) {
			return false;
		} else {
			return belief.getValue() != null;
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
				.append(getBeliefName()).append(" != null").toString();
	}

}