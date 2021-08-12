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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.annotation.GoalOwner;
import bdi4jade.belief.AttributeBelief;
import bdi4jade.belief.Belief;
import bdi4jade.belief.BeliefBase;
import bdi4jade.belief.NamedBelief;
import bdi4jade.belief.TransientBelief;
import bdi4jade.belief.TransientBeliefSet;
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
import bdi4jade.util.ReflectionUtils;
import jade.lang.acl.ACLMessage;

/**
 * This capability represents a component that aggregates the mental attitudes
 * defined by the BDI architecture. It has a belief base with the associated
 * beliefs (knowledge) and a plan library.
 * 
 * @author Ingrid Nunes
 */
public class Capability implements Serializable {

	private static final long serialVersionUID = -4922359927943108421L;
	private static final Log log = LogFactory.getLog(Capability.class);

	private final Set<Capability> associationSources;
	private final Set<Capability> associationTargets;
	protected final BeliefBase beliefBase;
	private BeliefRevisionStrategy beliefRevisionStrategy;
	private DeliberationFunction deliberationFunction;
	private Map<Class<? extends Capability>, Set<Capability>> fullAccessOwnersMap;
	protected final String id;
	private final Collection<Intention> intentions;
	private BDIAgent myAgent;
	private OptionGenerationFunction optionGenerationFunction;
	private final List<Class<? extends Capability>> parentCapabilities;
	private final Set<Capability> partCapabilities;
	protected final PlanLibrary planLibrary;
	private PlanSelectionStrategy planSelectionStrategy;
	private Map<Class<? extends Capability>, Set<Capability>> restrictedAccessOwnersMap;
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
	public Capability(Set<Belief<?, ?>> initialBeliefs, Set<Plan> initialPlans) {
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
			Set<Belief<?, ?>> initialBeliefs, PlanLibrary planLibrary,
			Set<Plan> initialPlans) {
		this.intentions = new LinkedList<>();
		this.parentCapabilities = generateParentCapabilities();
		log.debug("Parent capabilities: " + parentCapabilities);
		this.started = false;

		// Id initialization
		if (id == null) {
			if (this.getClass().getCanonicalName() == null
					|| Capability.class.equals(this.getClass())) {
				this.id = Capability.class.getName()
						+ System.currentTimeMillis();
			} else {
				this.id = this.getClass().getName();
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
		setBeliefRevisionStrategy(null);
		setOptionGenerationFunction(null);
		setDeliberationFunction(null);
		setPlanSelectionStrategy(null);

		computeGoalOwnersMap();
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
	public Capability(String id, Set<Belief<?, ?>> initialBeliefs,
			Set<Plan> initialPlans) {
		this(id, null, initialBeliefs, null, initialPlans);
	}

	/**
	 * Adds by reflection capability components, such as beliefs and plans,
	 * according to annotated fields. This method is invoked by for capability
	 * class, and all parent classes.
	 * 
	 * @param capabilityClass
	 *            the capability class of which fields should me added to this
	 *            capability.
	 */
	protected void addAnnotatedFields(
			Class<? extends Capability> capabilityClass) {
		for (Field field : capabilityClass.getDeclaredFields()) {
			boolean b = field.isAccessible();
			field.setAccessible(true);
			try {
				if (field.isAnnotationPresent(bdi4jade.annotation.Belief.class)) {
					if (Belief.class.isAssignableFrom(field.getType())) {
						Belief<?, ?> belief = (Belief<?, ?>) field.get(this);
						this.getBeliefBase().addBelief(belief);
					} else {
						this.getBeliefBase().addBelief(
								new AttributeBelief(this, field));
					}
				} else if (field
						.isAnnotationPresent(bdi4jade.annotation.TransientBelief.class)) {
					bdi4jade.annotation.TransientBelief annotation = field
							.getAnnotation(bdi4jade.annotation.TransientBelief.class);
					Object value = field.get(this);
					if ("".equals(annotation.name())) {
						this.getBeliefBase().addBelief(
								new NamedBelief(field.getName(), value));
					} else {
						this.getBeliefBase().addBelief(
								new TransientBelief(annotation.name(), value));
					}
				} else if (field
						.isAnnotationPresent(bdi4jade.annotation.TransientBeliefSet.class)) {
					bdi4jade.annotation.TransientBeliefSet annotation = field
							.getAnnotation(bdi4jade.annotation.TransientBeliefSet.class);
					String name = "".equals(annotation.name()) ? field
							.getName() : annotation.name();
					Object value = field.get(this);
					if (Set.class.isAssignableFrom(field.getType())) {
						this.getBeliefBase().addBelief(
								new TransientBeliefSet(name, (Set) value));
					} else {
						throw new ClassCastException("Field " + field.getName()
						+ " should be assignable to " + Set.class.getName());
					}
				} else if (field
						.isAnnotationPresent(bdi4jade.annotation.Plan.class)) {
					if (Plan.class.isAssignableFrom(field.getType())) {
						Plan plan = (Plan) field.get(this);
						this.getPlanLibrary().addPlan(plan);
					} else {
						throw new ClassCastException("Field " + field.getName()
								+ " should be a Plan");
					}
				} else if (field
						.isAnnotationPresent(bdi4jade.annotation.AssociatedCapability.class)) {
					if (Capability.class.isAssignableFrom(field.getType())) {
						Capability capability = (Capability) field.get(this);
						this.addAssociatedCapability(capability);
					} else {
						throw new ClassCastException("Field " + field.getName()
								+ " should be a Capability");
					}
				} else if (field
						.isAnnotationPresent(bdi4jade.annotation.PartCapability.class)) {
					if (Capability.class.isAssignableFrom(field.getType())) {
						Capability capability = (Capability) field.get(this);
						this.addPartCapability(capability);
					} else {
						throw new ClassCastException("Field " + field.getName()
								+ " should be a Capability");
					}
				}
			} catch (Exception exc) {
				log.warn(exc);
				exc.printStackTrace();
			}
			field.setAccessible(b);
		}
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
		this.computeGoalOwnersMap();
		capability.computeGoalOwnersMap();
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
		Set<Plan> plans = planLibrary.getCandidatePlans(goal);
		if (!plans.isEmpty()) {
			candidatePlansMap.put(this, plans);
		}
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

		partCapability.computeGoalOwnersMap();
		this.computeGoalOwnersMap();
		resetAgentCapabilities();
	}

	final Set<Capability> addRelatedCapabilities(Set<Capability> capabilities) {
		for (Capability part : partCapabilities) {
			if (!capabilities.contains(part)) {
				capabilities.add(part);
				part.addRelatedCapabilities(capabilities);
			}
		}
		for (Capability target : associationTargets) {
			if (!capabilities.contains(target)) {
				capabilities.add(target);
				target.addRelatedCapabilities(capabilities);
			}
		}
		return capabilities;
	}

	/**
	 * Checks if this capability has a plan that can achieve the given goal. It
	 * checks the plan library of this capabilities and, if cannot achieve it,
	 * it checks part capabilities, recursively.
	 * 
	 * @param goal
	 *            the goal to be checked.
	 * @return true if this capability has at least a plan that can achieve the
	 *         goal.
	 */
	public boolean canAchieve(Goal goal) {
		if (planLibrary.canAchieve(goal)) {
			return true;
		} else {
			for (Capability part : partCapabilities) {
				if (part.canAchieve(goal)) {
					return true;
				}
			}
		}
		return false;
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

	private final void computeGoalOwnersMap() {
		this.fullAccessOwnersMap = new HashMap<>();
		ReflectionUtils.addGoalOwner(fullAccessOwnersMap, this);
		if (wholeCapability != null) {
			ReflectionUtils.addGoalOwner(fullAccessOwnersMap, wholeCapability);
		}
		this.restrictedAccessOwnersMap = new HashMap<>();
		for (Capability capability : associationTargets) {
			ReflectionUtils.addGoalOwner(restrictedAccessOwnersMap, capability);
		}
		for (Capability capability : partCapabilities) {
			ReflectionUtils.addGoalOwner(restrictedAccessOwnersMap, capability);
		}
		log.debug("Full access owners: " + fullAccessOwnersMap);
		log.debug("Restricted access owners: " + restrictedAccessOwnersMap);
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

	@SuppressWarnings("unchecked")
	private final List<Class<? extends Capability>> generateParentCapabilities() {
		List<Class<? extends Capability>> parentCapabilities = new LinkedList<>();
		Class<?> currentClass = this.getClass().getSuperclass();
		while (Capability.class.isAssignableFrom(currentClass)
				&& !Capability.class.equals(currentClass)) {
			parentCapabilities.add((Class<Capability>) currentClass);
			currentClass = currentClass.getSuperclass();
		}
		return parentCapabilities;
	}

	/**
	 * Returns all capabilities with which this capability is associated.
	 * 
	 * @return the associated capabilities.
	 */
	public final Set<Capability> getAssociatedCapabilities() {
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
	 * Returns the capability instances that owns a dispatched goal, considering
	 * the superclasses of this capability, its associations and compositions.
	 * 
	 * A capability may dispatch its own goals and goals of its parents. It may
	 * also dispatch external goals of associated or part capabilities (and
	 * their parents), and all goals of whole capabilities.
	 * 
	 * This method thus searches all capabilities that have a relationship with
	 * this capability (either inheritance, composition or association) and
	 * finds the concrete capability instances whose definition owns a goal
	 * (specified with the {@link GoalOwner} annotation in goals).
	 * 
	 * If this method returns an empty set, it means that this capability has no
	 * access to the goal owned by capabilities of the given class.
	 * 
	 * @param owner
	 *            the capability class that is the goal owner.
	 * @param internal
	 *            the boolean that indicates whether the goal is internal or
	 *            external.
	 * @return the capability instances related to this capability (or the
	 *         capability itself) that owns the goal, or an empty set if the
	 *         capability has no access to goals owned by capability of the
	 *         given class.
	 */
	public final Set<Capability> getGoalOwner(
			Class<? extends Capability> owner, boolean internal) {
		Set<Capability> owners = new HashSet<>();

		Set<Capability> fullAccessOwners = fullAccessOwnersMap.get(owner);
		if (fullAccessOwners != null)
			owners.addAll(fullAccessOwners);

		if (!internal) {
			Set<Capability> restrictedAccessOwners = restrictedAccessOwnersMap
					.get(owner);
			if (restrictedAccessOwners != null)
				owners.addAll(restrictedAccessOwners);
		}

		return owners;
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
	 * Returns the classes of all parent capabilities of this capability.
	 * 
	 * @return the parentCapabilities.
	 */
	public final List<Class<? extends Capability>> getParentCapabilities() {
		return parentCapabilities;
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
		computeGoalOwnersMap();
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
			computeGoalOwnersMap();
			resetAgentCapabilities();
		}
		return removed;
	}

	private final void resetAgentCapabilities() {
		if (myAgent != null) {
			if (myAgent instanceof AbstractBDIAgent) {
				((AbstractBDIAgent) myAgent).resetAllCapabilities();
			}
		}
	}

	/**
	 * Sets the belief revision strategy of this capability.
	 * 
	 * @param beliefRevisionStrategy
	 *            the beliefRevisionStrategy to set.
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
	 *            the deliberationFunction to set.
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
	final synchronized void setMyAgent(BDIAgent myAgent) {
		if (this.myAgent != null && myAgent == null) {
			takeDown();
		}
		this.myAgent = myAgent;
		if (this.myAgent != null && !started) {
			addAnnotatedFields(this.getClass());
			for (Class<? extends Capability> parentCapabilityClass : parentCapabilities) {
				addAnnotatedFields(parentCapabilityClass);
			}
			setup();
			this.started = true;
		}
	}

	/**
	 * Sets the option generation function of this capability.
	 * 
	 * @param optionGenerationFunction
	 *            the optionGenerationFunction to set.
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
	 *            the planSelectionStrategy to set.
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
