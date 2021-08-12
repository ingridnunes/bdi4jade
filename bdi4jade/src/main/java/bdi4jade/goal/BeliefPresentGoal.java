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

import bdi4jade.belief.BeliefBase;

/**
 * This class represents the goal of an agent to believe in a certain belief,
 * that is, the agent has a belief whose name is specified in this goal.
 * 
 * @author Ingrid Nunes
 */
public class BeliefPresentGoal<K> extends AbstractBeliefGoal<K> {

	private static final long serialVersionUID = 2493877854717226283L;

	/**
	 * Default constructor.
	 */
	public BeliefPresentGoal() {

	}

	/**
	 * Creates a new BeliefPresentGoal with the provided belief name.
	 * 
	 * @param beliefName
	 *            the belief name.
	 */
	public BeliefPresentGoal(K beliefName) {
		super(beliefName);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BeliefPresentGoal) {
			BeliefPresentGoal<?> bg = (BeliefPresentGoal<?>) obj;
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
		int result = BeliefPresentGoal.class.hashCode();
		result = prime * result
				+ ((beliefName == null) ? 0 : beliefName.hashCode());
		return result;
	}

	/**
	 * Checks whether this goal is achieved by verifying if the provided belief
	 * base contains the belief of this goal.
	 * 
	 * @param beliefBase
	 *            the belief base to be checked.
	 * @return true if the belief base contains the belief of this goal.
	 */
	public boolean isAchieved(BeliefBase beliefBase) {
		return beliefBase.hasBelief(beliefName);
	}

	/**
	 * Returns a string representation of this goal, in the form
	 * "BeliefGoal: belief name".
	 * 
	 * @return the string representation of this belief goal.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer(getClass().getName()).append(": ")
				.append(beliefName).toString();
	}

}
