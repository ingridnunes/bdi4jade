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

import bdi4jade.core.Intention;

/**
 * This enumeration provides the possible status that a goal can have. This
 * status is determined by {@link Intention}.
 * 
 * @author Ingrid Nunes
 */
public enum GoalStatus {

	ACHIEVED(true), NO_LONGER_DESIRED(true), PLAN_FAILED, TRYING_TO_ACHIEVE, UNACHIEVABLE(
			true), WAITING;

	private boolean isFinished;

	private GoalStatus() {
		this(false);
	}

	private GoalStatus(boolean isFinished) {
		this.isFinished = isFinished;
	}

	/**
	 * Indicates whether this status corresponds to a status in which the goal
	 * has finished, that is, the agent does not have the goal anymore.
	 * 
	 * @return true if the status is a status of finished goal, false otherwise.
	 */
	public boolean isFinished() {
		return isFinished;
	}

}
