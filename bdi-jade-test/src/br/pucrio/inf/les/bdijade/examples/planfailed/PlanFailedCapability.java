/*
 * Created on 28/01/2010 00:09:12 
 */
package br.pucrio.inf.les.bdijade.examples.planfailed;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdijade.core.BeliefBase;
import br.pucrio.inf.les.bdijade.core.Capability;
import br.pucrio.inf.les.bdijade.core.PlanLibrary;
import br.pucrio.inf.les.bdijade.event.GoalEvent;
import br.pucrio.inf.les.bdijade.event.GoalFinishedEvent;
import br.pucrio.inf.les.bdijade.event.GoalListener;
import br.pucrio.inf.les.bdijade.goal.Goal;
import br.pucrio.inf.les.bdijade.plan.Plan;
import br.pucrio.inf.les.bdijade.util.plan.SimplePlan;

/**
 * @author ingrid
 * 
 */
public class PlanFailedCapability extends Capability implements GoalListener {

	class MyGoal implements Goal {
		private static final long serialVersionUID = 3405041038738876061L;

		private int id;

		public MyGoal(int id) {
			this.id = id;
		}

		public String toString() {
			return "Goal: " + id;
		}
	};

	private static final int GOALS = 10;
	private static final Log log = LogFactory
			.getLog(PlanFailedCapability.class);
	private static final long serialVersionUID = -4800805796961540570L;

	private static Set<Plan> getPlans() {
		Set<Plan> plans = new HashSet<Plan>();
		plans.add(new SimplePlan("Plan1", MyGoal.class, MyPlan.class));
		plans.add(new SimplePlan("Plan2", MyGoal.class, MyPlan.class));
		plans.add(new SimplePlan("Plan3", MyGoal.class, MyPlan.class));
		return plans;
	}

	private int counter;

	public PlanFailedCapability() {
		super(new BeliefBase(), new PlanLibrary(getPlans()));
	}

	@Override
	public void goalPerformed(GoalEvent event) {
		if (event instanceof GoalFinishedEvent
				&& event.getGoal() instanceof MyGoal) {
			log.info(event.getGoal() + " Status: "
					+ ((GoalFinishedEvent) event).getStatus());
			counter++;
			if (counter >= GOALS) {
				log
						.info("Goal finished!! Removing capability of this agent...");
				myAgent.removeCapability(this);
			}
		}
	}

	@Override
	protected void setup() {
		this.counter = 0;
		for (int i = 0; i < GOALS; i++) {
			myAgent.addGoal(new MyGoal(i), this);
		}
	}

}
