//----------------------------------------------------------------------------
// Copyright (C) 2013  Ingrid Nunes
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

package bdi4jade.extension.softgoal.core;

import java.util.HashSet;
import java.util.Set;

import bdi4jade.core.BDIAgent;
import bdi4jade.extension.softgoal.reasoning.UtilityBasedPlanSelectionStrategy;

/**
 * @author ingrid
 * 
 */
public class SoftGoalBDIAgent extends BDIAgent {

	private static final long serialVersionUID = -1721751203235905764L;

	private final Set<Softgoal> softgoals;

	public SoftGoalBDIAgent() {

		this.softgoals = new HashSet<Softgoal>();
		setPlanSelectionStrategy(new UtilityBasedPlanSelectionStrategy(this));
		getRootCapability().getBeliefBase()
				.addBelief(new SoftgoalPreferences());
	}

	/**
	 * Adds a new softgoal to this agent.
	 * 
	 * @param softgoal
	 *            the softgoal to be pursued.
	 */
	public void addSoftgoal(Softgoal softgoal) {
		this.softgoals.add(softgoal);
	}

	/**
	 * Drops a given softgoal of this agent. If the softgoal is not part of the
	 * agent's current softgoals, no action is performed.
	 * 
	 * @param softgoal
	 *            the softgoal to be dropped.
	 */

	public void dropSoftoal(Softgoal softgoal) {
		this.softgoals.remove(softgoal);
	}

	/**
	 * Gets all softgoals of this agent.
	 * 
	 * @return the set of softgoals.
	 */
	public Set<Softgoal> getAllSoftgoals() {
		return this.softgoals;
	}

}
