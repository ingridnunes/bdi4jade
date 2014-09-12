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

package bdi4jade.examples.helloworld;

import bdi4jade.annotation.GoalOwner;
import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.Plan;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;

/**
 * @author Ingrid Nunes
 */
public class HelloWorldAnnotatedCapability extends Capability {

	@GoalOwner(capability = HelloWorldAnnotatedCapability.class)
	public static class HelloWorldGoal implements Goal {
		private static final long serialVersionUID = -9039447524062487795L;

		private String name;
		private long time;

		public HelloWorldGoal(String name) {
			this.name = name;
		}

		@Parameter(direction = Direction.IN)
		public String getName() {
			return name;
		}

		public void setTime(long time) {
			this.time = time;
		}

		@Parameter(direction = Direction.OUT)
		public long getTime() {
			return time;
		}

		@Override
		public String toString() {
			return getClass().getSimpleName() + " - name: " + name
					+ " / time: " + time;
		}
	}

	public static class HelloWorldPlanBody extends AbstractPlanBody {
		private static final long serialVersionUID = -9039447524062487795L;

		private String name;
		private long time;

		public void action() {
			System.out.println("Hello, " + name + "!");
			this.time = System.currentTimeMillis();
			setEndState(EndState.SUCCESSFUL);
		}

		@Parameter(direction = Direction.OUT)
		public long getTime() {
			return time;
		}

		@Parameter(direction = Direction.IN)
		public void setName(String name) {
			this.name = name;
		}
	}

	private static final long serialVersionUID = 2712019445290687786L;

	@bdi4jade.annotation.Plan
	private Plan plan = new DefaultPlan(HelloWorldGoal.class,
			HelloWorldPlanBody.class);

}
