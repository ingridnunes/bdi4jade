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
import bdi4jade.event.GoalListener;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.Plan;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.PlanBody;

/**
 * This class represents the intention abstraction from the BDI model. It
 * represents a goal that the agent is committed to achieve. It has the
 * associated goal and tries to execute plans to achieve it. It keeps a list of
 * the executed plans, and after using all plans unsuccessfully, the goal is
 * considered unachievable. When a plan fails, the BDI-interpreter cycle may
 * invoke the {@link #tryToAchive()} method again, so the intention tries
 * another plan. During its execution, the intention can be set to no longer
 * desired. This occurs during the agent reasoning cycle or when a goal is
 * dropped ({@link BDIAgent#dropGoal(Goal)}).
 * 
 * @author Ingrid Nunes
 */
public class Intention {

	private static final Log log = LogFactory.getLog(Intention.class);

	private PlanBody currentPlan;
	private final Capability dispatcher;
	private final Set<Plan> executedPlans;
	private final Goal goal;
	private final List<GoalListener> goalListeners;
	private final AbstractBDIAgent myAgent;
	private boolean noLongerDesired;
	private final Set<Capability> owners;
	private boolean unachievable;
	private boolean waiting;

	/**
	 * Creates a new intention. It is associated with an agent and the goal that
	 * it is committed to achieve.
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 * @param bdiAgent
	 *            the bdiAgent associated with this intention.
	 * 
	 * @throws IllegalAccessException
	 *             if the goal was dispatched by a capability that has no access
	 *             to the goal to be achieved.
	 */
	public Intention(Goal goal, AbstractBDIAgent bdiAgent)
			throws IllegalAccessException {
		this(goal, bdiAgent, null);
	}

	/**
	 * Creates a new intention. It is associated with an agent and the goal that
	 * it is committed to achieve. It also receives a {@link Capability} as
	 * parameter indicating the owner of the goal (dispatched the goal).
	 * 
	 * @param goal
	 *            the goal to be achieved.
	 * @param bdiAgent
	 *            the bdiAgent associated with this intention.
	 * @param dispatcher
	 *            the Capability that dispatched the goal.
	 * 
	 * @throws IllegalAccessException
	 *             if the goal was dispatched by a capability that has no access
	 *             to the goal to be achieved.
	 */
	public Intention(Goal goal, AbstractBDIAgent bdiAgent, Capability dispatcher)
			throws IllegalAccessException {
		this.goal = goal;
		this.myAgent = bdiAgent;
		this.unachievable = false;
		this.noLongerDesired = false;
		this.waiting = true;
		this.executedPlans = new HashSet<>();
		this.currentPlan = null;
		this.goalListeners = new LinkedList<>();
		this.dispatcher = dispatcher;

		Class<? extends Capability> owner = null;
		boolean internal = false;

		if (goal.getClass().isAnnotationPresent(GoalOwner.class)) {
			GoalOwner ownerAnnotation = goal.getClass().getAnnotation(
					GoalOwner.class);
			owner = ownerAnnotation.capability();
			internal = ownerAnnotation.internal();
		} else {
			Class<?> enclosingClass = goal.getClass().getEnclosingClass();
			if (enclosingClass != null
					&& Capability.class.isAssignableFrom(goal.getClass()
							.getEnclosingClass())) {
				owner = (Class<Capability>) enclosingClass;
			}
		}

		if (owner == null) {
			this.owners = new HashSet<>();
		} else {
			if (dispatcher == null) {
				this.owners = myAgent.getGoalOwner(owner, internal);
			} else {
				this.owners = dispatcher.getGoalOwner(owner, internal);
				if (owners.isEmpty()) {
					throw new IllegalAccessException("Capability " + dispatcher
							+ " has no access to goal "
							+ goal.getClass().getName() + " of capability "
							+ owner.getName());
				}
			}
		}
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
	 * Dispatches a new plan to try to achieve the intention goal. It looks for
	 * plans that can achieve the goal that were not already tried and then
	 * starts the plan. If all possible plans were already executed, the
	 * intention is set to unachievable.
	 */
	private synchronized void dispatchPlan() {
		Map<Capability, Set<Plan>> options = new HashMap<>();

		if (owners.isEmpty()) {
			for (Capability capability : myAgent.getCapabilities()) {
				capability.addCandidatePlans(goal, options);
			}
		} else {
			for (Capability capability : owners) {
				capability.addCandidatePlans(goal, options);
			}
		}

		Iterator<Capability> it = options.keySet().iterator();
		while (it.hasNext()) {
			Set<Plan> plans = options.get(it.next());
			plans.removeAll(executedPlans);
			if (plans.isEmpty()) {
				it.remove();
			}
		}

		while (this.currentPlan == null && !options.isEmpty()) {
			Plan selectedPlan = myAgent.getPlanSelectionStrategy().selectPlan(
					goal, options);
			try {
				this.currentPlan = selectedPlan.createPlanBody();
				currentPlan.init(selectedPlan, this);
			} catch (PlanInstantiationException e) {
				log.error("Plan " + selectedPlan.getId()
						+ " could not be instantiated.");
				e.printStackTrace();
				this.currentPlan = null;
				for (Set<Plan> plans : options.values()) {
					plans.remove(selectedPlan);
				}
			}
		}

		if (options.isEmpty()) {
			this.unachievable = true;
		} else {
			this.currentPlan.start();
		}
	}

	/**
	 * Sets this intention to the {@link GoalStatus#WAITING} status. It may come
	 * from the {@link GoalStatus#PLAN_FAILED} or
	 * {@link GoalStatus#TRYING_TO_ACHIEVE} states.
	 */
	public synchronized void doWait() {
		GoalStatus status = getStatus();
		switch (status) {
		case WAITING:
			break;
		case TRYING_TO_ACHIEVE:
			this.waiting = true;
			this.currentPlan.block();
			break;
		case PLAN_FAILED:
			this.waiting = true;
			this.executedPlans.add(this.currentPlan.getPlan());
			this.currentPlan = null;
			break;
		default:
			assert false : status;
			break;
		}
	}

	/**
	 * Returns the capability that dispatched this goal.
	 * 
	 * @return the dispatcher.
	 */
	public Capability getDispatcher() {
		return dispatcher;
	}

	/**
	 * Returns the goal associated with this intention.
	 * 
	 * @return the goal.
	 */
	public Goal getGoal() {
		return goal;
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
	 * Returns the agent associated with this intention.
	 * 
	 * @return the myAgent.
	 */
	public AbstractBDIAgent getMyAgent() {
		return myAgent;
	}

	/**
	 * Returns the set of capabilities that own this goal.
	 * 
	 * @return the owners.
	 */
	public Set<Capability> getOwners() {
		return owners;
	}

	/**
	 * Returns the current goal status that this capability is committed to
	 * achieve.
	 * 
	 * @return the current goal status.
	 * 
	 * @see GoalStatus
	 */
	public synchronized GoalStatus getStatus() {
		if (this.unachievable) {
			return GoalStatus.UNACHIEVABLE;
		} else if (this.noLongerDesired) {
			return GoalStatus.NO_LONGER_DESIRED;
		} else if (this.waiting) {
			return GoalStatus.WAITING;
		} else if (this.currentPlan == null) {
			return GoalStatus.TRYING_TO_ACHIEVE;
		} else {
			EndState endState = this.currentPlan.getEndState();
			if (EndState.FAILED.equals(endState)) {
				return GoalStatus.PLAN_FAILED;
			} else if (EndState.SUCCESSFUL.equals(endState)) {
				return GoalStatus.ACHIEVED;
			} else {
				return GoalStatus.TRYING_TO_ACHIEVE;
			}
		}
	}

	/**
	 * Sets this intention as no longer desired. It stops the current plan
	 * execution. It changes the goal status from {@link GoalStatus#WAITING},
	 * {@link GoalStatus#PLAN_FAILED} or {@link GoalStatus#TRYING_TO_ACHIEVE} to
	 * {@link GoalStatus#NO_LONGER_DESIRED}.
	 */
	public synchronized void noLongerDesire() {
		GoalStatus status = getStatus();
		switch (status) {
		case WAITING:
			this.noLongerDesired = true;
			if (currentPlan != null) {
				this.currentPlan.stop();
				this.currentPlan = null;
			}
			break;
		case TRYING_TO_ACHIEVE:
			this.noLongerDesired = true;
			this.currentPlan.stop();
			this.currentPlan = null;
			break;
		case PLAN_FAILED:
			this.noLongerDesired = true;
			this.executedPlans.add(this.currentPlan.getPlan());
			this.currentPlan = null;
			break;
		default:
			assert false : status;
			break;
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

	/**
	 * Makes this intention starts to try to achieve the goal. It changes the
	 * goal status from {@link GoalStatus#WAITING} or
	 * {@link GoalStatus#PLAN_FAILED} to {@link GoalStatus#TRYING_TO_ACHIEVE}.
	 */
	public synchronized void tryToAchive() {
		GoalStatus status = getStatus();
		switch (status) {
		case TRYING_TO_ACHIEVE:
			break;
		case WAITING:
			this.waiting = false;
			if (currentPlan != null) {
				this.currentPlan.restart();
			} else {
				dispatchPlan();
			}
			break;
		case PLAN_FAILED:
			this.executedPlans.add(this.currentPlan.getPlan());
			this.currentPlan = null;
			dispatchPlan();
			break;
		default:
			assert false : status;
			break;
		}
	}

}
