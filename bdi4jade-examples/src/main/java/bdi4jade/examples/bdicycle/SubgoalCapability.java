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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.annotation.GoalOwner;
import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.Plan;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;
import bdi4jade.plan.planbody.DisposablePlanBody;

/**
 * @author Ingrid Nunes
 */
public class SubgoalCapability extends Capability {

	public static class ChildPlan extends AbstractPlanBody implements
			DisposablePlanBody {
		private static final long serialVersionUID = -5432560989511973914L;

		private int counter;

		@Override
		public void action() {
			if (counter == 0) {
				dispatchSubgoal(new Subgoal());
			}
			log.info("ChildPlan executing... counter " + counter);
			counter++;
		}

		@Override
		public void onAbort() {
			log.info("ChildPlan aborted.");
		}

		@Override
		public void onStart() {
			this.counter = 0;
		}
	}

	public static class MyPlan extends AbstractPlanBody {
		private static final long serialVersionUID = -5432560989511973914L;

		private int counter;

		@Override
		public void action() {
			log.info("Plan executing... counter " + counter);
			counter++;

			if (counter >= 5) {
				log.info("Plan " + getPlan().getId()
						+ " completed its execution.");
				setEndState(EndState.SUCCESSFUL);
			}
		}

		@Override
		public void onStart() {
			this.counter = 0;
		}
	}

	@GoalOwner(capability = SubgoalCapability.class, internal = false)
	public static class ParentGoal implements Goal {
		private static final long serialVersionUID = 2702962164032833240L;
	}

	public static class ParentPlan extends AbstractPlanBody {
		private static final long serialVersionUID = -5432560989511973914L;

		private int counter;

		@Override
		public void action() {
			if (counter == 0) {
				dispatchGoal(new TopLevelGoal());
				dispatchSubgoal(new Subgoal());
			}
			log.info("ParentPlan executing... counter " + counter);
			counter++;

			if (counter >= 5) {
				setEndState(EndState.FAILED);
				log.info("Finishing ParentPlan.");
			}
		}

		@Override
		public int onEnd() {
			log.info("ParentPlan ended.");
			return super.onEnd();
		}

		@Override
		public void onStart() {
			this.counter = 0;
		}
	}

	@GoalOwner(capability = SubgoalCapability.class, internal = true)
	public static class Subgoal implements Goal {
		private static final long serialVersionUID = 2330672980228870224L;
	}

	@GoalOwner(capability = SubgoalCapability.class, internal = false)
	public static class TopLevelGoal implements Goal {
		private static final long serialVersionUID = 2702962164032833240L;
	}

	private static final Log log = LogFactory.getLog(SubgoalCapability.class);
	private static final long serialVersionUID = -4388902481688697669L;

	@bdi4jade.annotation.Plan
	private Plan childPlan = new DefaultPlan(Subgoal.class, ChildPlan.class);

	@bdi4jade.annotation.Plan
	private Plan myPlan = new DefaultPlan(TopLevelGoal.class, MyPlan.class);

	@bdi4jade.annotation.Plan
	private Plan parentPlan = new DefaultPlan(ParentGoal.class,
			ParentPlan.class);

}
