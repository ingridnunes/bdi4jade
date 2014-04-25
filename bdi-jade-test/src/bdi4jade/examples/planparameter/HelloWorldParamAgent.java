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

package bdi4jade.examples.planparameter;

import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.core.BDIAgent;
import bdi4jade.event.GoalEvent;
import bdi4jade.event.GoalFinishedEvent;
import bdi4jade.event.GoalListener;
import bdi4jade.goal.Goal;
import bdi4jade.plan.SimplePlan;

public class HelloWorldParamAgent extends BDIAgent implements GoalListener {

	public class HelloWorldParamGoal implements Goal {

		private static final long serialVersionUID = -9039447524062487795L;

		private String name;
		private long time;

		public HelloWorldParamGoal(String name) {
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

	private static final long serialVersionUID = 2712019445290687786L;

	protected void init() {
		this.getRootCapability()
				.getPlanLibrary()
				.addPlan(
						new SimplePlan(HelloWorldParamGoal.class,
								HelloWorldParamPlan.class));

		addGoal(new HelloWorldParamGoal("reader"), this);
	}

	@Override
	public void goalPerformed(GoalEvent event) {
		if (event instanceof GoalFinishedEvent) {
			System.out.println("Hello World Goal Finished! Time: "
					+ event.getGoal());
		}
	}

}
