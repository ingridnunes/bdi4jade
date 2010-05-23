/*
 * Created on 27/01/2010 21:57:07 
 */
package br.pucrio.inf.les.bdijade.plan;

import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import br.pucrio.inf.les.bdijade.core.BeliefBase;
import br.pucrio.inf.les.bdijade.core.Intention;
import br.pucrio.inf.les.bdijade.event.GoalEvent;
import br.pucrio.inf.les.bdijade.event.GoalFinishedEvent;
import br.pucrio.inf.les.bdijade.event.GoalListener;
import br.pucrio.inf.les.bdijade.exception.PlanInstantiationException;
import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * This class represents a plan that has been instantiated to be executed, with
 * an associated plan body (a behavior).
 * 
 * @author ingrid
 */
public class PlanInstance implements GoalListener {

	/**
	 * This enumuration represents the possible end states of a plan execution.
	 * 
	 * @author ingrid
	 */
	public enum EndState {
		FAILED, SUCCESSFUL;
	}

	private final Behaviour behaviour;
	private EndState endState;
	private final List<GoalFinishedEvent> goalEventQueue;
	private final Intention intention;
	private final Plan plan;
	private final List<Goal> subgoals;

	/**
	 * Creates a new plan instance. It is associated with a plan definition (
	 * {@link Plan}) and an {@link Intention}. It creates an instance of the
	 * plan body defined in the plan and throws an exception if an error occurs
	 * in this process.
	 * 
	 * @param plan
	 *            the plan associated this this plan instance.
	 * @param intention
	 *            the intention that this plan instance have to achieve.
	 * @throws PlanBodyInstantiationException
	 *             in an error occurred during the instantiation.
	 */
	public PlanInstance(Plan plan, Intention intention)
			throws PlanInstantiationException {
		this.plan = plan;
		this.intention = intention;
		this.subgoals = new ArrayList<Goal>();
		this.behaviour = plan.createPlanBody();
		if (!(behaviour instanceof PlanBody)) {
			throw new PlanInstantiationException(
					"instantiateBehavior() does not return a behaviour that implements PlanBody");
		}
		PlanBody planBody = (PlanBody) behaviour;
		planBody.init(this);
		this.endState = null;
		this.goalEventQueue = new LinkedList<GoalFinishedEvent>();
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

	/**
	 * Drops all current subgoals dispatched by this plan.
	 */
	private void dropSubgoals() {
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
	 * once). If the plan body has come to a failed state, it sets all of its
	 * subgoals as no longer desired.
	 * 
	 * @return the end state of the plan.
	 */
	public EndState getEndState() {
		if (this.endState == null) {
			this.endState = ((PlanBody) behaviour).getEndState();
			if (this.endState != null) {
				if (behaviour instanceof OutputPlanBody) {
					((OutputPlanBody) behaviour).setGoalOutput(getGoal());
				}
				if (EndState.FAILED.equals(this.endState)) {
					dropSubgoals();
				}
			}
		}
		return endState;
	}

	/**
	 * Returns the goal to be achieved by this plan instance.
	 * 
	 * @return the goal.
	 */
	public Goal getGoal() {
		return this.intention.getGoal();
	}

	/**
	 * Returns a goal event from the queue. If the queue is empty, the behavior
	 * associated with this plan instance is blocked.
	 * 
	 * @return the goal event or null if the queue is empty.
	 */
	public GoalFinishedEvent getGoalEvent() {
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
	public GoalFinishedEvent getGoalEvent(boolean block) {
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
	private GoalFinishedEvent getGoalEvent(boolean block, long ms) {
		synchronized (goalEventQueue) {
			if (!this.goalEventQueue.isEmpty()) {
				return this.goalEventQueue.remove(0);
			} else {
				if (this.behaviour != null && block) {
					if (ms < 0) {
						this.behaviour.block();
					} else {
						this.behaviour.block(ms);
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
	public GoalFinishedEvent getGoalEvent(long ms) {
		return getGoalEvent(true, ms);
	}

	/**
	 * Returns the {@link Plan} that is associated with this plan instance.
	 * 
	 * @return the plan.
	 */
	public Plan getPlan() {
		return plan;
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.event.GoalListener#goalPerformed(br.pucrio.inf.les.bdijade.event.GoalEvent)
	 */
	@Override
	public synchronized void goalPerformed(GoalEvent event) {
		if (event instanceof GoalFinishedEvent) {
			synchronized (goalEventQueue) {
				this.goalEventQueue.add((GoalFinishedEvent) event);
				if (this.behaviour != null)
					this.behaviour.restart();
			}
			synchronized (subgoals) {
				this.subgoals.remove(event.getGoal());
			}
		}
	}

	/**
	 * Starts the plan body, a {@link Behaviour}, associated with this plan.
	 */
	public void startPlan() {
		this.intention.getMyAgent().addBehaviour(this.behaviour);
	}

	/**
	 * Stops the plan body, a {@link Behaviour}, associated with this plan. If
	 * the body implements the {@link DisposablePlanBody}, it invokes the method
	 * to about the plan body, so it can perform finalizations.
	 */
	public void stopPlan() {
		dropSubgoals();
		this.intention.getMyAgent().removeBehaviour(behaviour);
		if (behaviour instanceof DisposablePlanBody) {
			((DisposablePlanBody) behaviour).onAbort();
		}
	}

}
