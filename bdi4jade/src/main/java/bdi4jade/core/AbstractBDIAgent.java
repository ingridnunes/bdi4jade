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

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.proto.states.MsgReceiver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.belief.Belief;
import bdi4jade.core.GoalUpdateSet.GoalDescription;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.goal.Softgoal;
import bdi4jade.message.BDIAgentMsgReceiver;
import bdi4jade.reasoning.AgentBeliefRevisionStrategy;
import bdi4jade.reasoning.AgentDeliberationFunction;
import bdi4jade.reasoning.AgentOptionGenerationFunction;
import bdi4jade.reasoning.AgentPlanSelectionStrategy;
import bdi4jade.reasoning.DefaultAgentBeliefRevisionStrategy;
import bdi4jade.reasoning.DefaultAgentDeliberationFunction;
import bdi4jade.reasoning.DefaultAgentOptionGenerationFunction;
import bdi4jade.reasoning.DefaultAgentPlanSelectionStrategy;
import bdi4jade.util.ReflectionUtils;

/**
 * This class is an abstract implementation of the {@link BDIAgent} interface.
 * It is an extension of {@link Agent}. It also has a set of {@link Capability}
 * - an agent is an aggregation of capabilities, and a {@link MsgReceiver}
 * behavior to receive all messages that the agent current plans can process.
 * 
 * @author Ingrid Nunes
 */
public abstract class AbstractBDIAgent extends Agent implements BDIAgent {

	/**
	 * This class is a {@link CyclicBehaviour} that runs during all the
	 * {@link BDIAgent} life in order to provide the reasoning engine.
	 * 
	 * @author Ingrid Nunes
	 */
	class BDIInterpreter extends CyclicBehaviour {

		private static final long serialVersionUID = -6991759791322598475L;

		private BDIInterpreter(BDIAgent bdiAgent) {
			super((Agent) bdiAgent);
		}

		/**
		 * This method is a variation of the BDI-interpreter cycle of the BDI
		 * architecture. It first reviews the beliefs of this agent, by invoking
		 * the {@link AgentBeliefRevisionStrategy#reviewBeliefs()} method.
		 * 
		 * After it removes from the intention set the ones that are finished,
		 * i.e. associated with goals with status achieved, no longer desired or
		 * unachievable, and notifies goal listeners about this event. This is
		 * performed using the {@link #processIntentions(Collection)} method.
		 * 
		 * Then, it generate a an updated set of goals, dropping existing ones
		 * that are no longer desired and also creating new ones. This updated
		 * set of goals is given by the
		 * {@link AgentOptionGenerationFunction#generateGoals(GoalUpdateSet, Map)}
		 * method.
		 * 
		 * Finally, from the set of current goals, they are now filtered, by
		 * invoking the {@link AgentDeliberationFunction#filter(Set, Map)}
		 * method, to select the current agent intentions. The non-selected
		 * goals will be set to wait ({@link Intention#doWait()}) and the
		 * selected ones will be tried to be achieved (
		 * {@link Intention#tryToAchive()}).
		 * 
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			log.trace("Beginning BDI-interpreter cycle.");

			log.trace("Reviewing beliefs.");
			beliefRevisionStrategy.reviewBeliefs();

			synchronized (allIntentions) {
				// Removing finished goals and generate appropriate goal events
				GoalUpdateSet agentGoalUpdateSet = processIntentions(agentIntentions);
				Map<Capability, GoalUpdateSet> capabilityGoalUpdateSets = new HashMap<>();
				for (Capability capability : capabilities) {
					GoalUpdateSet capabilityGoalUpdateSet = processIntentions(capability
							.getIntentions());
					capabilityGoalUpdateSets.put(capability,
							capabilityGoalUpdateSet);
				}

				// Generating new goals and choosing goals to drop
				optionGenerationFunction.generateGoals(agentGoalUpdateSet,
						capabilityGoalUpdateSets);

				// Adding generated goals
				for (GoalDescription goal : agentGoalUpdateSet
						.getGeneratedGoals()) {
					try {
						Intention intention = addIntention(
								goal.getDispatcher(), goal.getGoal(),
								goal.getListener());
						if (intention != null)
							agentGoalUpdateSet.addIntention(intention);
					} catch (IllegalAccessException exc) {
						log.error(exc);
					}
				}
				for (GoalUpdateSet goalUpdateSet : capabilityGoalUpdateSets
						.values()) {
					for (GoalDescription goal : goalUpdateSet
							.getGeneratedGoals()) {
						try {
							Intention intention = addIntention(
									goal.getDispatcher(), goal.getGoal(),
									goal.getListener());
							if (intention != null)
								goalUpdateSet.addIntention(intention);
						} catch (IllegalAccessException exc) {
							log.error(exc);
						}
					}
				}

				// Removing dropped goals
				for (GoalDescription goal : agentGoalUpdateSet
						.getDroppedGoals()) {
					goal.getIntention().noLongerDesire();
					fireGoalEvent(goal.getIntention());
					agentIntentions.remove(goal.getIntention());
					allIntentions.remove(goal.getGoal());
					agentGoalUpdateSet.removeIntention(goal);
				}
				for (GoalUpdateSet goalUpdateSet : capabilityGoalUpdateSets
						.values()) {
					for (GoalDescription goal : goalUpdateSet.getDroppedGoals()) {
						goal.getIntention().noLongerDesire();
						fireGoalEvent(goal.getIntention());
						goal.getDispatcher().removeIntention(
								goal.getIntention());
						allIntentions.remove(goal.getGoal());
						goalUpdateSet.removeIntention(goal);
					}
				}

				// Filtering options
				Map<Capability, Set<GoalDescription>> capabilityGoals = new HashMap<>();
				for (Capability capability : capabilityGoalUpdateSets.keySet()) {
					capabilityGoals.put(capability, capabilityGoalUpdateSets
							.get(capability).getCurrentGoals());
				}
				Set<Goal> selectedGoals = deliberationFunction.filter(
						agentGoalUpdateSet.getCurrentGoals(), capabilityGoals);

				log.trace("Selected goals to be intentions: "
						+ selectedGoals.size());
				for (Intention intention : allIntentions.values()) {
					if (selectedGoals.contains(intention.getGoal())) {
						intention.tryToAchive();
					} else {
						intention.doWait();
					}
				}

				if (allIntentions.isEmpty()) {
					log.trace("No goals or intentions: blocking cycle.");
					this.block();
				}
			}

			log.trace("BDI-interpreter cycle finished.");
		}

		/**
		 * Processes all intentions of the given collection. Intentions
		 * associated with goals that finished are removed and goal listeners (
		 * {@link GoalListener}) are notified. Goal listeners are also notified
		 * if a plan failed while trying to achieve a goal (intentions with
		 * {@link GoalStatus#PLAN_FAILED}).
		 * 
		 * @param intentions
		 *            the collection of intentions to be processed.
		 * @return the {@link GoalUpdateSet} with current goals initialized with
		 *         current intentions.
		 */
		private GoalUpdateSet processIntentions(Collection<Intention> intentions) {
			GoalUpdateSet goalUpdateSet = new GoalUpdateSet();
			List<Intention> intentionsList = new ArrayList<>(intentions);
			for (Intention intention : intentionsList) {
				GoalStatus status = intention.getStatus();
				if (status.isFinished()) {
					fireGoalEvent(intention);
					intentions.remove(intention);
					allIntentions.remove(intention.getGoal());
				} else {
					if (GoalStatus.PLAN_FAILED.equals(status)) {
						fireGoalEvent(intention);
					}
					goalUpdateSet.addIntention(intention);
				}
			}
			return goalUpdateSet;
		}

	}

	private static final Log log = LogFactory.getLog(AbstractBDIAgent.class);
	private static final long serialVersionUID = -841774495336214256L;

	private final Collection<Intention> agentIntentions;
	private final Set<Capability> aggregatedCapabilities;
	private final Map<Goal, Intention> allIntentions;
	private final BDIInterpreter bdiInterpreter;
	private AgentBeliefRevisionStrategy beliefRevisionStrategy;
	private Set<Capability> capabilities;
	private AgentDeliberationFunction deliberationFunction;
	protected final List<GoalListener> goalListeners;
	private AgentOptionGenerationFunction optionGenerationFunction;
	private AgentPlanSelectionStrategy planSelectionStrategy;
	private Map<Class<? extends Capability>, Set<Capability>> restrictedAccessOwnersMap;
	private final Set<Softgoal> softgoals;

	/**
	 * Default constructor.
	 */
	public AbstractBDIAgent() {
		this.bdiInterpreter = new BDIInterpreter(this);
		this.capabilities = new HashSet<>();
		this.restrictedAccessOwnersMap = new HashMap<>();
		this.allIntentions = new HashMap<>();
		this.aggregatedCapabilities = new HashSet<>();
		this.agentIntentions = new LinkedList<>();
		this.softgoals = new HashSet<>();
		this.goalListeners = new LinkedList<>();

		// Initializing reasoning strategies
		setBeliefRevisionStrategy(null);
		setOptionGenerationFunction(null);
		setDeliberationFunction(null);
		setPlanSelectionStrategy(null);
	}

	/**
	 * Adds a capability to this agent.
	 * 
	 * @param capability
	 *            capability to be added.
	 */
	void addCapability(Capability capability) {
		synchronized (aggregatedCapabilities) {
			this.aggregatedCapabilities.add(capability);
			resetAllCapabilities();
			computeGoalOwnersMap();
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#addGoal(bdi4jade.core.Capability,
	 *      bdi4jade.goal.Goal)
	 */
	@Override
	public final boolean addGoal(Capability dispatcher, Goal goal) {
		try {
			addIntention(dispatcher, goal, null);
			return true;
		} catch (IllegalAccessException exc) {
			log.error(exc);
			return false;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#addGoal(bdi4jade.core.Capability,
	 *      bdi4jade.goal.Goal, bdi4jade.event.GoalListener)
	 */
	@Override
	public final boolean addGoal(Capability dispatcher, Goal goal,
			GoalListener goalListener) {
		try {
			addIntention(dispatcher, goal, goalListener);
			return true;
		} catch (IllegalAccessException exc) {
			log.error(exc);
			return false;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#addGoal(bdi4jade.goal.Goal)
	 */
	@Override
	public final boolean addGoal(Goal goal) {
		try {
			addIntention(null, goal, null);
			return true;
		} catch (IllegalAccessException exc) {
			log.error(exc);
			return false;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#addGoal(bdi4jade.goal.Goal,
	 *      bdi4jade.event.GoalListener)
	 */
	@Override
	public final boolean addGoal(Goal goal, GoalListener goalListener) {
		try {
			addIntention(null, goal, goalListener);
			return true;
		} catch (IllegalAccessException exc) {
			log.error(exc);
			return false;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#addGoalListener(bdi4jade.event.GoalListener)
	 */
	@Override
	public final void addGoalListener(GoalListener goalListener) {
		synchronized (goalListeners) {
			goalListeners.add(goalListener);
		}
	}

	/**
	 * Adds a new goal to this agent to be achieved by creating an intention. It
	 * also may add a listener to observe events related to this goal.
	 * 
	 * @param dispatcher
	 *            the capability that dispatched this goal.
	 * @param goal
	 *            the goal to be achieved.
	 * @param goalListener
	 *            the listener to be notified.
	 */
	private final Intention addIntention(Capability dispatcher, Goal goal,
			GoalListener goalListener) throws IllegalAccessException {
		Intention intention = null;
		synchronized (allIntentions) {
			intention = allIntentions.get(goal);
			if (intention != null) {
				log.info("This agent already has goal: " + goal);
				if (goalListener != null) {
					intention.addGoalListener(goalListener);
				}
				return null;
			}

			intention = new Intention(goal, this, dispatcher);
			this.allIntentions.put(goal, intention);
			if (dispatcher == null) {
				agentIntentions.add(intention);
			} else {
				dispatcher.addIntention(intention);
			}
			if (goalListener != null) {
				intention.addGoalListener(goalListener);
			}
		}
		fireGoalEvent(new GoalEvent(goal));
		restart();
		return intention;
	}

	/**
	 * @see bdi4jade.core.BDIAgent#addSoftgoal(bdi4jade.goal.Softgoal)
	 */
	@Override
	public final void addSoftgoal(Softgoal softgoal) {
		synchronized (softgoals) {
			this.softgoals.add(softgoal);
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#canHandle(jade.lang.acl.ACLMessage)
	 */
	@Override
	public boolean canHandle(ACLMessage msg) {
		synchronized (aggregatedCapabilities) {
			for (Capability capability : aggregatedCapabilities) {
				if (capability.canHandle(msg)) {
					return true;
				}
			}
			return false;
		}
	}

	private final void computeGoalOwnersMap() {
		this.restrictedAccessOwnersMap = new HashMap<>();
		for (Capability capability : aggregatedCapabilities) {
			ReflectionUtils.addGoalOwner(restrictedAccessOwnersMap, capability);
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#dropGoal(bdi4jade.goal.Goal)
	 */
	@Override
	public final void dropGoal(Goal goal) {
		synchronized (allIntentions) {
			Intention intention = allIntentions.get(goal);
			if (intention != null) {
				intention.noLongerDesire();
			}
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#dropSoftoal(bdi4jade.goal.Softgoal)
	 */
	@Override
	public final void dropSoftoal(Softgoal softgoal) {
		synchronized (softgoals) {
			this.softgoals.remove(softgoal);
		}
	}

	/**
	 * Notifies all listeners, if any, about a goal event.
	 * 
	 * @param goalEvent
	 *            the event to notify.
	 */
	private final void fireGoalEvent(GoalEvent goalEvent) {
		synchronized (goalListeners) {
			for (GoalListener goalListener : goalListeners) {
				goalListener.goalPerformed(goalEvent);
			}
		}
	}

	/**
	 * Creates a goal event given an intention, and notifies all listeners, if
	 * any, about a goal event.
	 * 
	 * @param intention
	 *            the intention used to create the goal event.
	 */
	private final void fireGoalEvent(Intention intention) {
		Goal goal = intention.getGoal();
		GoalStatus status = intention.getStatus();
		log.debug("Goal: " + goal.getClass().getSimpleName() + " (" + status
				+ ") - " + goal);

		GoalEvent goalEvent = new GoalEvent(goal, status);
		synchronized (goalListeners) {
			for (GoalListener goalListener : goalListeners) {
				goalListener.goalPerformed(goalEvent);
			}
			for (GoalListener goalListener : intention.getGoalListeners()) {
				goalListener.goalPerformed(goalEvent);
			}
		}
	}

	/**
	 * @see BDIAgent#getAllCapabilities()
	 */
	@Override
	public final Collection<Capability> getAllCapabilities() {
		synchronized (aggregatedCapabilities) {
			return capabilities;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getBeliefRevisionStrategy()
	 */
	@Override
	public final AgentBeliefRevisionStrategy getBeliefRevisionStrategy() {
		return beliefRevisionStrategy;
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getBeliefs()
	 */
	@Override
	public final Collection<Belief<?, ?>> getBeliefs() {
		synchronized (aggregatedCapabilities) {
			Collection<Belief<?, ?>> beliefs = new LinkedList<>();
			for (Capability capability : capabilities) {
				beliefs.addAll(capability.getBeliefBase().getBeliefs());
			}
			return beliefs;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getCapabilities()
	 */
	public final Set<Capability> getCapabilities() {
		synchronized (aggregatedCapabilities) {
			return aggregatedCapabilities;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getDeliberationFunction()
	 */
	@Override
	public final AgentDeliberationFunction getDeliberationFunction() {
		return deliberationFunction;
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getGoalListeners()
	 */
	@Override
	public final List<GoalListener> getGoalListeners() {
		return goalListeners;
	}

	/**
	 * Returns the capability instances that owns a dispatched goal, considering
	 * the aggregated capabilities of this agent.
	 * 
	 * If this method returns an empty set, it means that this agent cannot add
	 * a goal without the scope of a dispatcher that has access to it.
	 * 
	 * @param owner
	 *            the capability class that is the goal owner.
	 * @param internal
	 *            a boolean indicated whether the goal is internal. It is true
	 *            if the goal is internal, false otherwise.
	 * @return the capability instances related to this capability that owns the
	 *         goal, or an empty set if the agent cannot add this goal.
	 */
	protected final Set<Capability> getGoalOwner(
			Class<? extends Capability> owner, boolean internal) {
		if (internal) {
			return new HashSet<Capability>();
		} else {
			Set<Capability> restrictedAccessOwners = restrictedAccessOwnersMap
					.get(owner);
			return restrictedAccessOwners == null ? new HashSet<Capability>()
					: restrictedAccessOwners;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getGoals()
	 */
	@Override
	public final Set<Goal> getGoals() {
		synchronized (allIntentions) {
			return allIntentions.keySet();
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getIntentions()
	 */
	@Override
	public final Set<Intention> getIntentions() {
		synchronized (allIntentions) {
			Set<Intention> activeIntentions = new HashSet<Intention>();
			for (Intention intention : activeIntentions) {
				if (!GoalStatus.WAITING.equals(intention.getStatus()))
					activeIntentions.add(intention);
			}
			return activeIntentions;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getOptionGenerationFunction()
	 */
	@Override
	public final AgentOptionGenerationFunction getOptionGenerationFunction() {
		return optionGenerationFunction;
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getPlanSelectionStrategy()
	 */
	@Override
	public final AgentPlanSelectionStrategy getPlanSelectionStrategy() {
		return planSelectionStrategy;
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getSoftgoals()
	 */
	@Override
	public final Set<Softgoal> getSoftgoals() {
		synchronized (softgoals) {
			return this.softgoals;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#hasGoal(bdi4jade.goal.Goal)
	 */
	@Override
	public boolean hasGoal(Goal goal) {
		return allIntentions.get(goal) != null;
	}

	/**
	 * This method initializes the BDI agent. It is invoked by the
	 * {@link #setup()} method. This is an empty method that should be overriden
	 * by subclasses.
	 */
	protected void init() {

	}

	/**
	 * Removes a capability from this agent.
	 * 
	 * @param capability
	 *            capability to be removed.
	 * 
	 * @return true if the capability exists and was removed, false otherwise.
	 */
	boolean removeCapability(Capability capability) {
		synchronized (aggregatedCapabilities) {
			boolean removed = this.aggregatedCapabilities.remove(capability);
			if (removed) {
				resetAllCapabilities();
				computeGoalOwnersMap();
			}
			return removed;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#removeGoalListener(bdi4jade.event.GoalListener)
	 */
	@Override
	public final void removeGoalListener(GoalListener goalListener) {
		synchronized (goalListeners) {
			goalListeners.remove(goalListener);
		}
	}

	final void resetAllCapabilities() {
		synchronized (aggregatedCapabilities) {
			Set<Capability> oldCapabilities = this.capabilities;
			Set<Capability> allCapabilities = new HashSet<>();
			for (Capability capability : aggregatedCapabilities) {
				allCapabilities.add(capability);
				capability.addRelatedCapabilities(allCapabilities);
			}
			this.capabilities = allCapabilities;
			log.debug("Capabilities: " + this.capabilities);

			Set<Capability> removedCapabilities = new HashSet<>(oldCapabilities);
			removedCapabilities.removeAll(allCapabilities);
			for (Capability capability : removedCapabilities) {
				capability.setMyAgent(null);
			}

			Set<Capability> addedCapabilities = new HashSet<>(allCapabilities);
			addedCapabilities.removeAll(oldCapabilities);
			for (Capability capability : addedCapabilities) {
				if (capability.getMyAgent() != null) {
					throw new IllegalArgumentException(
							"Capability already binded to another agent: "
									+ capability.getFullId());
				}
				capability.setMyAgent(this);
			}
		}
	}

	/**
	 * @see BDIAgent#restart()
	 */
	@Override
	public final void restart() {
		this.bdiInterpreter.restart();
	}

	/**
	 * Sets the belief revision strategy of this agent.
	 * 
	 * @param beliefRevisionStrategy
	 *            the beliefRevisionStrategy to set.
	 */
	public final void setBeliefRevisionStrategy(
			AgentBeliefRevisionStrategy beliefRevisionStrategy) {
		if (beliefRevisionStrategy == null) {
			this.beliefRevisionStrategy = new DefaultAgentBeliefRevisionStrategy();
		} else {
			this.beliefRevisionStrategy.setAgent(null);
			this.beliefRevisionStrategy = beliefRevisionStrategy;
		}
		this.beliefRevisionStrategy.setAgent(this);
	}

	/**
	 * Sets the deliberation function of this agent.
	 * 
	 * @param deliberationFunction
	 *            the deliberationFunction to set.
	 */
	public final void setDeliberationFunction(
			AgentDeliberationFunction deliberationFunction) {
		if (deliberationFunction == null) {
			this.deliberationFunction = new DefaultAgentDeliberationFunction();
		} else {
			this.deliberationFunction.setAgent(null);
			this.deliberationFunction = deliberationFunction;
		}
		this.deliberationFunction.setAgent(this);
	}

	/**
	 * Sets the option generation function of this agent.
	 * 
	 * @param optionGenerationFunction
	 *            the optionGenerationFunction to set.
	 */
	public final void setOptionGenerationFunction(
			AgentOptionGenerationFunction optionGenerationFunction) {
		if (optionGenerationFunction == null) {
			this.optionGenerationFunction = new DefaultAgentOptionGenerationFunction();
		} else {
			this.optionGenerationFunction.setAgent(null);
			this.optionGenerationFunction = optionGenerationFunction;
		}
		this.optionGenerationFunction.setAgent(this);
	}

	/**
	 * Sets the plan selection strategy of this agent.
	 * 
	 * @param planSelectionStrategy
	 *            the planSelectionStrategy to set.
	 */
	public final void setPlanSelectionStrategy(
			AgentPlanSelectionStrategy planSelectionStrategy) {
		if (planSelectionStrategy == null) {
			this.planSelectionStrategy = new DefaultAgentPlanSelectionStrategy();
		} else {
			this.planSelectionStrategy.setAgent(null);
			this.planSelectionStrategy = planSelectionStrategy;
		}
		this.planSelectionStrategy.setAgent(this);
	}

	/**
	 * Initializes the BDI agent. It adds the behavior to handle message
	 * received and can be processed by capabilities and the
	 * {@link BDIInterpreter} behavior as well. It invokes the {@link #init()}
	 * method, so that customized initializations can be perfomed by subclasses.
	 * 
	 * @see jade.core.Agent#setup()
	 */
	@Override
	protected final void setup() {
		this.addBehaviour(new BDIAgentMsgReceiver(this));
		this.addBehaviour(bdiInterpreter);
		init();
	}

	/**
	 * Removes all capabilities of this agent, before it stops its execution.
	 * 
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		synchronized (aggregatedCapabilities) {
			Iterator<Capability> iterator = aggregatedCapabilities.iterator();
			while (iterator.hasNext()) {
				removeCapability(iterator.next());
			}
		}
	}

}
