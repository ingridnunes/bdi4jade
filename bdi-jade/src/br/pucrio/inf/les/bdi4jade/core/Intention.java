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
// http://www.inf.puc-rio.br/~ionunes/
//
//----------------------------------------------------------------------------

package br.pucrio.inf.les.bdi4jade.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdi4jade.event.GoalEvent;
import br.pucrio.inf.les.bdi4jade.event.GoalFinishedEvent;
import br.pucrio.inf.les.bdi4jade.event.GoalListener;
import br.pucrio.inf.les.bdi4jade.exception.PlanInstantiationException;
import br.pucrio.inf.les.bdi4jade.goal.Goal;
import br.pucrio.inf.les.bdi4jade.goal.GoalStatus;
import br.pucrio.inf.les.bdi4jade.plan.Plan;
import br.pucrio.inf.les.bdi4jade.plan.PlanInstance;
import br.pucrio.inf.les.bdi4jade.plan.PlanInstance.EndState;

/**
 * This class represents the intention abstraction from the BDI model. It
 * represents a goal that the agent is committed to achieve. It has the
 * associated goal and tries to execute plans to achieve it. It keeps a list of
 * the executed plans, and after using all plans unsuccessfully, the goal is
 * considered unachievable. When a plan fails, the BDI-interpreter cycle may
 * invoke the {@link #tryToAchive()} method again, so the intention tries
 * another plan. During its execution, the intention can be set to no longer
 * desired.
 * 
 * @author ingrid
 */
public class Intention {

	private PlanInstance currentPlan;
	private final Set<Plan> executedPlans;
	private final Goal goal;
	private final List<GoalListener> goalListeners;
	private final Log log;
	private final BDIAgent myAgent;
	private boolean noLongerDesired;
	private final Capability owner;// XXX Intention - owner (Capability)
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
	 */
	public Intention(BDIAgent bdiAgent, Goal goal) {
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
	 * @param owner
	 *            the Capability that is owner of the goal.
	 */
	public Intention(Goal goal, BDIAgent bdiAgent, Capability owner) {
		this.log = LogFactory.getLog(this.getClass());
		this.goal = goal;
		this.myAgent = bdiAgent;
		this.unachievable = false;
		this.noLongerDesired = false;
		this.waiting = true;
		this.goalListeners = new ArrayList<GoalListener>();
		this.executedPlans = new HashSet<Plan>();
		this.currentPlan = null;
		this.owner = owner;
	}

	/**
	 * Adds a listener to be notified when the given has achieve its end state.
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
		Set<Plan> options = getCanAchievePlans();
		options.removeAll(executedPlans);

		while (this.currentPlan == null && !options.isEmpty()) {
			Plan selectedPlan = myAgent.getPlanSelectionStrategy().selectPlan(
					goal, options);
			try {
				this.currentPlan = new PlanInstance(selectedPlan, this);
			} catch (PlanInstantiationException e) {
				log.error("Plan " + selectedPlan.getId()
						+ " could not be instantiated.");
				e.printStackTrace();
				this.currentPlan = null;
				options.remove(selectedPlan);
			}
		}

		if (options.isEmpty()) {
			this.unachievable = true;
		} else {
			this.currentPlan.startPlan();
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
			this.currentPlan.stopPlan();
			this.currentPlan = null;
			break;
		case PLAN_FAILED:
			this.waiting = true;
			this.executedPlans.add(this.currentPlan.getPlan());
			this.currentPlan = null;
			break;
		}
	}

	/**
	 * Notify all listeners, if any, about a goal event.
	 * 
	 * @param goalEvent
	 */
	private void fireGoalEvent(GoalEvent goalEvent) {
		synchronized (goalListeners) {
			for (GoalListener goalListener : goalListeners) {
				goalListener.goalPerformed(goalEvent);
			}
		}
	}

	/**
	 * Fires a goal event when a goal has achieved its end state.
	 * 
	 * @see GoalStatus
	 */
	public void fireGoalFinishedEvent() {
		GoalStatus status = getStatus();
		log.debug("Goal: " + goal.getClass().getSimpleName() + " (" + status
				+ ") - " + goal);
		this.fireGoalEvent(new GoalFinishedEvent(goal, status));
	}

	/**
	 * Returns all plans from all capabilities that can achieve the goal.
	 * 
	 * @return the set of plans that can achieve the goal.
	 */
	private Set<Plan> getCanAchievePlans() {
		Set<Plan> plans = new HashSet<Plan>();
		for (Capability capability : myAgent.getCapabilities()) {
			plans.addAll(capability.getPlanLibrary().canAchievePlans(goal));
		}
		return plans;
	}

	/**
	 * @return the goal
	 */
	public Goal getGoal() {
		return goal;
	}

	/**
	 * @return the goalListeners
	 */
	public List<GoalListener> getGoalListeners() {
		return goalListeners;
	}

	/**
	 * @return the myAgent
	 */
	public BDIAgent getMyAgent() {
		return myAgent;
	}

	/**
	 * Returns the current goal status that this capability is committed to
	 * achieve.
	 * 
	 * @see GoalStatus
	 * 
	 * @return the current goal status.
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
			break;
		case TRYING_TO_ACHIEVE:
			this.noLongerDesired = true;
			this.currentPlan.stopPlan();
			this.currentPlan = null;
			break;
		case PLAN_FAILED:
			this.noLongerDesired = true;
			this.executedPlans.add(this.currentPlan.getPlan());
			this.currentPlan = null;
			break;
		}
	}

	/**
	 * Removes a goal listener to not be notified about the goal achievement
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
			dispatchPlan();
			break;
		case PLAN_FAILED:
			this.executedPlans.add(this.currentPlan.getPlan());
			this.currentPlan = null;
			dispatchPlan();
			break;
		}
	}

}
