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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.event.GoalFinishedEvent;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.AbstractPlanBody;
import bdi4jade.plan.OutputPlanBody;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.util.goal.SequentialGoal;

/**
 * This plan
 * 
 * @author ingrid
 */
public class SequentialGoalPlanBody extends AbstractPlanBody implements
		OutputPlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	protected List<Goal> completedGoals;
	protected Goal currentGoal;
	protected GoalFinishedEvent failedGoal;
	protected Iterator<Goal> it;
	protected Log log;

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
				setEndState(EndState.SUCCESSFUL);
				log.debug("All goals completed.");
			} else {
				this.currentGoal = it.next();
				if (!this.completedGoals.isEmpty()) {
					setNextGoal(this.completedGoals.get(this.completedGoals
							.size() - 1), this.currentGoal);
				}
				dispatchSubgoalAndListen(currentGoal);
				log.debug("Dispatching goal: " + currentGoal);
			}
		} else {
			GoalFinishedEvent goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				if (GoalStatus.ACHIEVED.equals(goalEvent.getStatus())) {
					this.completedGoals.add(goalEvent.getGoal());
					this.currentGoal = null;
					log.debug("Goal " + goalEvent.getGoal() + " completed!");
				} else {
					this.failedGoal = goalEvent;
					setEndState(EndState.FAILED);
					log.debug("A goal has failed: " + goalEvent.getGoal());
				}
			}
		}
	}

	/**
	 * Initializes this plan. Starts the goals iterator.
	 */
	@Override
	public void onStart() {
		SequentialGoal goal = (SequentialGoal) getGoal();
		this.it = goal.getGoals().iterator();
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
