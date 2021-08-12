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

package bdi4jade.plan;

import jade.lang.acl.ACLMessage;
import bdi4jade.core.MetadataElement;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.plan.planbody.PlanBody;

/**
 * This interfaces represents the plan abstraction. It defines the goals that
 * the plan can achieve, in which context, and it able to create an instance of
 * plan body to be executed.
 * 
 * @author Ingrid Nunes
 */
public interface Plan extends MetadataElement {

	/**
	 * This enumeration represents the possible end states of a plan execution.
	 * 
	 * @author Ingrid Nunes
	 */
	public enum EndState {
		FAILED, SUCCESSFUL;
	}

	/**
	 * Verifies if a given goal can be achieved by this plan.
	 * 
	 * @param goal
	 *            the goal to be verified.
	 * @return true if the given goal can be achieved by this plan, false
	 *         otherwise.
	 */
	public boolean canAchieve(Goal goal);

	/**
	 * Verifies if the message can be processed by this plan.
	 * 
	 * @param message
	 *            the message to be checked.
	 * @return true if this plan can process the message, false otherwise.
	 */
	public boolean canProcess(ACLMessage message);

	/**
	 * Instantiate the plan body of this plan, which is an implementation of the
	 * {@link PlanBody} interface.
	 * 
	 * @return the instantiated plan body.
	 * @throws PlanInstantiationException
	 *             if an error occurred during the instantiation process.
	 */
	public abstract PlanBody createPlanBody() throws PlanInstantiationException;

	/**
	 * Returns the id of this plan.
	 * 
	 * @return the id
	 */
	public String getId();

	/**
	 * Returns the plan library with which this plan is associated.
	 * 
	 * @return the planLibrary.
	 */
	public PlanLibrary getPlanLibrary();

	/**
	 * Verifies if the current context is valid for this plan execution.
	 * 
	 * @param goal
	 *            the goal to be achieved whose conditions may be tested to
	 *            verify the applicability of this plan.
	 * 
	 * @return true if the plan can be executed in the current context, false
	 *         otherwise.
	 */
	public boolean isContextApplicable(Goal goal);

	/**
	 * Sets the plan library with which this plan is associated.
	 * 
	 * @param planLibrary
	 *            the planLibrary to set.
	 */
	public void setPlanLibrary(PlanLibrary planLibrary);

}
