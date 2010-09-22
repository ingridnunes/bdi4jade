/*
 * Created on 04/02/2010 19:52:56 
 */
package br.pucrio.inf.les.bdijade.util.plan;

import jade.core.behaviours.Behaviour;

import java.util.ArrayList;
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
import br.pucrio.inf.les.bdijade.util.goal.ParallelGoal;

/**
 * @author ingrid
 * 
 */
public class ParallelGoalPlanBody extends Behaviour implements PlanBody,
		OutputPlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	protected List<Goal> completedGoals;
	protected boolean dispatched;
	protected GoalFinishedEvent failedGoal;
	protected Log log;
	protected ParallelGoal parallelGoal;
	protected PlanInstance planInstance;
	protected Boolean success;

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
			GoalFinishedEvent goalEvent = planInstance.getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				if (GoalStatus.ACHIEVED.equals(goalEvent.getStatus())) {
					this.completedGoals.add(goalEvent.getGoal());
					log.debug("Goal " + goalEvent.getGoal() + " completed!");
					if (completedGoals.size() == parallelGoal.getGoals().size()) {
						this.success = Boolean.TRUE;
						log.debug("All goals completed.");
					}
				} else {
					this.failedGoal = goalEvent;
					this.success = Boolean.FALSE;
					log.debug("A goal has failed: " + goalEvent.getGoal());
				}
			}
		} else {
			for (Goal goal : parallelGoal.getGoals()) {
				planInstance.dispatchSubgoalAndListen(goal);
			}
			this.dispatched = true;
			log.debug("Goals dispatched!");
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
	 * Initializes this plan.
	 * 
	 * @param planInstance
	 *            the plan instance associated with this plan.
	 */
	@Override
	public void init(PlanInstance planInstance) {
		this.planInstance = planInstance;
		this.parallelGoal = (ParallelGoal) planInstance.getGoal();
		this.completedGoals = new ArrayList<Goal>(parallelGoal.getGoals()
				.size());
		this.failedGoal = null;
		this.success = null;
		this.dispatched = false;
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.plan.OutputPlanBody#setGoalOutput(br.pucrio.inf.les.bdijade.goal.Goal)
	 */
	@Override
	public void setGoalOutput(Goal goal) {
		ParallelGoal parGoal = (ParallelGoal) goal;
		parGoal.setCompletedGoals(completedGoals);
		parGoal.setFailedGoal(failedGoal);
	}

}
