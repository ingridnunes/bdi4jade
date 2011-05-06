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

package br.pucrio.inf.les.bdi4jade.exception;

import br.pucrio.inf.les.bdi4jade.goal.Goal;

/**
 * @author ingridnunes
 * 
 */
public class GoalFailedException extends Exception {

	private static final long serialVersionUID = -6082968354395705561L;

	private Goal goal;

	/**
	 * Creates a new instance of GoalFailedException.
	 * 
	 * @param goal
	 *            the goal that failed.
	 */
	public GoalFailedException(Goal goal) {
		this.goal = goal;
	}

	/**
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return "Goal failed exception: " + goal;
	}

}