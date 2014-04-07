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
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.event.GoalFinishedEvent;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.OutputPlanBody;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.PlanBody;
import bdi4jade.util.goal.ParallelGoal;

/**
 * @author ingrid
 * 
 */
public class ParallelGoalPlanBody extends PlanBody implements OutputPlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	protected List<Goal> completedGoals;
	protected boolean dispatched;
	protected GoalFinishedEvent failedGoal;
	protected Log log;
	protected ParallelGoal parallelGoal;

	/**
	 * Created a new ParallelGoalPlan.
	 */
	public ParallelGoalPlanBody() {
		this.log = LogFactory.getLog(this.getClass());
	}

	/**
	 * @see jade.core.behaviours.Behaviour#action()
	 */
	@Override
	public void action() {
		if (this.dispatched) {
			GoalFinishedEvent goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				if (GoalStatus.ACHIEVED.equals(goalEvent.getStatus())) {
					this.completedGoals.add(goalEvent.getGoal());
					log.debug("Goal " + goalEvent.getGoal() + " completed!");
					if (completedGoals.size() == parallelGoal.getGoals().size()) {
						setEndState(EndState.SUCCESSFUL);
						log.debug("All goals completed.");
					}
				} else {
					this.failedGoal = goalEvent;
					setEndState(EndState.FAILED);
					log.debug("A goal has failed: " + goalEvent.getGoal());
				}
			}
		} else {
			for (Goal goal : parallelGoal.getGoals()) {
				dispatchSubgoalAndListen(goal);
			}
			this.dispatched = true;
			log.debug("Goals dispatched!");
		}
	}

	/**
	 * Initializes this plan.
	 */
	@Override
	public void onStart() {
		this.parallelGoal = (ParallelGoal) getGoal();
		this.completedGoals = new ArrayList<Goal>(parallelGoal.getGoals()
				.size());
		this.failedGoal = null;
		this.dispatched = false;
	}

	/**
	 * @see bdi4jade.plan.OutputPlanBody#setGoalOutput(bdi4jade.goal.Goal)
	 */
	@Override
	public void setGoalOutput(Goal goal) {
		ParallelGoal parGoal = (ParallelGoal) goal;
		parGoal.setCompletedGoals(completedGoals);
		parGoal.setFailedGoal(failedGoal);
	}

}
