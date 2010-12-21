/*
 * Created on 13/12/2009 10:13:03 
 */
package br.pucrio.inf.les.bdijade.core;

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

import br.pucrio.inf.les.bdijade.belief.Belief;
import br.pucrio.inf.les.bdijade.event.GoalListener;
import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.goal.GoalStatus;
import br.pucrio.inf.les.bdijade.message.BDIAgentMsgReceiver;
import br.pucrio.inf.les.bdijade.message.BDIAgentMsgReceiver.BDIAgentMatchExpression;
import br.pucrio.inf.les.bdijade.reasoning.BeliefRevisionStrategy;
import br.pucrio.inf.les.bdijade.reasoning.DeliberationFunction;
import br.pucrio.inf.les.bdijade.reasoning.OptionGenerationFunction;
import br.pucrio.inf.les.bdijade.reasoning.PlanSelectionStrategy;
import br.pucrio.inf.les.bdijade.util.DefaultCapability;
import br.pucrio.inf.les.bdijade.util.reasoning.DefaultBeliefRevisionStrategy;
import br.pucrio.inf.les.bdijade.util.reasoning.DefaultDeliberationFunction;
import br.pucrio.inf.les.bdijade.util.reasoning.DefaultOptionGenerationFunction;
import br.pucrio.inf.les.bdijade.util.reasoning.DefaultPlanSelectionStrategy;

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
				while (it.hasNext()) {
					Intention intention = it.next();
					GoalStatus status = intention.getStatus();
					switch (status) {
					case ACHIEVED:
					case NO_LONGER_DESIRED:
					case UNACHIEVABLE:
						intention.fireGoalFinishedEvent();
						it.remove();
						break;
					default:
						goalStatus.put(intention.getGoal(), status);
						break;
					}
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
	private final Set<Capability> capabilities;
	private DeliberationFunction deliberationFunction;
	private final List<Intention> intentions;
	private OptionGenerationFunction optionGenerationFunction;
	private PlanSelectionStrategy planSelectionStrategy;

	/**
	 * Default constructor.
	 */
	public BDIAgent() {
		this.capabilities = new HashSet<Capability>();
		this.intentions = new LinkedList<Intention>();
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
		synchronized (capabilities) {
			capability.setMyAgent(this);
			this.capabilities.add(capability);
		}
	}

	/**
	 * Adds a new goal to this agent to be achieved.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 */
	public void addGoal(Goal goal) {
		this.addGoal(goal, null);
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
		synchronized (intentions) {
			Intention intention = new Intention(this, goal);
			this.intentions.add(intention);
			this.bdiInterpreter.restart();
			if (goalListener != null) {
				intention.addGoalListener(goalListener);
			}
		}
	}

	/**
	 * Drops a given goal of this agent. If the goal is not part of the agent's
	 * current goal, no action is performed.
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
	 * Returns a collection of all beliefs from all capabilities of this agent.
	 * It may have two equivalent beliefs, i.e. beliefs with the same name.
	 * 
	 * @return the collection of all beliefs of this agent.
	 */
	public Collection<Belief<?>> getAllBeliefs() {
		synchronized (capabilities) {
			int size = 0;
			for (Capability capability : capabilities) {
				size += capability.getBeliefBase().size();
			}
			Collection<Belief<?>> beliefs = new ArrayList<Belief<?>>(size);
			for (Capability capability : capabilities) {
				beliefs.addAll(capability.getBeliefBase().getBeliefs());
			}
			return beliefs;
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
	 * @return the beliefRevisionStrategy
	 */
	public BeliefRevisionStrategy getBeliefRevisionStrategy() {
		return beliefRevisionStrategy;
	}

	/**
	 * @return the capabilities
	 */
	public Set<Capability> getCapabilities() {
		synchronized (capabilities) {
			return capabilities;
		}
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
		synchronized (capabilities) {
			boolean removed = this.capabilities.remove(capability);
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
		while (!capabilities.isEmpty()) {
			this.removeCapability(capabilities.iterator().next());
		}
	}

}
