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

package br.ufrgs.inf.bdi4jade.event;

import jade.content.AgentAction;
import br.ufrgs.inf.bdi4jade.goal.Goal;

/**
 * This class represents an event performed over a goal.
 * 
 * @author ingrid
 */
public class GoalEvent implements AgentAction {

	private static final long serialVersionUID = 8315524257754153164L;

	protected Goal goal;

	/**
	 * Default constructor.
	 */
	public GoalEvent() {

	}

	/**
	 * Creates a new goal event with a goal.
	 * 
	 * @param goal
	 *            the goal of this event.
	 */
	public GoalEvent(Goal goal) {
		this.goal = goal;
	}

	/**
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * @param goal
	 *            the goal to set
	 */
	public void setGoal(Goal goal) {
		this.goal = goal;
	}

}
