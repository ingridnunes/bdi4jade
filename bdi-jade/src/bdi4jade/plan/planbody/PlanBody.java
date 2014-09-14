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
import bdi4jade.core.Capability;
import bdi4jade.core.Intention;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.exception.PlanInstantiationException;
import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;
import bdi4jade.plan.Plan.EndState;

/**
 * This interface defines a plan body, which specifies a set of steps to achieve
 * a goal. It is part of a {@link Plan}, which in turn specifies different
 * properties such as the goals that a plan can achieve or messages it can
 * process. When a plan should be executed, a plan body is instantiated and run.
 * 
 * A plan body is a {@link GoalListener} as it may dispatch goals during its
 * execution and be notified when it is achieved (or learn it is not possible to
 * achieve it).
 * 
 * @author Ingrid Nunes
 */
public interface PlanBody extends GoalListener {

	/**
	 * Blocks this behaviour. It should be noticed that this method is NOT a
	 * blocking call: when it is invoked, the internal behaviour state is set to
	 * Blocked so that, as soon as the action() method returns, the behaviour is
	 * put into a blocked behaviours queue so that it will not be scheduled
	 * anymore. The behaviour is moved back in the pool of active behaviours
	 * when either a message is received or the behaviour is explicitly
	 * restarted by means of its restart() method. If this behaviour is a child
	 * of a CompositeBehaviour a suitable event is fired to notify its parent
	 * behaviour up to the behaviour composition hierarchy root.
	 * 
	 * @see Behaviour#block()
	 */
	public void block();

	/**
	 * Dispatches a goal to be achieved. It is added as a top level agent goal,
	 * that is, the dispatched goal is independent of the goal that this plan
	 * body is trying to achieve.
	 * 
	 * @param goal
	 *            the goal to be dispatched.
	 * 
	 * @return true if the goal could be dispatched, false otherwise.
	 */
	public boolean dispatchGoal(Goal goal);

	/**
	 * Dispatches a subgoal to be achieved. Dispatched subgoals are goals of an
	 * agent as long as the plan body that dispatched it is being executed.
	 * 
	 * @param subgoal
	 *            the subgoal to be dispatched.
	 * 
	 * @return true if the goal could be dispatched, false otherwise.
	 */
	public boolean dispatchSubgoal(Goal subgoal);

	/**
	 * Dispatches a subgoal to be achieved and registers itself as a listener to
	 * receive a notification of the end of execution of the goal.
	 * 
	 * @param subgoal
	 *            the subgoal to be dispatched.
	 * 
	 * @return true if the goal could be dispatched, false otherwise.
	 */
	public boolean dispatchSubgoalAndListen(Goal subgoal);

	/**
	 * Returns the belief base of the capability associated with the plan of
	 * this plan body.
	 * 
	 * @return the belief base containing the capability beliefs.
	 */
	public BeliefBase getBeliefBase();

	/**
	 * Returns the capability associated with the plan of this plan body.
	 * 
	 * @return the capability.
	 */
	public Capability getCapability();

	/**
	 * Returns the end state of the execution of this plan.
	 * 
	 * @return the end state of this plan, or null if it has not finished yet.
	 */
	public EndState getEndState();

	/**
	 * Returns the goal to be achieved by this plan body.
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
	 * @throws PlanInstantiationException
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
	 * @return an integer code representing the termination value of the
	 *         behaviour.
	 * 
	 * @see Behaviour#onEnd()
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
	 * Restarts a blocked behaviour. This method fires a suitable event to
	 * notify this behaviour's parent. When the agent scheduler inserts a
	 * blocked event back into the agent ready queue, it restarts it
	 * automatically. When this method is called, any timer associated with this
	 * behaviour object is cleared.
	 * 
	 * @see Behaviour#restart()
	 */
	public void restart();

	/**
	 * Starts the execution of a plan body, a {@link Behaviour}, associated with
	 * this plan.
	 */
	public void start();

	/**
	 * Stops the plan body, a {@link Behaviour}, associated with this plan. If
	 * the body implements the {@link DisposablePlanBody}, it invokes the method
	 * to about the plan body, so it can perform finalizations.
	 */
	public void stop();

}
