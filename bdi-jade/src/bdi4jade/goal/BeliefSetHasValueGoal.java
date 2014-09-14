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
import bdi4jade.belief.BeliefBase;
import bdi4jade.belief.BeliefSet;

/**
 * This class represents the goal of an agent believe in a belief that contains
 * a certain value, that is, the agent has a belief set whose name is specified
 * in this goal and it contains the specified value.
 * 
 * @param <K>
 *            the type of the belief name.
 * @param <V>
 *            the type of the values in the belief set.
 * 
 * @author Ingrid Nunes
 */
public class BeliefSetHasValueGoal<K, V> extends AbstractBeliefGoal<K> {

	private static final long serialVersionUID = 2493877854717226283L;

	private V value;

	/**
	 * Default constructor.
	 */
	public BeliefSetHasValueGoal() {

	}

	/**
	 * Creates a new BeliefSetValueGoal with the provided belief name and a
	 * value. This value represents the one that should be part of the belief
	 * set.
	 * 
	 * @param beliefSetName
	 *            the belief name.
	 * @param value
	 *            the value that is target of this goal.
	 */
	public BeliefSetHasValueGoal(K beliefSetName, V value) {
		super(beliefSetName);
		this.value = value;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BeliefSetHasValueGoal) {
			BeliefSetHasValueGoal<?, ?> bg = (BeliefSetHasValueGoal<?, ?>) obj;
			if (!beliefName.equals(bg.beliefName))
				return false;
			if (value == null) {
				return bg.value == null;
			} else if (bg.value == null) {
				return false;
			} else {
				return value.equals(bg.value);
			}
		}
		return false;
	}

	/**
	 * The belief value associated with this goal.
	 * 
	 * @return the belief value.
	 */
	@Parameter(direction = Direction.IN)
	public V getValue() {
		return value;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = BeliefSetHasValueGoal.class.hashCode();
		result = prime * result
				+ ((beliefName == null) ? 0 : beliefName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/**
	 * Checks whether this goal is achieved by verifying if the provided belief
	 * set contains the value specified in this goal.
	 * 
	 * @param beliefBase
	 *            the belief base to be checked.
	 * @return true if the belief set contains the value specified in this goal.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public boolean isAchieved(BeliefBase beliefBase) {
		BeliefSet<K, V> beliefSet = (BeliefSet<K, V>) beliefBase
				.getBelief(getBeliefName());
		if (beliefSet == null) {
			return false;
		} else {
			return beliefSet.hasValue(value);
		}
	}

	/**
	 * Sets the value of this goal.
	 * 
	 * @param value
	 *            the value.
	 */
	public void setValue(V value) {
		this.value = value;
	}

	/**
	 * Returns a string representation of this goal, in the form
	 * "BeliefSetValueGoal: BELIEF NAME should have BELIEF VALUE".
	 * 
	 * @return the string representation of this belief value goal.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer(getClass().getName()).append(": ")
				.append(value).append(" in ").append(beliefName).toString();
	}

}
