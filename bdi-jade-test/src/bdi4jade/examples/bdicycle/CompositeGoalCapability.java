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
import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalTemplateFactory;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;

/**
 * @author Ingrid Nunes
 */
public class CompositeGoalCapability extends Capability {

	@GoalOwner(capability = CompositeGoalCapability.class)
	public static class MyGoal1 implements Goal {
		private static final long serialVersionUID = 3405041038738876061L;

		private String msg;

		public MyGoal1(String msg) {
			this.msg = msg;
		}

		@Parameter(direction = Direction.OUT)
		public String getMsg() {
			return msg;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + ": " + msg;
		}
	}

	@GoalOwner(capability = CompositeGoalCapability.class)
	public static class MyGoal2 implements Goal {
		private static final long serialVersionUID = 3405041038738876061L;

		private String message;

		@Parameter(direction = Direction.INOUT)
		public String getMsg() {
			return message;
		}

		public void setMsg(String msg) {
			this.message = msg;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + ": " + message;
		}
	}

	@GoalOwner(capability = CompositeGoalCapability.class)
	public static class MyGoal3 implements Goal {
		private static final long serialVersionUID = 3405041038738876061L;

		private String msg;

		@Parameter(direction = Direction.IN)
		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + ": " + msg;
		}
	}

	public static class MyPlan extends AbstractPlanBody {
		private static final long serialVersionUID = -220345270457161508L;

		public void action() {
			long random = new Random().nextLong();
			log.info("Random: " + random);
			if (random % 7 != 0)
				setEndState(EndState.SUCCESSFUL);
			else
				setEndState(EndState.FAILED);
			log.info(getGoal() + " Plan#" + getPlan().getId() + " EndState: "
					+ getEndState());
		}
	}

	private static final Log log = LogFactory
			.getLog(CompositeGoalCapability.class);
	private static final long serialVersionUID = -4800805796961540570L;

	@bdi4jade.annotation.Plan
	private DefaultPlan multigoalPlan1;

	@bdi4jade.annotation.Plan
	private DefaultPlan multigoalPlan2;

	public CompositeGoalCapability() {
		this.multigoalPlan1 = new DefaultPlan("multigoalPlan1", MyPlan.class);
		multigoalPlan1.addGoalTemplate(GoalTemplateFactory
				.goalOfType(MyGoal1.class));
		multigoalPlan1.addGoalTemplate(GoalTemplateFactory
				.goalOfType(MyGoal2.class));

		this.multigoalPlan2 = new DefaultPlan("multigoalPlan2", MyPlan.class);
		multigoalPlan2.addGoalTemplate(GoalTemplateFactory
				.goalOfType(MyGoal2.class));
		multigoalPlan2.addGoalTemplate(GoalTemplateFactory
				.goalOfType(MyGoal3.class));
	}

}