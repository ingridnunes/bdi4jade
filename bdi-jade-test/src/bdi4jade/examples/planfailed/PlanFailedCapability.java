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

package bdi4jade.examples.planfailed;

import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.core.BeliefBase;
import bdi4jade.core.Capability;
import bdi4jade.core.PlanLibrary;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalFinishedEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.goal.Goal;
import bdi4jade.plan.Plan;
import bdi4jade.plan.SimplePlan;

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
				log.info("Goal finished!! Removing capability of this agent...");
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
