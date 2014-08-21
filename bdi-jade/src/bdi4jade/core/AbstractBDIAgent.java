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

import bdi4jade.annotation.GoalOwner;
import bdi4jade.belief.Belief;
import bdi4jade.core.GoalUpdateSet.GoalDescription;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.goal.Softgoal;
import bdi4jade.message.BDIAgentMsgReceiver;
import bdi4jade.plan.Plan;
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

		private BDIInterpreter(AbstractBDIAgent bdiAgent) {
			super(bdiAgent);
		}

		/**
		 * This method is a variation of the BDI-interpreter cycle of the BDI
		 * architecture. It first reviews the beliefs of this agent, by invoking
		 * the {@link BDIAgent#reviewBeliefs()} method.
		 * 
		 * After it removes from the intention set the ones that are finished,
		 * i.e. associated with goals with status achieved, no longer desired or
		 * unachievable, and notifies goal listeners about this event. This is
		 * performed using the {@link #processIntentions(Collection)} method.
		 * 
		 * Then, it generate a an updated set of goals, dropping existing ones
		 * that are no longer desired and also creating new ones. This updated
		 * set of goals is given by the
		 * {@link BDIAgent#generateGoals(GoalUpdateSet, Map)} method.
		 * 
		 * Finally, from the set of current goals, they are now filtered, by
		 * invoking the {@link BDIAgent#filter(Set, Map)} method, to select the
		 * current agent intentions. The non-selected goals will be set to wait
		 * ({@link Intention#doWait()}) and the selected ones will be tried to
		 * be achieved ({@link Intention#tryToAchive()}).
		 * 
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			log.trace("Beginning BDI-interpreter cycle.");

			log.trace("Reviewing beliefs.");
			reviewBeliefs();

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
				generateGoals(agentGoalUpdateSet, capabilityGoalUpdateSets);

				// Adding generated goals
				for (GoalDescription goal : agentGoalUpdateSet
						.getGeneratedGoals()) {
					Intention intention = addIntention(goal.getDispatcher(),
							goal.getGoal(), null);
					if (intention != null)
						agentGoalUpdateSet.addIntention(intention);
				}
				for (GoalUpdateSet goalUpdateSet : capabilityGoalUpdateSets
						.values()) {
					for (GoalDescription goal : goalUpdateSet
							.getGeneratedGoals()) {
						Intention intention = addIntention(
								goal.getDispatcher(), goal.getGoal(), null);
						if (intention != null)
							goalUpdateSet.addIntention(intention);
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
				Set<Goal> selectedGoals = filter(
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
			Iterator<Intention> it = intentions.iterator();
			while (it.hasNext()) {
				Intention intention = it.next();
				GoalStatus status = intention.getStatus();
				if (status.isFinished()) {
					fireGoalEvent(intention);
					it.remove();
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

	private static final long serialVersionUID = -841774495336214256L;

	private final Collection<Intention> agentIntentions;
	private final Set<Capability> aggregatedCapabilities;
	private final Map<Goal, Intention> allIntentions;
	private final BDIInterpreter bdiInterpreter;
	private Set<Capability> capabilities;
	protected final List<GoalListener> goalListeners;
	protected final Log log;
	private Map<Class<? extends Capability>, Set<Capability>> restrictedAccessOwnersMap;
	private final Set<Softgoal> softgoals;

	/**
	 * Default constructor.
	 */
	public AbstractBDIAgent() {
		this.log = LogFactory.getLog(this.getClass());
		this.bdiInterpreter = new BDIInterpreter(this);
		this.capabilities = new HashSet<>();
		this.restrictedAccessOwnersMap = new HashMap<>();
		this.allIntentions = new HashMap<>();
		this.aggregatedCapabilities = new HashSet<>();
		this.agentIntentions = new LinkedList<>();
		this.softgoals = new HashSet<>();
		this.goalListeners = new LinkedList<>();
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
		return addIntention(dispatcher, goal, null) == null ? false : true;
	}

	/**
	 * @see bdi4jade.core.BDIAgent#addGoal(bdi4jade.core.Capability,
	 *      bdi4jade.goal.Goal, bdi4jade.event.GoalListener)
	 */
	@Override
	public final boolean addGoal(Capability dispatcher, Goal goal,
			GoalListener goalListener) {
		return addIntention(dispatcher, goal, goalListener) == null ? false
				: true;
	}

	/**
	 * @see bdi4jade.core.BDIAgent#addGoal(bdi4jade.goal.Goal)
	 */
	@Override
	public final boolean addGoal(Goal goal) {
		return addIntention(null, goal, null) == null ? false : true;
	}

	/**
	 * @see bdi4jade.core.BDIAgent#addGoal(bdi4jade.goal.Goal,
	 *      bdi4jade.event.GoalListener)
	 */
	@Override
	public final boolean addGoal(Goal goal, GoalListener goalListener) {
		return addIntention(null, goal, goalListener) == null ? false : true;
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
			GoalListener goalListener) {
		synchronized (allIntentions) {
			if (allIntentions.get(goal) != null) {
				log.warn("This agent already has this goal.");
				return null;
			}

			Intention intention = null;
			try {
				intention = new Intention(goal, this, dispatcher);
			} catch (IllegalAccessException exc) {
				log.error(exc);
				return null;
			}

			this.allIntentions.put(goal, intention);
			if (dispatcher == null) {
				agentIntentions.add(intention);
			} else {
				dispatcher.addIntention(intention);
			}
			this.bdiInterpreter.restart();
			if (goalListener != null) {
				intention.addGoalListener(goalListener);
			}
			fireGoalEvent(new GoalEvent(goal));
			return intention;
		}
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
	 * This method is responsible for selecting a set of goals that must be
	 * tried to be achieved (intentions) from the set of goals. Its default
	 * implementation selects all agent goals (those not dispatched within the
	 * scope of a capability) to be achieved, and requests each of its
	 * capabilities to filter their goals. Subclasses may override this method
	 * to customize this deliberation function.
	 * 
	 * @param agentGoals
	 *            the set of agent goals, which are goals not dispatched within
	 *            the scope of a capability.
	 * @param capabilityGoals
	 *            the map from capabilities to their set of goals.
	 * 
	 * @return the list of selected goals, which will become intentions.
	 */
	protected Set<Goal> filter(Set<GoalDescription> agentGoals,
			Map<Capability, Set<GoalDescription>> capabilityGoals) {
		Set<Goal> selectedGoals = new HashSet<>();
		for (GoalDescription goalDescription : agentGoals) {
			selectedGoals.add(goalDescription.getGoal());
		}
		for (Capability capability : capabilityGoals.keySet()) {
			selectedGoals.addAll(capability.filter(capabilityGoals
					.get(capability)));
		}
		return selectedGoals;
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
	 * This method is responsible for generating new goals or dropping existing
	 * ones. Its default implementation requests each of its capabilities to
	 * generate or drop goals. Subclasses may override this method to customize
	 * this options generation function.
	 * 
	 * @param agentGoalUpdateSet
	 *            the {@link GoalUpdateSet} that contains the set of agent
	 *            current goals. It has also a set of dropped goals and
	 *            generated goals, which are used as outputs of this method.
	 * @param capabilityGoalUpdateSets
	 *            the map from capabilities to their goal update set.
	 */
	protected void generateGoals(GoalUpdateSet agentGoalUpdateSet,
			Map<Capability, GoalUpdateSet> capabilityGoalUpdateSets) {
		for (Capability capability : capabilityGoalUpdateSets.keySet()) {
			capability.generateGoals(capabilityGoalUpdateSets.get(capability));
		}
	}

	/**
	 * Returns the root capability of this agent.
	 * 
	 * @return the rootCapability
	 */
	protected Set<Capability> getAggregatedCapabilities() {
		synchronized (aggregatedCapabilities) {
			return aggregatedCapabilities;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getAllBeliefs()
	 */
	@Override
	public final Collection<Belief<?>> getAllBeliefs() {
		synchronized (aggregatedCapabilities) {
			Collection<Belief<?>> beliefs = new LinkedList<Belief<?>>();
			for (Capability capability : capabilities) {
				beliefs.addAll(capability.getBeliefBase().getBeliefs());
			}
			return beliefs;
		}
	}

	/**
	 * Returns all capabilities that are part of this agent. This included all
	 * capabilities composed or associated with other capabilities.
	 * 
	 * @return the capabilities.
	 */
	protected Collection<Capability> getAllCapabilities() {
		synchronized (aggregatedCapabilities) {
			return capabilities;
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getAllGoals()
	 */
	@Override
	public final Set<Goal> getAllGoals() {
		synchronized (allIntentions) {
			return allIntentions.keySet();
		}
	}

	/**
	 * @see bdi4jade.core.BDIAgent#getAllSoftgoals()
	 */
	@Override
	public final Set<Softgoal> getAllSoftgoals() {
		synchronized (softgoals) {
			return this.softgoals;
		}
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
	 *            the annotation with the goal owner.
	 * @return the capability instances related to this capability that owns the
	 *         goal, or an empty set if the agent cannot add this goal.
	 */
	protected final Set<Capability> getGoalOwner(GoalOwner owner) {
		Set<Capability> restrictedAccessOwners = restrictedAccessOwnersMap
				.get(owner.capability());
		return restrictedAccessOwners == null ? new HashSet<Capability>()
				: restrictedAccessOwners;
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
	 * This method is responsible for reviewing beliefs from this agent. Its
	 * default implementation requests each of its capabilities to review their
	 * individual set of beliefs. Subclasses may override this method to
	 * customize belief revision.
	 */
	protected void reviewBeliefs() {
		for (Capability capability : capabilities) {
			capability.reviewBeliefs();
		}
	}

	/**
	 * This method is responsible for selecting plans to achieve a goals of this
	 * agent. Its default implementation requests each of its capabilities to
	 * select one of its plans, and this method selects one of them, randomly.
	 * Subclasses may override this method to customize plan selection.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 * @param capabilityPlans
	 *            the set of candidate plans of each capability, as a map.
	 */
	protected Plan selectPlan(Goal goal,
			Map<Capability, Set<Plan>> capabilityPlans) {
		Set<Plan> preselectedPlans = new HashSet<>();
		for (Capability capability : capabilityPlans.keySet()) {
			Plan preselectedPlan = capability.selectPlan(goal,
					capabilityPlans.get(capability));
			if (preselectedPlan != null) {
				preselectedPlans.add(preselectedPlan);
			}
		}

		if (preselectedPlans.isEmpty()) {
			return null;
		} else {
			return preselectedPlans.iterator().next();
		}
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
