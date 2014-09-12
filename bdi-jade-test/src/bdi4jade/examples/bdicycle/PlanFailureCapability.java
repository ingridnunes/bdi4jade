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

package bdi4jade.examples.bdicycle;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.annotation.GoalOwner;
import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalTemplateFactory;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.Plan;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;

/**
 * @author Ingrid Nunes
 */
public class PlanFailureCapability extends Capability {

	@GoalOwner(capability = PlanFailureCapability.class)
	public static class MyGoal implements Goal {
		private static final long serialVersionUID = 3405041038738876061L;

		private int id;

		public MyGoal(int id) {
			this.id = id;
		}

		public String toString() {
			return "Goal: " + id;
		}
	}

	public static class MyPlan extends AbstractPlanBody {

		private static final long serialVersionUID = -220345270457161508L;

		private Log log = LogFactory.getLog(this.getClass());

		public void action() {
			long random = new Random().nextLong();
			log.info("Random: " + random);
			if (random % 3 == 0)
				setEndState(EndState.SUCCESSFUL);
			else
				setEndState(EndState.FAILED);
			log.info(getGoal() + " Plan#" + getPlan().getId() + " EndState: "
					+ getEndState());
		}

	};

	private static final long serialVersionUID = -4800805796961540570L;

	@bdi4jade.annotation.Plan
	private Plan plan1 = new DefaultPlan("Plan1",
			GoalTemplateFactory.goalOfType(MyGoal.class), MyPlan.class);

	@bdi4jade.annotation.Plan
	private Plan plan2 = new DefaultPlan("Plan2",
			GoalTemplateFactory.goalOfType(MyGoal.class), MyPlan.class);

	@bdi4jade.annotation.Plan
	private Plan plan3 = new DefaultPlan("Plan3",
			GoalTemplateFactory.goalOfType(MyGoal.class), MyPlan.class);

}
