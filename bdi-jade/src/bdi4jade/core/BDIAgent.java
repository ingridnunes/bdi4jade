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

import bdi4jade.belief.Belief;
import bdi4jade.core.GoalUpdateSet.GoalDescription;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.goal.Softgoal;
import bdi4jade.message.BDIAgentMsgReceiver;
import bdi4jade.plan.Plan;

/**
 * This class is an extension of {@link Agent} that has a current set of goals,
 * which can be selected to become intentions, that is, to tried to be achieved
 * by means of the selection and execution of plans. It also have a set of
 * {@link Capability} - an agent is an aggregation of capabilities. It has a
 * behavior that runs the BDI-interpreter. This agent also have a
 * {@link MsgReceiver} behavior to receive all messages that the agent current
 * plans can process.
 * 
 * @author Ingrid Nunes
 */
public class BDIAgent extends Agent {

	/**
	 * This class is a {@link CyclicBehaviour} that runs during all the
	 * {@link BDIAgent} life in order to provide the reasoning engine.
	 * 
	 * @author Ingrid Nunes
	 */
	class BDIInterpreter extends CyclicBehaviour {

		private static final long serialVersionUID = -6991759791322598475L;

		private BDIInterpreter(BDIAgent bdiAgent) {
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
				GoalUpdateSet agentGoalUpdateSet = processIntentions(agentIntentions);
				Map<Capability, GoalUpdateSet> capabilityGoalUpdateSets = new HashMap<>();
				for (Capability capability : capabilities) {
					GoalUpdateSet capabilityGoalUpdateSet = processIntentions(capability
							.getIntentions());
					capabilityGoalUpdateSets.put(capability,
							capabilityGoalUpdateSet);
				}

				generateGoals(agentGoalUpdateSet, capabilityGoalUpdateSets);

				// Adding generated goals
				for (GoalDescription goal : agentGoalUpdateSet
						.getGeneratedGoals()) {
					Intention intention = addIntention(goal.getDispatcher(),
							goal.getGoal(), null);
					agentGoalUpdateSet.addIntention(intention);
				}
				for (GoalUpdateSet goalUpdateSet : capabilityGoalUpdateSets
						.values()) {
					for (GoalDescription goal : goalUpdateSet
							.getGeneratedGoals()) {
						Intention intention = addIntention(
								goal.getDispatcher(), goal.getGoal(), null);
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
				Set<GoalDescription> selectedGoals = filter(
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
	private final Set<Capability> capabilities;
	protected final List<GoalListener> goalListeners;
	protected final Log log;
	private final Set<Softgoal> softgoals;

	/**
	 * Default constructor.
	 */
	public BDIAgent() {
		this.log = LogFactory.getLog(this.getClass());
		this.bdiInterpreter = new BDIInterpreter(this);
		this.capabilities = new HashSet<>();
		this.allIntentions = new HashMap<>();
		this.aggregatedCapabilities = new HashSet<>();
		this.agentIntentions = new LinkedList<>();
		this.softgoals = new HashSet<>();
		this.goalListeners = new LinkedList<>();
	}

	/**
	 * Default constructor.
	 */
	public BDIAgent(Capability capability) {
		this();
		this.addCapability(capability);
	}

	/**
	 * Default constructor.
	 */
	public BDIAgent(Capability[] capabilities) {
		this();
		for (Capability capability : capabilities) {
			this.addCapability(capability);
		}
	}

	/**
	 * Default constructor.
	 */
	public BDIAgent(Collection<Capability> capabilities) {
		this();
		for (Capability capability : capabilities) {
			this.addCapability(capability);
		}
	}

	/**
	 * Adds a capability to this agent.
	 * 
	 * @param capability
	 *            capability to be added.
	 */
	public void addCapability(Capability capability) {
		synchronized (capabilities) {
			this.capabilities.add(capability);
			this.aggregatedCapabilities.add(capability); // FIXME
			capability.setMyAgent(this);
		}
	}

	/**
	 * Adds a new goal to this agent to be achieved.
	 * 
	 * @param dispatcher
	 *            the Capability that dispatched this goal.
	 * @param goal
	 *            the goal to be achieved.
	 */
	public void addGoal(Capability dispatcher, Goal goal) {
		addIntention(dispatcher, goal, null);
	}

	/**
	 * Adds a new goal to this agent to be achieved and adds a listener to
	 * observe its end. If this goal has a capability that owns it, only plans
	 * of this capability and its children capabilities will be considered to
	 * achieve this goal.
	 * 
	 * @param dispatcher
	 *            the Capability that dispatched this goal.
	 * @param goal
	 *            the goal to be achieved.
	 * @param goalListener
	 *            the listener to be notified.
	 */
	public void addGoal(Capability dispatcher, Goal goal,
			GoalListener goalListener) {
		addIntention(dispatcher, goal, goalListener);
	}

	/**
	 * Adds a new goal to this agent to be achieved.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 */
	public void addGoal(Goal goal) {
		addIntention(null, goal, null);
	}

	/**
	 * Adds a new goal to this agent to be achieved and adds a listener to
	 * observe its end.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 * @param goalListener
	 *            the listener to be notified.
	 */
	public void addGoal(Goal goal, GoalListener goalListener) {
		this.addGoal(null, goal, goalListener);
	}

	/**
	 * Adds a listener to be notified when about goal events.
	 * 
	 * @param goalListener
	 *            the listener to be notified.
	 */
	public void addGoalListener(GoalListener goalListener) {
		synchronized (goalListeners) {
			goalListeners.add(goalListener);
		}
	}

	/**
	 * Adds a new goal to this agent to be achieved and adds a listener to
	 * observe its end. If this goal has a capability that owns it, only plans
	 * of this capability and its children capabilities will be considered to
	 * achieve this goal.
	 * 
	 * @param dispatcher
	 *            the Capability that dispatched this goal.
	 * @param goal
	 *            the goal to be achieved.
	 * @param goalListener
	 *            the listener to be notified.
	 */
	private Intention addIntention(Capability dispatcher, Goal goal,
			GoalListener goalListener) {
		synchronized (allIntentions) {
			Intention intention = new Intention(goal, this, dispatcher);
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
	 * Adds a new softgoal to this agent.
	 * 
	 * @param softgoal
	 *            the softgoal to be pursued.
	 */
	public void addSoftgoal(Softgoal softgoal) {
		synchronized (softgoals) {
			this.softgoals.add(softgoal);
		}
	}


	/**
	 * Drops a given goal of this agent. If the goal is not part of the agent's
	 * current goals, no action is performed.
	 * 
	 * @param goal
	 *            the goal to be dropped.
	 */
	public void dropGoal(Goal goal) {
		synchronized (allIntentions) {
			Intention intention = allIntentions.get(goal);
			if (intention != null) {
				intention.noLongerDesire();
			}
		}
	}

	/**
	 * Drops a given softgoal of this agent. If the softgoal is not part of the
	 * agent's current softgoals, no action is performed.
	 * 
	 * @param softgoal
	 *            the softgoal to be dropped.
	 */

	public void dropSoftoal(Softgoal softgoal) {
		synchronized (softgoals) {
			this.softgoals.remove(softgoal);
		}
	}

	/**
	 * This method is responsible for selecting a set of goals that must be
	 * tried to be achieved (intentions) from the set of goals. Its default
	 * implementation requests each of its capabilities to filter their goals.
	 * Subclasses may override this method to customize this deliberation
	 * function.
	 */
	protected Set<GoalDescription> filter(Set<GoalDescription> agentGoals,
			Map<Capability, Set<GoalDescription>> capabilityGoals) {
		Set<GoalDescription> selectedGoals = new HashSet<>();
		selectedGoals.addAll(agentGoals);
		for (Capability capability : capabilityGoals.keySet()) {
			capability.filter(capabilityGoals.get(capability));
		}
		return selectedGoals;
	}

	/**
	 * Notifies all listeners, if any, about a goal event.
	 * 
	 * @param goalEvent
	 *            the event to notify.
	 */
	private void fireGoalEvent(GoalEvent goalEvent) {
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
	private void fireGoalEvent(Intention intention) {
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
	public Set<Capability> getAggregatedCapabilities() {
		synchronized (capabilities) {
			return aggregatedCapabilities;
		}
	}

	/**
	 * Returns a collection of all beliefs from all capabilities of this agent.
	 * It may have two equivalent beliefs, i.e. beliefs with the same name.
	 * 
	 * @return the collection of all beliefs of this agent.
	 */
	public Collection<Belief<?>> getAllBeliefs() {
		synchronized (capabilities) {
			Collection<Belief<?>> beliefs = new LinkedList<Belief<?>>();
			for (Capability capability : capabilities) {
				beliefs.addAll(capability.getBeliefBase().getBeliefs());
			}
			return beliefs;
		}
	}

	/**
	 * @return the capabilities
	 */
	public Collection<Capability> getAllCapabilities() {
		synchronized (capabilities) {
			return capabilities;
		}
	}

	/**
	 * Gets all goals of this agent. This goals are the ones in the goal set and
	 * the ones that are trying to be achieve in intentions.
	 * 
	 * @return the set of goals.
	 */
	public Set<Goal> getAllGoals() {
		synchronized (allIntentions) {
			return allIntentions.keySet();
		}
	}

	/**
	 * Gets all softgoals of this agent.
	 * 
	 * @return the set of softgoals.
	 */
	public Set<Softgoal> getAllSoftgoals() {
		synchronized (softgoals) {
			return this.softgoals;
		}
	}

	/**
	 * Returns all goal listeners.
	 * 
	 * @return the goalListeners.
	 */
	public List<GoalListener> getGoalListeners() {
		return goalListeners;
	}

	/**
	 * @return the intentions
	 */
	public Set<Intention> getIntentions() {
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
	 * {@link #setup()} method.
	 */
	protected void init() {

	}

	/**
	 * Removes a capability from this agent.
	 * 
	 * @param capability
	 *            capability to be removed.
	 * 
	 * @return true if the capability exists and was removed.
	 */
	public boolean removeCapability(Capability capability) {
		synchronized (capabilities) { // FIXME
			boolean removed = this.capabilities.remove(capability);
			if (removed) {
				capability.setMyAgent(null);
			}
			return removed;
		}
	}

	/**
	 * Removes a goal listener, so it will not be notified about the goal events
	 * anymore.
	 * 
	 * @param goalListener
	 *            the goal listener to be removed.
	 */
	public void removeGoalListener(GoalListener goalListener) {
		synchronized (goalListeners) {
			goalListeners.remove(goalListener);
		}
	}

	void resetAllCapabilities() {
		//TODO
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
	 * select one of its plans. Subclasses may override this method to customize
	 * plan selection.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 * @param capabilityPlans
	 *            the set of candidate plans of each capability.
	 */
	protected Plan selectPlan(Goal goal,
			Map<Capability, Set<Plan>> capabilityPlans) {
		Set<Plan> preselectedPlans = new HashSet<>();
		for (Capability capability : capabilityPlans.keySet()) {
			capability.selectPlan(goal, capabilityPlans.get(capability));
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
	 * {@link BDIInterpreter} behavior as well..
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
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		synchronized (capabilities) {
			Iterator<Capability> iterator = aggregatedCapabilities.iterator();
			while (iterator.hasNext())
				removeCapability(iterator.next());
		}
	}

}
