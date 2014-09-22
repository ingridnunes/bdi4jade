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
 * This class represents the goal of an agent to believe in a certain belief,
 * that is, the agent has a belief whose name is specified in this goal.
 * 
 * @author Ingrid Nunes
 */
public interface BeliefGoal<K> extends Goal {

	/**
	 * Returns the name of the belief associated with this goal.
	 * 
	 * @return the belief name.
	 */
	public K getBeliefName();

	/**
	 * Returns the belief which is the output of this goal achievement.
	 * 
	 * @return the belief.
	 */
	public Belief<K, ?> getOutputBelief();

	/**
	 * Checks whether this goal is achieved by checking the provided belief
	 * base.
	 * 
	 * @param beliefBase
	 *            the belief base to be checked.
	 * @return true if goal was achieved, false otherwise.
	 */
	public boolean isAchieved(BeliefBase beliefBase);

	/**
	 * Sets the belief which is the output of this goal achievement.
	 * 
	 * @param belief the belief.
	 */
	public void setOutputBelief(Belief<K, ?> belief);

}
