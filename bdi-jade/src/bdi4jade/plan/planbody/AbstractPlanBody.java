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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.belief.BeliefBase;
import bdi4jade.core.Capability;
import bdi4jade.core.Intention;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.exception.ParameterException;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.util.ReflectionUtils;

/**
 * This class provides an almost complete implementation of the {@link PlanBody}
 * interface. It represents a plan that has been instantiated to be executed.
 * 
 * @author Ingrid Nunes
 */
public abstract class AbstractPlanBody extends Behaviour implements PlanBody {

	private static final long serialVersionUID = -6488256636028800227L;
	private static final Log log = LogFactory.getLog(AbstractPlanBody.class);

	private EndState endState;
	private final List<GoalEvent> goalEventQueue;
	private Intention intention;
	private Plan plan;
	private final List<Goal> subgoals;

	/**
	 * Creates a new plan body.
	 */
	public AbstractPlanBody() {
		this.plan = null;
		this.intention = null;
		this.endState = null;
		this.subgoals = new ArrayList<Goal>();
		this.goalEventQueue = new LinkedList<>();
	}

	/**
	 * @see PlanBody#dispatchGoal(Goal)
	 */
	public boolean dispatchGoal(Goal goal) {
		return this.intention.getMyAgent().addGoal(
				this.plan.getPlanLibrary().getCapability(), goal);
	}

	/**
	 * @see PlanBody#dispatchSubgoal(Goal)
	 */
	public boolean dispatchSubgoal(Goal subgoal) {
		boolean goalAdded = this.intention.getMyAgent().addGoal(
				this.plan.getPlanLibrary().getCapability(), subgoal);
		synchronized (subgoals) {
			if (goalAdded)
				this.subgoals.add(subgoal);
		}
		return goalAdded;
	}

	/**
	 * @see PlanBody#dispatchSubgoalAndListen(Goal)
	 */
	public boolean dispatchSubgoalAndListen(Goal subgoal) {
		boolean goalAdded = this.intention.getMyAgent().addGoal(
				this.plan.getPlanLibrary().getCapability(), subgoal, this);
		synchronized (subgoals) {
			if (goalAdded)
				this.subgoals.add(subgoal);
		}
		return goalAdded;
	}

	/**
	 * Indicates to the JADE platform that this behavior/plan body finished its
	 * execution. If {@link #getEndState()} returns null, it returns false, as
	 * the plan body has not reached a final state. It returns true otherwise.
	 * 
	 * @return false if {@link #getEndState()} returns null, true otherwise.
	 * 
	 * @see jade.core.behaviours.Behaviour#done()
	 */
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
				this.intention.getMyAgent().dropGoal(subgoal);
				it.remove();
			}
		}
	}

	/**
	 * @see PlanBody#getBeliefBase()
	 */
	public BeliefBase getBeliefBase() {
		return this.plan.getPlanLibrary().getCapability().getBeliefBase();
	}

	/**
	 * @see PlanBody#getCapability()
	 */
	public Capability getCapability() {
		return this.plan.getPlanLibrary().getCapability();
	}

	/**
	 * Returns the end state of plan. A null value means that the plan is still
	 * executing.
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
	 * Returns a goal event from the queue. If the queue is empty, the plan body
	 * execution is blocked.
	 * 
	 * @return the goal event or null if the queue is empty.
	 */
	public GoalEvent getGoalEvent() {
		return getGoalEvent(true, -1);
	}

	/**
	 * Returns a goal event from the queue. If the queue is empty, the plan body
	 * execution is blocked if the parameter passed to this method is true.
	 * 
	 * @param block
	 *            true if the plan body must be blocked if the queue is empty.
	 * @return the goal event or null if the queue is empty.
	 */
	public GoalEvent getGoalEvent(boolean block) {
		return getGoalEvent(block, -1);
	}

	/**
	 * Returns a goal event from the queue. If the block parameter is true, the
	 * plan body execution is blocked if the queue is empty according to the
	 * specified milliseconds. If the time is lower then zero, the plan body is
	 * going to be blocked until an event happens.
	 * 
	 * @param block
	 *            true if the behavior must be blocked if the queue is empty.
	 * @param ms
	 *            the maximum amount of time that the behavior must be blocked.
	 * @return the goal event or null if the queue is empty.
	 * 
	 * @see Behaviour#block()
	 * @see Behaviour#block(long)
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
	 * Returns a goal event from the queue. If the queue is empty, the plan body
	 * execution is blocked for the specified milliseconds.
	 * 
	 * @param ms
	 *            the maximum amount of time that the behavior must be blocked.
	 * @return the goal event or null if the queue is empty.
	 */
	public GoalEvent getGoalEvent(long ms) {
		return getGoalEvent(true, ms);
	}

	/**
	 * Returns the intention associated with the goal that triggered this plan
	 * pdy execution.
	 * 
	 * @return the intention
	 */
	Intention getIntention() {
		return intention;
	}

	/**
	 * Returns the {@link Plan} that is associated with this plan body.
	 * 
	 * @return the plan.
	 */
	public final Plan getPlan() {
		return plan;
	}

	/**
	 * Receives the notification that a goal event has occurred. If the event
	 * has a finished status, it is added to the event queue, which can be
	 * retrieved by invoking the {@link #getGoalEvent()} method, and restarts
	 * the plan body execution.
	 * 
	 * @see GoalListener#goalPerformed(bdi4jade.event.GoalEvent)
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
	 * @throws PlanInstantiationException
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
			ReflectionUtils.setupBeliefs(this);
		} catch (ParameterException exc) {
			throw new PlanInstantiationException(exc);
		}
	}

	/**
	 * Sets the end state of plan. A null value means that the plan is still
	 * executing.
	 * 
	 * If the plan body has come to an end state, it invokes the method to set
	 * the output parameters of the goal, in case the plan body implements the
	 * {@link OutputPlanBody} interface (this is invoked only once), or sets up
	 * the goal inputs parameters based on the plan body output parameters. If
	 * an error occurs during this setting process, a warn is shown, but no
	 * exception is thrown.
	 * 
	 * If the plan body has come to an end state, it drops all subgoals, in case
	 * they are still trying to be achieved.
	 * 
	 * @param endState
	 *            the endState to set.
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
						log.warn("Could not set all goal outputs: " + exc);
					}
				}
				dropSubgoals();
			}
		}
	}

	/**
	 * Starts the plan body, by adding it as to the agent as a {@link Behaviour}
	 * .
	 */
	public final void start() {
		this.intention.getMyAgent().addBehaviour(this);
	}

	/**
	 * Stops the plan body execution. It drops all plan body subgoals. If the
	 * body implements the {@link DisposablePlanBody}, it invokes the method to
	 * about the plan body, so it can perform finalizations.
	 */
	public final void stop() {
		dropSubgoals();
		this.intention.getMyAgent().removeBehaviour(this);
		if (this instanceof DisposablePlanBody) {
			((DisposablePlanBody) this).onAbort();
		}
	}

}
