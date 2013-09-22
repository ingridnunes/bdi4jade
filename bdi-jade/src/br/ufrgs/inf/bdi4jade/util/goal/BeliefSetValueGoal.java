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

package br.ufrgs.inf.bdi4jade.util.goal;

import br.ufrgs.inf.bdi4jade.belief.BeliefSet;
import br.ufrgs.inf.bdi4jade.core.BeliefBase;
import br.ufrgs.inf.bdi4jade.goal.Goal;

/**
 * This class represents the goal of an agent believe in a belief that contains
 * a certain value, i.e. the agent has a belief set whose name is specified in
 * this goal and it contains the specified value..
 * 
 * @author ingrid
 */
public class BeliefSetValueGoal<T> implements Goal {

	private static final long serialVersionUID = 2493877854717226283L;

	private String beliefSetName;
	private T value;

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
	public BeliefSetValueGoal(String beliefSetName, T value) {
		this.beliefSetName = beliefSetName;
		this.value = value;
	}

	/**
	 * @return the beliefSetName
	 */
	public String getBeliefSetName() {
		return beliefSetName;
	}

	/**
	 * @return the value
	 */
	public T getValue() {
		return value;
	}

	/**
	 * Checks if this goal is achieved by verifying if the provided belief set
	 * contains the Value of this goal.
	 * 
	 * @param beliefBase
	 *            the belief base to be checked.
	 * @return true if the belief sey contains the value of this goal.
	 */
	@SuppressWarnings("unchecked")
	public boolean isAchieved(BeliefBase beliefBase) {
		BeliefSet<T> beliefSet = (BeliefSet<T>) beliefBase
				.getBelief(beliefSetName);
		if (beliefSet == null) {
			return false;
		} else {
			return beliefSet.hasValue(value);
		}
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Belief '" + beliefSetName + "' has value " + value;
	}

}
