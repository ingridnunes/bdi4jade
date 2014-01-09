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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.util.plan;

import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.event.GoalFinishedEvent;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.OutputPlanBody;
import bdi4jade.plan.PlanBody;
import bdi4jade.plan.PlanInstance;
import bdi4jade.plan.PlanInstance.EndState;
import bdi4jade.util.goal.SequentialGoal;

/**
 * This plan
 * 
 * @author ingrid
 */
public class SequentialGoalPlanBody extends Behaviour implements PlanBody,
		OutputPlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	protected List<Goal> completedGoals;
	protected Goal currentGoal;
	protected GoalFinishedEvent failedGoal;
	protected Iterator<Goal> it;
	protected Log log;
	protected PlanInstance planInstance;
	protected Boolean success;

	/**
	 * Created a new SequentialGoalPlan.
	 */
	public SequentialGoalPlanBody() {
		this.log = LogFactory.getLog(this.getClass());
	}

	/**
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		if (this.currentGoal == null) {
			if (!it.hasNext()) {
				this.success = Boolean.TRUE;
				log.debug("All goals completed.");
			} else {
				this.currentGoal = it.next();
				if (!this.completedGoals.isEmpty()) {
					setNextGoal(this.completedGoals.get(this.completedGoals
							.size() - 1), this.currentGoal);
				}
				planInstance.dispatchSubgoalAndListen(currentGoal);
				log.debug("Dispatching goal: " + currentGoal);
			}
		} else {
			GoalFinishedEvent goalEvent = planInstance.getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				if (GoalStatus.ACHIEVED.equals(goalEvent.getStatus())) {
					this.completedGoals.add(goalEvent.getGoal());
					this.currentGoal = null;
					log.debug("Goal " + goalEvent.getGoal() + " completed!");
				} else {
					this.failedGoal = goalEvent;
					this.success = Boolean.FALSE;
					log.debug("A goal has failed: " + goalEvent.getGoal());
				}
			}
		}
	}

	/**
	 * @see jade.core.behaviours.Behaviour#done()
	 */
	@Override
	public boolean done() {
		return (this.success != null);
	}

	/**
	 * @see bdi4jade.plan.PlanBody#getEndState()
	 */
	@Override
	public EndState getEndState() {
		if (this.success == null) {
			return null;
		} else {
			return this.success ? EndState.SUCCESSFUL : EndState.FAILED;
		}
	}

	/**
	 * Initializes this plan. Starts the goals iterator.
	 * 
	 * @param planInstance
	 *            the plan instance associated with this plan.
	 */
	@Override
	public void init(PlanInstance planInstance) {
		this.planInstance = planInstance;
		SequentialGoal goal = (SequentialGoal) planInstance.getGoal();
		this.it = goal.getGoals().iterator();
		this.success = null;
		this.currentGoal = null;
		this.failedGoal = null;
		this.completedGoals = new ArrayList<Goal>(goal.getGoals().size());
	}

	/**
	 * @see bdi4jade.plan.OutputPlanBody#setGoalOutput(bdi4jade.goal.Goal)
	 */
	@Override
	public void setGoalOutput(Goal goal) {
		SequentialGoal seqGoal = (SequentialGoal) goal;
		seqGoal.setCompletedGoals(completedGoals);
		seqGoal.setFailedGoal(failedGoal);
	}

	/**
	 * Sets the parameters of the next goal to be executed based on the previous
	 * goal execution. This is an empty place holder for subclasses.
	 * 
	 * @param previousGoal
	 *            the previously executed goal.
	 * @param goal
	 *            the goal that is going to be dispatched.
	 */
	protected void setNextGoal(Goal previousGoal, Goal goal) {

	}

}
