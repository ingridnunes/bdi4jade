/*
 * Created on 13/12/2009 01:33:10 
 */
package br.pucrio.inf.les.bdijade.core;

import jade.lang.acl.ACLMessage;

/**
 * This capability represents a component that aggregates the mental attitudes
 * defined by the BDI architecture. It has a belief base with the associated
 * beliefs (knowledge) and a plan library. *
 * 
 * @author ingrid
 */
public class Capability {

	private static final long serialVersionUID = -4922359927943108421L;

	protected final BeliefBase beliefBase;
	protected final String id;
	protected BDIAgent myAgent;
	protected final PlanLibrary planLibrary;
	private boolean start;

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
	 * @param beliefBase
	 *            the belief base of this capability.
	 * @param planLibrary
	 *            the plan library of this capability.
	 */
	public Capability(BeliefBase beliefBase,
			PlanLibrary planLibrary) {
		this(null, beliefBase, planLibrary);
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
		this(id, new BeliefBase(), new PlanLibrary());
	}

	/**
	 * Creates a new capability.
	 * 
	 * @param id
	 *            the capability id. If it is null, the class name is going to
	 *            be used.
	 * @param beliefBase
	 *            the belief base of this capability.
	 * @param planLibrary
	 *            the plan library of this capability.
	 */
	public Capability(String id, BeliefBase beliefBase,
			PlanLibrary planLibrary) {
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
		beliefBase.setCapability(this);
		this.beliefBase = beliefBase;
		planLibrary.setCapability(this);
		this.planLibrary = planLibrary;
		this.start = false;
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
		return this.planLibrary.canProcessPlans(msg);
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
	 * @return the planLibrary
	 */
	public PlanLibrary getPlanLibrary() {
		return planLibrary;
	}

	/**
	 * @param myAgent the myAgent to set
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
