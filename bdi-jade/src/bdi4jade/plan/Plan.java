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

import jade.lang.acl.ACLMessage;
import bdi4jade.core.MetadataElement;
import bdi4jade.core.PlanLibrary;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.message.MessageGoal;

/**
 * This interfaces represents the plan abstraction. It defines the goals that
 * the plan can achieve, in which context, and knows which is its plan body.
 * 
 * @author ingrid
 */
public interface Plan extends MetadataElement {

	/**
	 * This enumuration represents the possible end states of a plan execution.
	 * 
	 * @author ingrid
	 */
	public enum EndState {
		FAILED, SUCCESSFUL;
	}

	/**
	 * Verifies if a given goal can be achieved by this plan. When the goal is a
	 * {@link MessageGoal}, it invokes the method
	 * {@link Plan#canProcess(ACLMessage)}. Otherwise, it checks if the class of
	 * this goal is contained in the goal set of this plan.
	 * 
	 * @param goal
	 *            the goal to be verified.
	 * @return true if the given goal can be achieved by this plan, false
	 *         otherwise.
	 */
	public boolean canAchieve(Goal goal);

	/**
	 * Verifies if the message received matches with any of the message
	 * templates of this plan.
	 * 
	 * @param message
	 *            the message to be checked.
	 * @return true if this plan can process the message.
	 */
	public boolean canProcess(ACLMessage message);

	/**
	 * Instantiate the plan body of this plan. It must implement the
	 * {@link PlanBodyInterface} interface.
	 * 
	 * @return the instantiated plan body.
	 * @throws PlanInstantiationException
	 *             if an error occurred during the instantiation process.
	 */
	public abstract PlanBody createPlanBody() throws PlanInstantiationException;

	/**
	 * @return the id
	 */
	public String getId();

	/**
	 * @return the planLibrary
	 */
	public PlanLibrary getPlanLibrary();

	/**
	 * @param planLibrary
	 *            the planLibrary to set
	 */
	public void setPlanLibrary(PlanLibrary planLibrary);

}
