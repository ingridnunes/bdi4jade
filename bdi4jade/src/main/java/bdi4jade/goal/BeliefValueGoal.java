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
 * @param <K>
 *            the type of the belief name.
 * @param <V>
 *            the type of the belief value.
 * 
 * @author Ingrid Nunes
 */
public class BeliefValueGoal<K, V> extends AbstractBeliefGoal<K> {

	private static final long serialVersionUID = 2493877854717226283L;

	private V value;

	/**
	 * Default constructor.
	 */
	public BeliefValueGoal() {

	}

	/**
	 * Creates a new BeliefValueGoal with the provided belief. The value
	 * associated with this goal is initialized with null.
	 * 
	 * @param beliefName
	 *            the belief name.
	 */
	public BeliefValueGoal(K beliefName) {
		this(beliefName, null);
	}

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
	public BeliefValueGoal(K beliefName, V value) {
		super(beliefName);
		this.value = value;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BeliefValueGoal) {
			BeliefValueGoal<?, ?> bg = (BeliefValueGoal<?, ?>) obj;
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
		int result = BeliefValueGoal.class.hashCode();
		result = prime * result
				+ ((beliefName == null) ? 0 : beliefName.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
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
			if (value == null) {
				return belief.getValue() == null;
			} else if (belief.getValue() == null) {
				return false;
			} else {
				return belief.getValue().equals(value);
			}
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
	 * "BeliefValueGoal: BELIEF NAME should be BELIEF VALUE".
	 * 
	 * @return the string representation of this belief value goal.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer(getClass().getName()).append(": ")
				.append(getBeliefName()).append("=").append(value).toString();
	}

}