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

import br.ufrgs.inf.bdi4jade.core.BeliefBase;
import br.ufrgs.inf.bdi4jade.goal.Goal;

/**
 * This class represents the goal of an agent believe in a certain belief, i.e.
 * the agent has a belief whose name is specified in this goal.
 * 
 * @author ingrid
 */
public class BeliefGoal implements Goal {

	private static final long serialVersionUID = 2493877854717226283L;

	private String beliefName;

	/**
	 * Creates a new BeliefGoal. It considers that the belief name is string
	 * returned from the toString() method of the beliefValue.
	 * 
	 * @param beliefValue
	 *            the belief value whose toString() is the belief name.
	 */
	public BeliefGoal(Object beliefValue) {
		this.beliefName = beliefValue.toString();
	}

	/**
	 * Creates a new BeliefGoal with the provided belief name.
	 * 
	 * @param beliefName
	 *            the belief name.
	 */
	public BeliefGoal(String beliefName) {
		this.beliefName = beliefName;
	}

	/**
	 * @return the beliefName
	 */
	public String getBeliefName() {
		return beliefName;
	}

	/**
	 * Checks if this goal is achieved by verifying if the provided belief base
	 * contains the belief of this goal.
	 * 
	 * @param beliefBase
	 *            the belief base to be checked.
	 * @return true if the belief base contains the belief of this goal.
	 */
	public boolean isAchieved(BeliefBase beliefBase) {
		return beliefBase.hasBelief(beliefName);
	}

}
