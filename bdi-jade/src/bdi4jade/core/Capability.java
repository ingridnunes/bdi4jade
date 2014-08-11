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

package bdi4jade.core;

import jade.lang.acl.ACLMessage;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.belief.Belief;
import bdi4jade.belief.BeliefBase;
import bdi4jade.plan.Plan;
import bdi4jade.plan.PlanLibrary;

/**
 * This capability represents a component that aggregates the mental attitudes
 * defined by the BDI architecture. It has a belief base with the associated
 * beliefs (knowledge) and a plan library.
 * 
 * @author ingrid
 */
public class Capability implements Serializable {

	private static final long serialVersionUID = -4922359927943108421L;

	protected final BeliefBase beliefBase;
	protected final String id;
	protected final Log log;
	protected BDIAgent myAgent;
	protected final Set<Capability> partCapabilities;
	protected final PlanLibrary planLibrary;
	private boolean start;
	protected Capability wholeCapability;

	/**
	 * Creates a new capability. It uses {@link BeliefBase} and
	 * {@link PlanLibrary} as belief base and plan library respectively.
	 */
	public Capability() {
		this(null);
	}

	/**
	 * Creates a new capability.
	 * 
	 * @param initialBeliefs
	 *            the initial set of beliefs to be added to the belief base of
	 *            this capability.
	 * @param initialPlans
	 *            the initial set of plans to be added to the plan library of
	 *            this capability.
	 */
	public Capability(Set<Belief<?>> initialBeliefs, Set<Plan> initialPlans) {
		this(null, initialBeliefs, initialPlans);
	}

	/**
	 * Creates a new capability. It uses {@link BeliefBase} and
	 * {@link PlanLibrary} as belief base and plan library respectively.
	 * 
	 * @param id
	 *            the capability id. If it is null, the class name is going to
	 *            be used.
	 */
	public Capability(String id) {
		this(id, null, null);
	}

	/**
	 * Creates a new capability. It uses {@link BeliefBase} and
	 * {@link PlanLibrary} as belief base and plan library respectively.
	 * 
	 * @param id
	 *            the capability id. If it is null, the class name is going to
	 *            be used.
	 * @param wholeCapability
	 *            the whole-capability that this capability is part of.
	 */
	public Capability(String id, Capability wholeCapability) {
		this(id, wholeCapability, null, null);
	}

	/**
	 * Creates a new capability.
	 * 
	 * @param id
	 *            the capability id. If it is null, the class name is going to
	 *            be used.
	 * @param wholeCapability
	 *            the whole-capability that this capability is part of.
	 * @param initialBeliefs
	 *            the initial set of beliefs to be added to the belief base of
	 *            this capability.
	 * @param initialPlans
	 *            the initial set of plans to be added to the plan library of
	 *            this capability.
	 */
	public Capability(String id, Capability wholeCapability,
			Set<Belief<?>> initialBeliefs, Set<Plan> initialPlans) {
		this.log = LogFactory.getLog(getClass());

		// Id initialization
		if (id == null) {
			if (this.getClass().getCanonicalName() == null
					|| Capability.class.equals(this.getClass())) {
				this.id = Capability.class.getSimpleName()
						+ System.currentTimeMillis();
			} else {
				this.id = this.getClass().getSimpleName();
			}
		} else {
			this.id = id;
		}

		// Setting up parent
		this.partCapabilities = new HashSet<>();
		if (wholeCapability != null) {
			wholeCapability.addPartCapability(this);
		}

		// Initializing belief base
		this.beliefBase = new BeliefBase(this, initialBeliefs);

		// Initializing plan library
		this.planLibrary = new PlanLibrary(this, initialPlans);

		this.start = false;
	}

	/**
	 * Creates a new capability.
	 * 
	 * @param id
	 *            the capability id. If it is null, the class name is going to
	 *            be used.
	 * @param initialBeliefs
	 *            the initial set of beliefs to be added to the belief base of
	 *            this capability.
	 * @param initialPlans
	 *            the initial set of plans to be added to the plan library of
	 *            this capability.
	 */
	public Capability(String id, Set<Belief<?>> initialBeliefs,
			Set<Plan> initialPlans) {
		this(id, null, initialBeliefs, initialPlans);
	}

	public void addPartCapability(Capability partCapability) {
		if (partCapability.wholeCapability != null) {
			partCapability.wholeCapability.removePartCapability(partCapability);
		}
		partCapability.wholeCapability = this;
		this.partCapabilities.add(partCapability);
	}

	/**
	 * Checks if this capability has a plan that can process the given message.
	 * 
	 * @param msg
	 *            the message to be checked.
	 * @return true if this capability has at least a plan that can process the
	 *         message.
	 */
	public boolean canProcess(ACLMessage msg) {
		return this.planLibrary.canHandle(msg);
	}

	/**
	 * @return the beliefBase
	 */
	public BeliefBase getBeliefBase() {
		return beliefBase;
	}

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the agent that this capability is associated with.
	 */
	public BDIAgent getMyAgent() {
		return this.myAgent;
	}

	/**
	 * @return the partCapabilities
	 */
	public Set<Capability> getPartCapabilities() {
		return partCapabilities;
	}

	/**
	 * @return the planLibrary
	 */
	public PlanLibrary getPlanLibrary() {
		return planLibrary;
	}

	/**
	 * @return the wholeCapability
	 */
	public Capability getWholeCapability() {
		return wholeCapability;
	}

	public boolean hasParts() {
		return !this.partCapabilities.isEmpty();
	}

	public boolean removePartCapability(Capability partCapability) {
		boolean removed = this.partCapabilities.remove(partCapability);
		if (removed) {
			partCapability.wholeCapability = null;
		}
		return removed;
	}

	/**
	 * This method is an empty place holder for subclasses. It may be invoked to
	 * review beliefs from this belief base.
	 */
	public void reviewBeliefs() {

	}

	/**
	 * @param myAgent
	 *            the myAgent to set
	 */
	public void setMyAgent(BDIAgent myAgent) {
		this.myAgent = myAgent;
		if (!start) {
			setup();
			this.start = true;
		}
	}

	/**
	 * This is an empty holder for being overridden by subclasses. Initializes
	 * the capability. This method is invoked by the constructor. It may be used
	 * to add initial plans and beliefs. The reasoning strategies of this
	 * capability are initialized in the constructor with default strategies.
	 * This method may also customize them.
	 */
	protected void setup() {

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id;
	}

}
