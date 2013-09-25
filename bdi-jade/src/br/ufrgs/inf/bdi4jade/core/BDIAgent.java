//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes, et al.
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

package br.ufrgs.inf.bdi4jade.core;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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

import br.ufrgs.inf.bdi4jade.belief.Belief;
import br.ufrgs.inf.bdi4jade.event.GoalListener;
import br.ufrgs.inf.bdi4jade.goal.Goal;
import br.ufrgs.inf.bdi4jade.goal.GoalStatus;
import br.ufrgs.inf.bdi4jade.message.BDIAgentMsgReceiver;
import br.ufrgs.inf.bdi4jade.message.BDIAgentMsgReceiver.BDIAgentMatchExpression;
import br.ufrgs.inf.bdi4jade.reasoning.BeliefRevisionStrategy;
import br.ufrgs.inf.bdi4jade.reasoning.DeliberationFunction;
import br.ufrgs.inf.bdi4jade.reasoning.OptionGenerationFunction;
import br.ufrgs.inf.bdi4jade.reasoning.PlanSelectionStrategy;
import br.ufrgs.inf.bdi4jade.softgoal.Softgoal;
import br.ufrgs.inf.bdi4jade.util.DefaultCapability;
import br.ufrgs.inf.bdi4jade.util.reasoning.DefaultBeliefRevisionStrategy;
import br.ufrgs.inf.bdi4jade.util.reasoning.DefaultDeliberationFunction;
import br.ufrgs.inf.bdi4jade.util.reasoning.DefaultOptionGenerationFunction;
import br.ufrgs.inf.bdi4jade.util.reasoning.DefaultPlanSelectionStrategy;

/**
 * This class is an extension of {@link Agent} that has a current set of goals,
 * which are selected to become intentions, i.e. to tried to be achieve by means
 * of the selection and execution of plans. It also have a set of
 * {@link Capability}. It has a behavior that runs the BDI-interpreter. This
 * agent also have a {@link MsgReceiver} behavior to receive all messages that
 * the agent current plans can process.
 * 
 * @author ingrid
 */
public class BDIAgent extends Agent {

	/**
	 * This class is a {@link CyclicBehaviour} that runs during all the
	 * {@link BDIAgent} life in order to provide the reasoning engine.
	 * 
	 * @author ingrid
	 */
	class BDIInterpreter extends CyclicBehaviour {

		private static final long serialVersionUID = -6991759791322598475L;

		private Log log;

		public BDIInterpreter(BDIAgent bdiAgent) {
			super(bdiAgent);
			this.log = LogFactory.getLog(this.getClass());
		}

		/**
		 * This method is a variation of the BDI-interpreter cycle of the BDI
		 * architecture. It first reviews the beliefs of this agent, by menas of
		 * the {@link BeliefRevisionStrategy}.
		 * 
		 * After it removes from the intention set the ones that achieved their
		 * end, i.e. achieved, no longer desired and unachievable, and notifies
		 * goal listeners about this event.
		 * 
		 * Then, it generate a new set of goals, dropping existing ones that
		 * were not selected and also creating new ones. This new set of goals
		 * is defined by the {@link OptionGenerationFunction}.
		 * 
		 * Finally, from the set of selected goals, they are now filtered to
		 * select the current agent intentions. The non-selected goals will be
		 * set to wait ({@link Intention#doWait()}) and the selected ones will
		 * be tried to be achieved ({@link Intention#tryToAchive()}).
		 * 
		 * @see jade.core.behaviours.Behaviour#action()
		 */
		@Override
		public void action() {
			log.trace("Beginning BDI-interpreter cycle.");

			log.trace("Reviewing beliefs.");
			beliefRevisionStrategy.reviewBeliefs(BDIAgent.this);

			synchronized (intentions) {
				Map<Goal, GoalStatus> goalStatus = new HashMap<Goal, GoalStatus>();
				Iterator<Intention> it = intentions.iterator();
				List<Intention> doneIntentions = new LinkedList<>();
				while (it.hasNext()) {
					Intention intention = it.next();
					GoalStatus status = intention.getStatus();
					switch (status) {
					case ACHIEVED:
					case NO_LONGER_DESIRED:
					case UNACHIEVABLE:
						doneIntentions.add(intention);
						it.remove();
						break;
					default:
						goalStatus.put(intention.getGoal(), status);
						break;
					}
				}
				for (Intention intention : doneIntentions) {
					intention.fireGoalFinishedEvent();
				}

				Set<Goal> generatedGoals = optionGenerationFunction
						.generateGoals(goalStatus);
				Set<Goal> newGoals = new HashSet<Goal>(generatedGoals);
				newGoals.removeAll(goalStatus.keySet());
				for (Goal goal : newGoals) {
					addGoal(goal);
				}
				Set<Goal> removedGoals = new HashSet<Goal>(goalStatus.keySet());
				removedGoals.removeAll(generatedGoals);
				for (Goal goal : removedGoals) {
					it = intentions.iterator();
					while (it.hasNext()) {
						Intention intention = it.next();
						if (intention.getGoal().equals(goal)) {
							intention.noLongerDesire();
							intention.fireGoalFinishedEvent();
							it.remove();
						}
					}
				}

				goalStatus = new HashMap<Goal, GoalStatus>();
				for (Intention intention : intentions) {
					goalStatus.put(intention.getGoal(), intention.getStatus());
				}
				Set<Goal> selectedGoals = deliberationFunction
						.filter(goalStatus);
				log.trace("Selected goals to be intentions: "
						+ selectedGoals.size());
				for (Intention intention : intentions) {
					if (selectedGoals.contains(intention.getGoal())) {
						intention.tryToAchive();
					} else {
						intention.doWait();
					}
				}

				if (intentions.isEmpty()) {
					log.trace("No goals or intentions - blocking cycle.");
					this.block();
				}
			}

			log.trace("BDI-interpreter cycle finished.");
		}

		public BDIAgent getMyAgent() {
			return (BDIAgent) this.myAgent;
		}

	}

	private static final long serialVersionUID = -841774495336214256L;
	private final BDIInterpreter bdiInterpreter;
	private BeliefRevisionStrategy beliefRevisionStrategy;
	private DeliberationFunction deliberationFunction;
	private final List<Intention> intentions;
	private OptionGenerationFunction optionGenerationFunction;
	private PlanSelectionStrategy planSelectionStrategy;
	private final Capability rootCapability;
	private final Set<Softgoal> softgoals;

	/**
	 * Default constructor.
	 */
	public BDIAgent() {
		this.rootCapability = new Capability();
		this.intentions = new LinkedList<Intention>();
		this.softgoals = new HashSet<Softgoal>();
		this.bdiInterpreter = new BDIInterpreter(this);
		this.beliefRevisionStrategy = new DefaultBeliefRevisionStrategy();
		this.optionGenerationFunction = new DefaultOptionGenerationFunction();
		this.deliberationFunction = new DefaultDeliberationFunction();
		this.planSelectionStrategy = new DefaultPlanSelectionStrategy();
	}

	/**
	 * Adds a capability to this agent.
	 * 
	 * @param capability
	 *            capability to be added.
	 */
	public void addCapability(Capability capability) {
		synchronized (rootCapability) {
			if (capability.getParent() != null) {
				throw new RuntimeException(
						"Capability already binded to another capability!");
			}

			this.rootCapability.addChild(capability);
			capability.setMyAgent(this);
		}
	}

	/**
	 * Adds a new goal to this agent to be achieved.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 */
	public void addGoal(Goal goal) {
		this.addGoal(null, goal, null);
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
	 * Adds a new goal to this agent to be achieved.
	 * 
	 * @param owner
	 *            the Capability that is owner of the goal.
	 * @param goal
	 *            the goal to be achieved.
	 */
	public void addGoal(Capability owner, Goal goal) {
		this.addGoal(owner, goal, null);
	}

	/**
	 * Adds a new goal to this agent to be achieved and adds a listener to
	 * observe its end. If this goal has a capability that owns it, only plans
	 * of this capability and its children capabilities will be considered to
	 * achieve this goal.
	 * 
	 * @param owner
	 *            the Capability that is owner of the goal.
	 * @param goal
	 *            the goal to be achieved.
	 * @param goalListener
	 *            the listener to be notified.
	 */
	public void addGoal(Capability owner, Goal goal, GoalListener goalListener) {
		synchronized (intentions) {
			Intention intention = new Intention(goal, this, owner);
			this.intentions.add(intention);
			this.bdiInterpreter.restart();
			if (goalListener != null) {
				intention.addGoalListener(goalListener);
			}
		}
	}

	/**
	 * Adds a new softgoal to this agent.
	 * 
	 * @param softgoal
	 *            the softgoal to be pursued.
	 */
	public void addSoftgoal(Softgoal softgoal) {
		this.softgoals.add(softgoal);
	}

	/**
	 * Drops a given goal of this agent. If the goal is not part of the agent's
	 * current goals, no action is performed.
	 * 
	 * @param goal
	 *            the goal to be dropped.
	 */
	public void dropGoal(Goal goal) {
		synchronized (intentions) {
			for (Intention intention : intentions) {
				if (intention.getGoal().equals(goal)) {
					intention.noLongerDesire();
					return;
				}
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
		this.softgoals.remove(softgoal);
	}

	/**
	 * Returns a collection of all beliefs from all capabilities of this agent.
	 * It may have two equivalent beliefs, i.e. beliefs with the same name.
	 * 
	 * @return the collection of all beliefs of this agent.
	 */
	public Collection<Belief<?>> getAllBeliefs() {
		synchronized (rootCapability) {
			Collection<Belief<?>> beliefs = new LinkedList<Belief<?>>();
			getAllBeliefs(beliefs, rootCapability);
			return beliefs;
		}
	}

	private void getAllBeliefs(final Collection<Belief<?>> beliefs,
			Capability capability) {
		beliefs.addAll(capability.getBeliefBase().getBeliefs());
		for (Capability child : capability.getChildren()) {
			getAllBeliefs(beliefs, child);
		}
	}

	/**
	 * @return the capabilities
	 */
	public List<Capability> getAllCapabilities() {
		synchronized (rootCapability) {
			List<Capability> capabilities = new ArrayList<>();
			getAllCapabilities(capabilities, rootCapability);
			return capabilities;
		}
	}

	private void getAllCapabilities(final List<Capability> capabilities,
			Capability capability) {
		capabilities.add(capability);
		Set<Capability> children = capability.getChildren();
		for (Capability child : children) {
			getAllCapabilities(capabilities, child);
		}
	}

	/**
	 * Gets all goals of this agent. This goals are the ones in the goal set and
	 * the ones that are trying to be achieve in intentions.
	 * 
	 * @return the set of goals.
	 */
	public Set<Goal> getAllGoals() {
		synchronized (intentions) {
			Set<Goal> goals = new HashSet<Goal>();
			for (Intention intention : intentions) {
				goals.add(intention.getGoal());
			}
			return goals;
		}
	}

	/**
	 * Gets all softgoals of this agent.
	 * 
	 * @return the set of softgoals.
	 */
	public Set<Softgoal> getAllSoftgoals() {
		return this.softgoals;
	}

	/**
	 * @return the beliefRevisionStrategy
	 */
	public BeliefRevisionStrategy getBeliefRevisionStrategy() {
		return beliefRevisionStrategy;
	}

	/**
	 * @return the deliberationFunction
	 */
	public DeliberationFunction getDeliberationFunction() {
		return deliberationFunction;
	}

	/**
	 * @return the intentions
	 */
	public Set<Intention> getIntentions() {
		synchronized (intentions) {
			Set<Intention> activeIntetions = new HashSet<Intention>();
			for (Intention intention : intentions) {
				if (!GoalStatus.WAITING.equals(intention.getStatus()))
					activeIntetions.add(intention);
			}
			return activeIntetions;
		}
	}

	/**
	 * @return the optionGenerationFunction
	 */
	public OptionGenerationFunction getOptionGenerationFunction() {
		return optionGenerationFunction;
	}

	/**
	 * @return the planSelectionStrategy
	 */
	public PlanSelectionStrategy getPlanSelectionStrategy() {
		return planSelectionStrategy;
	}

	/**
	 * Returns the root capability of this agent.
	 * 
	 * @return the rootCapability
	 */
	public Capability getRootCapability() {
		return rootCapability;
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
		synchronized (rootCapability) {
			boolean removed = this.rootCapability.removeChild(capability);
			if (removed) {
				capability.setMyAgent(null);
			}
			return removed;
		}
	}

	/**
	 * @param beliefRevisionStrategy
	 *            the beliefRevisionStrategy to set
	 */
	public void setBeliefRevisionStrategy(
			BeliefRevisionStrategy beliefRevisionStrategy) {
		if (beliefRevisionStrategy == null) {
			this.beliefRevisionStrategy = new DefaultBeliefRevisionStrategy();
		} else {
			this.beliefRevisionStrategy = beliefRevisionStrategy;
		}
	}

	/**
	 * @param deliberationFunction
	 *            the deliberationFunction to set
	 */
	public void setDeliberationFunction(
			DeliberationFunction deliberationFunction) {
		if (deliberationFunction == null) {
			this.deliberationFunction = new DefaultDeliberationFunction();
		} else {
			this.deliberationFunction = deliberationFunction;
		}
	}

	/**
	 * Sets a goal to be no longer desired.
	 * 
	 * @param goal
	 *            the goal to be no longer desired.
	 */
	public void setNoLongerDesired(Goal goal) {
		synchronized (intentions) {
			for (Intention intention : intentions) {
				if (intention.getGoal().equals(goal)) {
					intention.noLongerDesire();
					return;
				}
			}
		}
	}

	/**
	 * @param optionGenerationFunction
	 *            the optionGenerationFunction to set
	 */
	public void setOptionGenerationFunction(
			OptionGenerationFunction optionGenerationFunction) {
		if (optionGenerationFunction == null) {
			this.optionGenerationFunction = new DefaultOptionGenerationFunction();
		} else {
			this.optionGenerationFunction = optionGenerationFunction;
		}
	}

	/**
	 * @param planSelectionStrategy
	 *            the planSelectionStrategy to set
	 */
	public void setPlanSelectionStrategy(
			PlanSelectionStrategy planSelectionStrategy) {
		if (planSelectionStrategy == null) {
			this.planSelectionStrategy = new DefaultPlanSelectionStrategy();
		} else {
			this.planSelectionStrategy = planSelectionStrategy;
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
		this.addBehaviour(new BDIAgentMsgReceiver(this,
				new BDIAgentMatchExpression()));
		this.addBehaviour(bdiInterpreter);
		this.addCapability(new DefaultCapability());
		init();
	}

	/**
	 * @see jade.core.Agent#takeDown()
	 */
	@Override
	protected void takeDown() {
		synchronized (rootCapability) {
			Set<Capability> capabilities = rootCapability.getChildren();
			for (Capability capability : capabilities) {
				rootCapability.removeChild(capability);
			}
		}

	}

}
