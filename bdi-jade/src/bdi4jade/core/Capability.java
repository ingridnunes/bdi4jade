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
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.belief.Belief;
import bdi4jade.belief.BeliefBase;
import bdi4jade.core.GoalUpdateSet.GoalDescription;
import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;
import bdi4jade.plan.PlanLibrary;
import bdi4jade.reasoning.BeliefRevisionStrategy;
import bdi4jade.reasoning.DefaultBeliefRevisionStrategy;
import bdi4jade.reasoning.DefaultDeliberationFunction;
import bdi4jade.reasoning.DefaultOptionGenerationFunction;
import bdi4jade.reasoning.DefaultPlanSelectionStrategy;
import bdi4jade.reasoning.DeliberationFunction;
import bdi4jade.reasoning.OptionGenerationFunction;
import bdi4jade.reasoning.PlanSelectionStrategy;

/**
 * This capability represents a component that aggregates the mental attitudes
 * defined by the BDI architecture. It has a belief base with the associated
 * beliefs (knowledge) and a plan library.
 * 
 * @author Ingrid Nunes
 */
public class Capability implements Serializable {

	private static final long serialVersionUID = -4922359927943108421L;

	private final Set<Capability> associationSources;
	private final Set<Capability> associationTargets;
	protected final BeliefBase beliefBase;
	private BeliefRevisionStrategy beliefRevisionStrategy;
	private DeliberationFunction deliberationFunction;
	protected final String id;
	private final Collection<Intention> intentions;
	protected final Log log;
	private BDIAgent myAgent;
	private OptionGenerationFunction optionGenerationFunction;
	private final Set<Capability> partCapabilities;
	protected final PlanLibrary planLibrary;
	private PlanSelectionStrategy planSelectionStrategy;
	private boolean started;

	private Capability wholeCapability;

	/**
	 * Creates a new capability with a generated id. It uses {@link BeliefBase}
	 * and {@link PlanLibrary} as belief base and plan library respectively.
	 */
	public Capability() {
		this(null, null, null, null, null);
	}

	/**
	 * Creates a new capability with a generated id. It uses {@link BeliefBase}
	 * and {@link PlanLibrary} as belief base and plan library respectively, and
	 * adds initial beliefs and plans.
	 * 
	 * @param initialBeliefs
	 *            the initial set of beliefs to be added to the belief base of
	 *            this capability.
	 * @param initialPlans
	 *            the initial set of plans to be added to the plan library of
	 *            this capability.
	 */
	public Capability(Set<Belief<?>> initialBeliefs, Set<Plan> initialPlans) {
		this(null, null, initialBeliefs, null, initialPlans);
	}

	/**
	 * Creates a new capability with the given id. It uses {@link BeliefBase}
	 * and {@link PlanLibrary} as belief base and plan library respectively.
	 * 
	 * @param id
	 *            the capability id. If it is null, the class name is going to
	 *            be used.
	 */
	public Capability(String id) {
		this(id, null, null, null, null);
	}

	/**
	 * Creates a new capability with the given id, or a generated one if it is
	 * null. It also sets the belief base and plan library, and adds initial
	 * beliefs and plans.
	 * 
	 * @param id
	 *            the capability id. If it is null, the class name is going to
	 *            be used.
	 * @param beliefBase
	 *            the belief base.
	 * @param initialBeliefs
	 *            the initial set of beliefs to be added to the belief base of
	 *            this capability.
	 * @param planLibrary
	 *            the plan library.
	 * @param initialPlans
	 *            the initial set of plans to be added to the plan library of
	 *            this capability.
	 */
	protected Capability(String id, BeliefBase beliefBase,
			Set<Belief<?>> initialBeliefs, PlanLibrary planLibrary,
			Set<Plan> initialPlans) {
		this.log = LogFactory.getLog(getClass());
		this.intentions = new LinkedList<>();

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

		// Initializing belief base and plan library
		this.beliefBase = beliefBase == null ? new BeliefBase(this,
				initialBeliefs) : beliefBase;
		this.planLibrary = planLibrary == null ? new PlanLibrary(this,
				initialPlans) : planLibrary;

		// Initializing associations
		this.wholeCapability = null;
		this.partCapabilities = new HashSet<>();
		this.associationSources = new HashSet<>();
		this.associationTargets = new HashSet<>();

		// Initializing reasoning strategies
		this.beliefRevisionStrategy = new DefaultBeliefRevisionStrategy();
		this.optionGenerationFunction = new DefaultOptionGenerationFunction();
		this.deliberationFunction = new DefaultDeliberationFunction();
		this.planSelectionStrategy = new DefaultPlanSelectionStrategy();

		synchronized (this) {
			this.started = false;
		}
	}

	/**
	 * Creates a new capability with the given id. It uses {@link BeliefBase}
	 * and {@link PlanLibrary} as belief base and plan library respectively, and
	 * adds initial beliefs and plans.
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
		this(id, null, initialBeliefs, null, initialPlans);
	}

	/**
	 * Associates a capability to this capability.
	 * 
	 * @param capability
	 *            the capability to be associated.
	 */
	public final void addAssociatedCapability(Capability capability) {
		this.associationTargets.add(capability);
		capability.associationSources.add(this);
		resetAgentCapabilities();
	}

	/**
	 * Adds the set of plans of this capability that can achieve the given goal
	 * to a map of candidate plans. It checks its plan library and the part
	 * capabilities, recursively.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 * @param candidatePlansMap
	 *            the map to which the set of plans that can achieve the goal
	 *            should be added.
	 */
	public void addCandidatePlans(Goal goal,
			Map<Capability, Set<Plan>> candidatePlansMap) {
		candidatePlansMap.put(this, planLibrary.getCandidatePlans(goal));
		for (Capability part : partCapabilities) {
			part.addCandidatePlans(goal, candidatePlansMap);
		}
	}

	final void addIntention(Intention intention) {
		this.intentions.add(intention);
	}

	/**
	 * Adds a capability as part of this capability, which is a
	 * whole-capability.
	 * 
	 * @param partCapability
	 *            the part capability to be added.
	 */
	public final void addPartCapability(Capability partCapability) {
		if (partCapability.wholeCapability != null) {
			throw new IllegalArgumentException(
					"Part capability already binded to another whole capability.");
		}
		partCapability.wholeCapability = this;
		this.partCapabilities.add(partCapability);

		resetAgentCapabilities();
	}

	/**
	 * Checks if this capability has a plan that can process the given message.
	 * It checks the plan library of this capabilities and, if cannot handle it,
	 * it checks part capabilities, recursively.
	 * 
	 * @param msg
	 *            the message to be checked.
	 * @return true if this capability has at least a plan that can process the
	 *         message.
	 */
	public boolean canHandle(ACLMessage msg) {
		if (planLibrary.canHandle(msg)) {
			return true;
		} else {
			for (Capability part : partCapabilities) {
				if (part.canHandle(msg)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Returns true if the object given as parameter is a capability and has the
	 * same full id of this capability.
	 * 
	 * @param obj
	 *            the object to be tested as equals to this plan.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (!(obj instanceof Capability))
			return false;
		return getFullId().equals(((Capability) obj).getFullId());
	}

	/**
	 * This method is responsible for selecting a set of goals that must be
	 * tried to be achieved (intentions) from the set of goals. It delegates its
	 * responsibility to the {@link DeliberationFunction} set in this
	 * capability.
	 * 
	 * @param goals
	 *            the list of current goals dispatched by the capability
	 *            associated with this strategy.
	 * 
	 * @return the list of selected goals (which are in the for of
	 *         {@link GoalDescription}), which will become intentions.
	 * 
	 * @see DeliberationFunction
	 */
	public final Set<GoalDescription> filter(Set<GoalDescription> goals) {
		return this.deliberationFunction.filter(goals);
	}

	/**
	 * This method is responsible for generating new goals or dropping existing
	 * ones. It delegates its responsibility to the
	 * {@link OptionGenerationFunction} set in this capability.
	 * 
	 * @param goalUpdateSet
	 *            a three-set object containing current goals with their status,
	 *            and dropped and generated goals.
	 * 
	 * @see OptionGenerationFunction
	 */
	public final void generateGoals(GoalUpdateSet goalUpdateSet) {
		this.optionGenerationFunction.generateGoals(goalUpdateSet);
	}

	/**
	 * Returns all capabilities with which this capability is associated.
	 * 
	 * @return the associated capabilities.
	 */
	public Set<Capability> getAssociatedCapabilities() {
		return associationTargets;
	}

	/**
	 * Returns this capability belief base.
	 * 
	 * @return the beliefBase.
	 */
	public final BeliefBase getBeliefBase() {
		return beliefBase;
	}

	/**
	 * Returns the belief revision strategy of this capability.
	 * 
	 * @return the beliefRevisionStrategy.
	 */
	public final BeliefRevisionStrategy getBeliefRevisionStrategy() {
		return beliefRevisionStrategy;
	}

	/**
	 * Returns the deliberation function of this capability.
	 * 
	 * @return the deliberationFunction.
	 */
	public final DeliberationFunction getDeliberationFunction() {
		return deliberationFunction;
	}

	/**
	 * Returns the full id of this capability, which is its id prefixed by all
	 * whole-capabilities' ids.
	 * 
	 * @return the full id of this capability.
	 */
	public final String getFullId() {
		StringBuffer sb = new StringBuffer();
		getFullId(sb);
		return sb.toString();
	}

	private final void getFullId(StringBuffer sb) {
		if (wholeCapability != null) {
			wholeCapability.getFullId(sb);
			sb.append(".");
		}
		sb.append(id);
	}

	/**
	 * Returns this capability id.
	 * 
	 * @return the id.
	 */
	public String getId() {
		return id;
	}

	final Collection<Intention> getIntentions() {
		return intentions;
	}

	/**
	 * Returns the agent that this capability is associated with.
	 * 
	 * @return the agent.
	 */
	public final BDIAgent getMyAgent() {
		return this.myAgent;
	}

	/**
	 * Returns the option generation function of this capability.
	 * 
	 * @return the optionGenerationFunction.
	 */
	public final OptionGenerationFunction getOptionGenerationFunction() {
		return optionGenerationFunction;
	}

	/**
	 * Returns the parts of this capability.
	 * 
	 * @return the partCapabilities.
	 */
	public final Set<Capability> getPartCapabilities() {
		return partCapabilities;
	}

	/**
	 * Returns the plan library of this capability.
	 * 
	 * @return the planLibrary.
	 */
	public final PlanLibrary getPlanLibrary() {
		return planLibrary;
	}

	/**
	 * Returns the plan selection strategy of this capability.
	 * 
	 * @return the planSelectionStrategy.
	 */
	public final PlanSelectionStrategy getPlanSelectionStrategy() {
		return planSelectionStrategy;
	}

	/**
	 * Returns the whole-capability, if this is a part capability.
	 * 
	 * @return the wholeCapability.
	 */
	public final Capability getWholeCapability() {
		return wholeCapability;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return this.getFullId().hashCode();
	}

	/**
	 * Dissociates a capability of this capability.
	 * 
	 * @param capability
	 *            the capability to be dissociated.
	 */
	public final void removeAssociatedCapability(Capability capability) {
		this.associationTargets.remove(capability);
		capability.associationSources.remove(this);
		resetAgentCapabilities();
	}

	final void removeIntention(Intention intention) {
		this.intentions.remove(intention);
	}

	/**
	 * Removes a capability as part of this capability, which is a
	 * whole-capability.
	 * 
	 * @param partCapability
	 *            the part capability to be removed.
	 * 
	 * @return true if the capability was removed, false otherwise.
	 */
	public final boolean removePartCapability(Capability partCapability) {
		boolean removed = this.partCapabilities.remove(partCapability);
		if (removed) {
			partCapability.wholeCapability = null;
			resetAgentCapabilities();
		}
		return removed;
	}

	private final void resetAgentCapabilities() {
		if (myAgent != null) {
			myAgent.resetAllCapabilities();
		}
	}

	/**
	 * This method is responsible for reviewing beliefs of this capability. It
	 * delegates its responsibility to the {@link BeliefRevisionStrategy} set in
	 * this capability.
	 * 
	 * @see BeliefRevisionStrategy
	 */
	public final void reviewBeliefs() {
		this.beliefRevisionStrategy.reviewBeliefs();
	}

	/**
	 * This method is responsible for selecting a plan to achieve a goals. It
	 * delegates its responsibility to the {@link PlanSelectionStrategy} set in
	 * this capability.
	 * 
	 * @param goal
	 *            the goal that must be achieved.
	 * @param candidatePlans
	 *            the plans that can achieve the goal.
	 * @return the selected plan.
	 * 
	 * @see PlanSelectionStrategy
	 */
	public final Plan selectPlan(Goal goal, Set<Plan> candidatePlans) {
		return this.planSelectionStrategy.selectPlan(goal, candidatePlans);
	}

	/**
	 * Sets the belief revision strategy of this capability.
	 * 
	 * @param beliefRevisionStrategy
	 *            the beliefRevisionStrategy to set
	 */
	public final void setBeliefRevisionStrategy(
			BeliefRevisionStrategy beliefRevisionStrategy) {
		if (beliefRevisionStrategy == null) {
			this.beliefRevisionStrategy = new DefaultBeliefRevisionStrategy();
		} else {
			this.beliefRevisionStrategy.setCapability(null);
			this.beliefRevisionStrategy = beliefRevisionStrategy;
		}
		this.beliefRevisionStrategy.setCapability(this);
	}

	/**
	 * Sets the deliberation function of this capability.
	 * 
	 * @param deliberationFunction
	 *            the deliberationFunction to set
	 */
	public final void setDeliberationFunction(
			DeliberationFunction deliberationFunction) {
		if (deliberationFunction == null) {
			this.deliberationFunction = new DefaultDeliberationFunction();
		} else {
			this.deliberationFunction.setCapability(null);
			this.deliberationFunction = deliberationFunction;
		}
		this.deliberationFunction.setCapability(this);
	}

	/**
	 * This method attaches a capability to an agent. It also sets up the
	 * capability by adding all annotated fields to this capability, including
	 * from its parents. It also invokes the {@link #setup()} method, so that
	 * further setup customizations can be performed.
	 * 
	 * @param myAgent
	 *            the myAgent to set
	 */
	final void setMyAgent(BDIAgent myAgent) {
		synchronized (this) {
			if (this.myAgent != null && myAgent == null) {
				takeDown();
			}
			this.myAgent = myAgent;
			if (this.myAgent != null && !started) {
				// TODO Adds all annotated fields.
				setup();
				this.started = true;
			}
		}
	}

	/**
	 * Sets the option generation function of this capability.
	 * 
	 * @param optionGenerationFunction
	 *            the optionGenerationFunction to set
	 */
	public final void setOptionGenerationFunction(
			OptionGenerationFunction optionGenerationFunction) {
		if (optionGenerationFunction == null) {
			this.optionGenerationFunction = new DefaultOptionGenerationFunction();
		} else {
			this.optionGenerationFunction.setCapability(null);
			this.optionGenerationFunction = optionGenerationFunction;
		}
		this.optionGenerationFunction.setCapability(this);
	}

	/**
	 * Sets the plan selection strategy of this capability.
	 * 
	 * @param planSelectionStrategy
	 *            the planSelectionStrategy to set
	 */
	public final void setPlanSelectionStrategy(
			PlanSelectionStrategy planSelectionStrategy) {
		if (planSelectionStrategy == null) {
			this.planSelectionStrategy = new DefaultPlanSelectionStrategy();
		} else {
			this.planSelectionStrategy.setCapability(null);
			this.planSelectionStrategy = planSelectionStrategy;
		}
		this.planSelectionStrategy.setCapability(this);
	}

	/**
	 * This is an empty holder for being overridden by subclasses. It is used to
	 * initialize the capability. This method is invoked when this capability is
	 * attached to an agent for the first time. It may be used to add initial
	 * plans and beliefs. The reasoning strategies of this capability are
	 * initialized in the constructor with default strategies. This method may
	 * also customize them.
	 */
	protected void setup() {

	}

	/**
	 * This is an empty holder for being overridden by subclasses. It is used to
	 * clean up a capability when it is removed from an agent.
	 */
	protected void takeDown() {

	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return id;
	}

}
