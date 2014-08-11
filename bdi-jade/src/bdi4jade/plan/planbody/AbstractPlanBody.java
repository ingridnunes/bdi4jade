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

package bdi4jade.plan.planbody;

import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import bdi4jade.belief.BeliefBase;
import bdi4jade.core.Intention;
import bdi4jade.event.GoalEvent;
import bdi4jade.exception.ParameterException;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.util.ReflectionUtils;

/**
 * This class represents a plan that has been instantiated to be executed.
 * 
 * @author ingrid
 */
public abstract class AbstractPlanBody extends Behaviour implements PlanBody {

	private static final long serialVersionUID = -6488256636028800227L;

	private EndState endState;
	private final List<GoalEvent> goalEventQueue;
	private Intention intention;
	private Plan plan;
	private final List<Goal> subgoals;

	/**
	 * Creates a new plan body instance.
	 */
	public AbstractPlanBody() {
		this.plan = null;
		this.intention = null;
		this.endState = null;
		this.subgoals = new ArrayList<Goal>();
		this.goalEventQueue = new LinkedList<>();
	}

	/**
	 * Dispatches a goal to be achieved.
	 * 
	 * @param goal
	 *            the goal to be dispatched.
	 */
	public void dispatchGoal(Goal goal) {
		this.intention.getMyAgent().addGoal(goal);
	}

	/**
	 * Dispatches a goal to be achieved, using the capability (or its children
	 * capabilities) associated with the plan.
	 * 
	 * @param goal
	 *            the goal to be dispatched.
	 */
	public void dispatchProtectedGoal(Goal goal) {
		this.intention.getMyAgent().addGoal(
				this.plan.getPlanLibrary().getCapability(), goal);
	}

	/**
	 * Dispatches a subgoal to be achieved, using the capability (or its
	 * children capabilities) associated with the plan.
	 * 
	 * @param subgoal
	 *            the subgoal to be dispatched.
	 */
	public void dispatchProtectedSubgoal(Goal subgoal) {
		this.intention.getMyAgent().addGoal(
				this.plan.getPlanLibrary().getCapability(), subgoal);
		synchronized (subgoals) {
			this.subgoals.add(subgoal);
		}
	}

	/**
	 * Dispatches a subgoal to be achieved, using the capability (or its
	 * children capabilities) associated with the plan, and registers itself as
	 * a listener to receive a notification of the end of execution of the goal.
	 * 
	 * @param subgoal
	 *            the subgoal to be dispatched.
	 */
	public void dispatchProtectedSubgoalAndListen(Goal subgoal) {
		this.intention.getMyAgent().addGoal(
				this.plan.getPlanLibrary().getCapability(), subgoal, this);
		synchronized (subgoals) {
			this.subgoals.add(subgoal);
		}
	}

	/**
	 * Dispatches a subgoal to be achieved.
	 * 
	 * @param subgoal
	 *            the subgoal to be dispatched.
	 */
	public void dispatchSubgoal(Goal subgoal) {
		this.intention.getMyAgent().addGoal(subgoal);
		synchronized (subgoals) {
			this.subgoals.add(subgoal);
		}
	}

	/**
	 * Dispatches a subgoal to be achieved and registers itself as a listener to
	 * receive a notification of the end of execution of the goal.
	 * 
	 * @param subgoal
	 *            the subgoal to be dispatched.
	 */
	public void dispatchSubgoalAndListen(Goal subgoal) {
		this.intention.getMyAgent().addGoal(subgoal, this);
		synchronized (subgoals) {
			this.subgoals.add(subgoal);
		}
	}

	@Override
	public final boolean done() {
		synchronized (plan) {
			return getEndState() != null;
		}
	}

	/**
	 * Drops all current subgoals dispatched by this plan.
	 */
	void dropSubgoals() {
		synchronized (subgoals) {
			Iterator<Goal> it = subgoals.iterator();
			while (it.hasNext()) {
				Goal subgoal = it.next();
				this.intention.getMyAgent().setNoLongerDesired(subgoal);
				it.remove();
			}
		}
	}

	/**
	 * Returns the belief base of the capability.
	 * 
	 * @return the belief base containing the beliefs.
	 */
	public BeliefBase getBeliefBase() {
		return this.plan.getPlanLibrary().getCapability().getBeliefBase();
	}

	/**
	 * Returns the end state of plan. A null value means that the plan is still
	 * in execution. If the plan body has come to an end state, it invokes the
	 * method to set the output parameters of the goal, in case the plan body
	 * implements the {@link OutputPlanBody} interface (this is invoked only
	 * once). If the plan body has come to an end state, it sets all of its
	 * subgoals as no longer desired, in case they are still trying to be
	 * achieved.
	 * 
	 * @return the end state of the plan.
	 */
	public EndState getEndState() {
		synchronized (plan) {
			return endState;
		}
	}

	/**
	 * Returns the goal to be achieved by this plan instance.
	 * 
	 * @return the goal.
	 */
	public final Goal getGoal() {
		return this.intention.getGoal();
	}

	/**
	 * Returns a goal event from the queue. If the queue is empty, the behavior
	 * associated with this plan instance is blocked.
	 * 
	 * @return the goal event or null if the queue is empty.
	 */
	public GoalEvent getGoalEvent() {
		return getGoalEvent(true, -1);
	}

	/**
	 * Returns a goal event from the queue. If the queue is empty, the behavior
	 * associated with this plan instance is going to be blocked if the
	 * parameter passed to this method is true.
	 * 
	 * @param block
	 *            true if the behavior must be blocked if the queue is empty.
	 * @return the goal event or null if the queue is empty.
	 */
	public GoalEvent getGoalEvent(boolean block) {
		return getGoalEvent(block, -1);
	}

	/**
	 * Returns a goal event from the queue. If the block parameter is true, the
	 * behavior associated with this plan instance is going to be blocked if the
	 * queue is empty according to the specified milliseconds. specified
	 * milliseconds. If the time is lower then zero, the behavior is going to be
	 * blocked until an event happens ({@link Behaviour#block()}).
	 * 
	 * @param block
	 *            true if the behavior must be blocked if the queue is empty.
	 * @param ms
	 *            the maximum amount of time that the behavior must be blocked.
	 * @return the goal event or null if the queue is empty.
	 */
	private GoalEvent getGoalEvent(boolean block, long ms) {
		synchronized (goalEventQueue) {
			if (!this.goalEventQueue.isEmpty()) {
				return this.goalEventQueue.remove(0);
			} else {
				if (block) {
					if (ms < 0) {
						block();
					} else {
						block(ms);
					}
				}
				return null;
			}
		}
	}

	/**
	 * Returns a goal event from the queue. If the queue is empty, the behavior
	 * associated with this plan instance is going to be blocked for the
	 * specified milliseconds.
	 * 
	 * @param ms
	 *            the maximum amount of time that the behavior must be blocked.
	 * @return the goal event or null if the queue is empty.
	 */
	public GoalEvent getGoalEvent(long ms) {
		return getGoalEvent(true, ms);
	}

	/**
	 * @return the intention
	 */
	Intention getIntention() {
		return intention;
	}

	/**
	 * Returns the {@link Plan} that is associated with this plan instance.
	 * 
	 * @return the plan.
	 */
	public final Plan getPlan() {
		return plan;
	}

	/**
	 * @see bdi4jade.event.GoalListener#goalPerformed(bdi4jade.event.GoalEvent)
	 */
	@Override
	public synchronized void goalPerformed(GoalEvent event) {
		if (event.getStatus().isFinished()) {
			synchronized (goalEventQueue) {
				this.goalEventQueue.add(event);
				restart();
			}
			synchronized (subgoals) {
				this.subgoals.remove(event.getGoal());
			}
		}
	}

	/**
	 * Initializes this plan body. It associates this plan body with a plan
	 * definition ({@link Plan}) and an {@link Intention}. If this plan body has
	 * already been initialized, this method throws a
	 * {@link PlanInstantiationException}. It also sets up the plan input
	 * parameters based on the goal input parameters.
	 * 
	 * @param plan
	 *            the plan associated this this plan body.
	 * @param intention
	 *            the intention that this plan instance have to achieve.
	 * @throws PlanBodyInstantiationException
	 *             if this plan body has already been initialized.
	 */
	public final void init(Plan plan, Intention intention)
			throws PlanInstantiationException {
		if (this.plan != null || this.intention != null) {
			throw new PlanInstantiationException(
					"This plan body has already been initialized.");
		}
		this.plan = plan;
		this.intention = intention;
		try {
			ReflectionUtils.setPlanBodyInput(this, intention.getGoal());
		} catch (ParameterException exc) {
			throw new PlanInstantiationException(exc);
		}
	}

	/**
	 * @param endState
	 *            the endState to set
	 */
	protected final void setEndState(EndState endState) {
		synchronized (plan) {
			this.endState = endState;
			if (this.endState != null) {
				if (this instanceof OutputPlanBody) {
					((OutputPlanBody) this).setGoalOutput(getGoal());
				} else {
					try {
						ReflectionUtils.setPlanBodyOutput(this,
								intention.getGoal());
					} catch (ParameterException exc) {
						// FIXME what to do
					}
				}
				dropSubgoals();
			}
		}
	}

	/**
	 * Starts the plan body, a {@link Behaviour}, associated with this plan.
	 */
	public final void start() {
		this.intention.getMyAgent().addBehaviour(this);
	}

	/**
	 * Stops the plan body, a {@link Behaviour}, associated with this plan. If
	 * the body implements the {@link DisposablePlanBody}, it invokes the method
	 * to about the plan body, so it can perform finalizations.
	 */
	public final void stop() {
		dropSubgoals();
		this.intention.getMyAgent().removeBehaviour(this);
		if (this instanceof DisposablePlanBody) {
			((DisposablePlanBody) this).onAbort();
		}
	}

}
