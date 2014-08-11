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
import bdi4jade.belief.BeliefBase;
import bdi4jade.core.Intention;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;
import bdi4jade.plan.Plan.EndState;

/**
 * This interface defines a PlanBody. Plans are executed as behaviors (
 * {@link Behaviour}), but executed in the BDI context, these behaviors should
 * also implement this interface.
 * 
 * @author ingrid
 */
public interface PlanBody extends GoalListener {

	/**
	 * Dispatches a goal to be achieved.
	 * 
	 * @param goal
	 *            the goal to be dispatched.
	 */
	public void dispatchGoal(Goal goal);

	/**
	 * Dispatches a goal to be achieved, using the capability (or its children
	 * capabilities) associated with the plan.
	 * 
	 * @param goal
	 *            the goal to be dispatched.
	 */
	public void dispatchProtectedGoal(Goal goal);

	/**
	 * Dispatches a subgoal to be achieved, using the capability (or its
	 * children capabilities) associated with the plan.
	 * 
	 * @param subgoal
	 *            the subgoal to be dispatched.
	 */
	public void dispatchProtectedSubgoal(Goal subgoal);

	/**
	 * Dispatches a subgoal to be achieved, using the capability (or its
	 * children capabilities) associated with the plan, and registers itself as
	 * a listener to receive a notification of the end of execution of the goal.
	 * 
	 * @param subgoal
	 *            the subgoal to be dispatched.
	 */
	public void dispatchProtectedSubgoalAndListen(Goal subgoal);

	/**
	 * Dispatches a subgoal to be achieved.
	 * 
	 * @param subgoal
	 *            the subgoal to be dispatched.
	 */
	public void dispatchSubgoal(Goal subgoal);

	/**
	 * Dispatches a subgoal to be achieved and registers itself as a listener to
	 * receive a notification of the end of execution of the goal.
	 * 
	 * @param subgoal
	 *            the subgoal to be dispatched.
	 */
	public void dispatchSubgoalAndListen(Goal subgoal);

	/**
	 * Returns the belief base of the capability.
	 * 
	 * @return the belief base containing the beliefs.
	 */
	public BeliefBase getBeliefBase();

	/**
	 * Returns the end state of the execution of this plan.
	 * 
	 * @return the end state of this plan, or null if it has not finished yet.
	 */
	public EndState getEndState();

	/**
	 * Returns the goal to be achieved by this plan instance.
	 * 
	 * @return the goal.
	 */
	public Goal getGoal();

	/**
	 * Returns a goal event from the queue. If the queue is empty, the behavior
	 * associated with this plan instance is blocked.
	 * 
	 * @return the goal event or null if the queue is empty.
	 */
	public GoalEvent getGoalEvent();

	/**
	 * Returns a goal event from the queue. If the queue is empty, the behavior
	 * associated with this plan instance is going to be blocked if the
	 * parameter passed to this method is true.
	 * 
	 * @param block
	 *            true if the behavior must be blocked if the queue is empty.
	 * @return the goal event or null if the queue is empty.
	 */
	public GoalEvent getGoalEvent(boolean block);

	/**
	 * Returns a goal event from the queue. If the queue is empty, the behavior
	 * associated with this plan instance is going to be blocked for the
	 * specified milliseconds.
	 * 
	 * @param ms
	 *            the maximum amount of time that the behavior must be blocked.
	 * @return the goal event or null if the queue is empty.
	 */
	public GoalEvent getGoalEvent(long ms);

	/**
	 * Returns the {@link Plan} that is associated with this plan instance.
	 * 
	 * @return the plan.
	 */
	public Plan getPlan();

	/**
	 * Initializes this plan body. It associates this plan body with a plan
	 * definition ({@link Plan}) and an {@link Intention}. If this plan body has
	 * already been initialized, this method throws a
	 * {@link PlanInstantiationException}.
	 * 
	 * @param plan
	 *            the plan associated this this plan body.
	 * @param intention
	 *            the intention that this plan instance have to achieve.
	 * @throws PlanBodyInstantiationException
	 *             if this plan body has already been initialized.
	 */
	public void init(Plan plan, Intention intention)
			throws PlanInstantiationException;

	/**
	 * This method is invoked just once after this behaviour has ended.
	 * Therefore, it acts as an epilog for the task represented by this plan
	 * body. Note that onEnd is called after the plan body has already stopped
	 * its execution.
	 * 
	 * @see Behaviour#onEnd()
	 * 
	 * @return an integer code representing the termination value of the
	 *         behaviour.
	 */
	public int onEnd();

	/**
	 * This method is executed just once before starting this plan body
	 * execution. Therefore, it acts as a prolog to the task represented by this
	 * plan body.
	 * 
	 * @see Behaviour#onStart()
	 */
	public void onStart();

	/**
	 * Restores plan body initial state. This method must be implemented by
	 * concrete subclasses in such a way that calling reset() on a plan body
	 * object is equivalent to destroying it and recreating it back.
	 * 
	 * @see Behaviour#reset()
	 */
	public void reset();

	/**
	 * Restarts a blocked plan body.
	 * 
	 * @see Behaviour#restart()
	 */
	public void restart();

	/**
	 * Starts the plan body, a {@link Behaviour}, associated with this plan.
	 */
	public void start();

	/**
	 * Stops the plan body, a {@link Behaviour}, associated with this plan. If
	 * the body implements the {@link DisposablePlanBody}, it invokes the method
	 * to about the plan body, so it can perform finalizations.
	 */
	public void stop();

}
