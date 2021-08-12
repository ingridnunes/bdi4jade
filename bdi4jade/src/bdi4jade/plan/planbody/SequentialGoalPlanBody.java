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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.annotation.Parameter;
import bdi4jade.event.GoalEvent;
import bdi4jade.exception.ParameterException;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.goal.SequentialGoal;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.util.ReflectionUtils;

/**
 * This plan body provides the set of actions to achieve a
 * {@link SequentialGoal}.
 * 
 * @author Ingrid Nunes
 */
public class SequentialGoalPlanBody extends AbstractPlanBody implements
		OutputPlanBody {

	private static final Log log = LogFactory
			.getLog(SequentialGoalPlanBody.class);
	private static final long serialVersionUID = -5919677537834351951L;

	protected List<Goal> completedGoals;
	protected Goal currentGoal;
	protected GoalEvent failedGoal;
	protected Iterator<Goal> it;

	/**
	 * This method tries to achieve all subgoals of the {@link SequentialGoal}
	 * to be achieved sequentially. If one of the subgoals fail, it stops the
	 * plan body execution.
	 * 
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
					try {
						setNextGoal(this.completedGoals.get(this.completedGoals
								.size() - 1), this.currentGoal);
					} catch (ParameterException gpe) {
						log.error(gpe);
						gpe.printStackTrace();
						setEndState(EndState.FAILED);
						return;
					}
				}
				dispatchSubgoalAndListen(currentGoal);
				log.debug("Dispatching goal: " + currentGoal);
			}
		} else {
			GoalEvent goalEvent = getGoalEvent();
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
	 * Initializes this plan. Starts the goals iterator and verifies if the goal
	 * that triggered this plan body execution is a {@link SequentialGoal}. If
	 * not, it throws an {@link IllegalArgumentException}.
	 */
	@Override
	public void onStart() {
		if (!(getGoal() instanceof SequentialGoal))
			throw new IllegalArgumentException("SequentialGoal expected.");

		SequentialGoal goal = (SequentialGoal) getGoal();
		this.it = goal.getGoals().iterator();
		this.currentGoal = null;
		this.failedGoal = null;
		this.completedGoals = new ArrayList<Goal>(goal.getGoals().size());
	}

	/**
	 * Sets completed goals, and the failed goal, if there is one.
	 * 
	 * @see bdi4jade.plan.planbody.OutputPlanBody#setGoalOutput(bdi4jade.goal.Goal)
	 */
	@Override
	public void setGoalOutput(Goal goal) {
		SequentialGoal seqGoal = (SequentialGoal) goal;
		seqGoal.setCompletedGoals(completedGoals);
		seqGoal.setFailedGoal(failedGoal);
	}

	/**
	 * Sets the parameters of the next goal to be executed based on the previous
	 * goal execution. It should be overridden by subclass of goals are not
	 * annotated with the {@link Parameter} annotation.
	 * 
	 * @param previousGoal
	 *            the previously executed goal.
	 * @param goal
	 *            the goal that is going to be dispatched.
	 * 
	 * @throws ParameterException
	 *             if an error occurred during setting up the next goal.
	 */
	protected void setNextGoal(Goal previousGoal, Goal goal)
			throws ParameterException {
		ReflectionUtils.setupParameters(previousGoal, goal);
	}

}
