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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.plan;

import jade.core.behaviours.Behaviour;
import bdi4jade.plan.PlanInstance.EndState;

/**
 * This interface defines a PlanBody. Plans are executed as behaviors (
 * {@link Behaviour}), but executed in the BDI context, these behaviors should
 * also implement this interface.
 * 
 * @author ingrid
 */
public interface PlanBody {

	/**
	 * Returns the end state of the execution of this plan.
	 * 
	 * @return the end state of this plan, or null if it has not finished yet.
	 */
	public EndState getEndState();

	/**
	 * Initializes the PlanBody. It is invoked just after its instantiation.
	 * 
	 * @param planInstance
	 *            the plan instance that contains contextual information for
	 *            this plan body.
	 */
	public void init(PlanInstance planInstance);

}
