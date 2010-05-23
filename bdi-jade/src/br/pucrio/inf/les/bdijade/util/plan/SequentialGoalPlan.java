/*
 * Created on 04/02/2010 15:54:21 
 */
package br.pucrio.inf.les.bdijade.util.plan;

import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdijade.event.GoalFinishedEvent;
import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.goal.GoalStatus;
import br.pucrio.inf.les.bdijade.plan.OutputPlanBody;
import br.pucrio.inf.les.bdijade.plan.PlanBody;
import br.pucrio.inf.les.bdijade.plan.PlanInstance;
import br.pucrio.inf.les.bdijade.plan.PlanInstance.EndState;
import br.pucrio.inf.les.bdijade.util.goal.SequentialGoal;

/**
 * This plan
 * 
 * @author ingrid
 */
public class SequentialGoalPlan extends Behaviour implements PlanBody,
		OutputPlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	private List<Goal> completedGoals;
	private Goal currentGoal;
	private GoalFinishedEvent failedGoal;
	private Iterator<Goal> it;
	private Log log;
	private PlanInstance planInstance;
	private Boolean success;

	/**
	 * Created a new SequentialGoalPlan.
	 */
	public SequentialGoalPlan() {
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
	 * @see br.pucrio.inf.les.bdijade.plan.PlanBody#getEndState()
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
	 * @see br.pucrio.inf.les.bdijade.plan.OutputPlanBody#setGoalOutput(br.pucrio.inf.les.bdijade.goal.Goal)
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
