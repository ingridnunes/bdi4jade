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
// http://www.inf.puc-rio.br/~ionunes/
//
//----------------------------------------------------------------------------

package br.pucrio.inf.les.bdi4jade.event;

import br.pucrio.inf.les.bdi4jade.goal.Goal;
import br.pucrio.inf.les.bdi4jade.goal.GoalStatus;

/**
 * @author ingrid
 * 
 */
public class GoalFinishedEvent extends GoalEvent {

	private static final long serialVersionUID = -4790145097443747163L;

	protected GoalStatus status;

	/**
	 * Default constructor.
	 */
	public GoalFinishedEvent() {

	}

	/**
	 * Creates a new goal event with a goal and its status.
	 * 
	 * @param goal
	 *            the goal of this event.
	 * @param status
	 *            the status of the goal.
	 */
	public GoalFinishedEvent(Goal goal, GoalStatus status) {
		super(goal);
		this.status = status;
	}

	/**
	 * @return the status
	 */
	public GoalStatus getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(GoalStatus status) {
		this.status = status;
	}

}
