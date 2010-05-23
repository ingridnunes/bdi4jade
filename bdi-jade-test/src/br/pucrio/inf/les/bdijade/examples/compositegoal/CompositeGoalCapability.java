/*
 * Created on 04/02/2010 22:19:42 
 */
package br.pucrio.inf.les.bdijade.examples.compositegoal;

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
import br.pucrio.inf.les.bdijade.util.goal.CompositeGoal;
import br.pucrio.inf.les.bdijade.util.goal.ParallelGoal;
import br.pucrio.inf.les.bdijade.util.goal.SequentialGoal;
import br.pucrio.inf.les.bdijade.util.plan.SimplePlan;

/**
 * @author ingrid
 * 
 */
public class CompositeGoalCapability extends Capability implements GoalListener {

	class MyGoal1 implements Goal {
		private static final long serialVersionUID = 3405041038738876061L;
	};

	class MyGoal2 implements Goal {
		private static final long serialVersionUID = 3405041038738876061L;
	};

	class MyGoal3 implements Goal {
		private static final long serialVersionUID = 3405041038738876061L;
	};

	private static final Log log = LogFactory
			.getLog(CompositeGoalCapability.class);
	private static final long serialVersionUID = -4800805796961540570L;

	private static Set<Plan> getPlans() {
		Set<Plan> plans = new HashSet<Plan>();
		SimplePlan plan = new SimplePlan(MyPlan.class);
		plan.addGoal(MyGoal1.class);
		plan.addGoal(MyGoal2.class);
		plan.addGoal(MyGoal3.class);
		plans.add(plan);
		return plans;
	}

	private boolean sequential;

	public CompositeGoalCapability(boolean sequential) {
		super(new BeliefBase(), new PlanLibrary(getPlans()));
		this.sequential = sequential;
	}

	@Override
	public void goalPerformed(GoalEvent event) {
		if (event instanceof GoalFinishedEvent
				&& event.getGoal() instanceof CompositeGoal) {
			log.info(event.getGoal() + " Status: "
					+ ((GoalFinishedEvent) event).getStatus());
			log.info("Goal finished!! Removing capability of this agent...");
			myAgent.removeCapability(this);

		}
	}

	@Override
	protected void setup() {
		Goal[] goals = { new MyGoal1(), new MyGoal2(), new MyGoal3() };
		CompositeGoal compositeGoal = null;
		if (this.sequential) {
			compositeGoal = new SequentialGoal(goals);
		} else {
			compositeGoal = new ParallelGoal(goals);
		}
		this.myAgent.addGoal(compositeGoal, this);
	}

}