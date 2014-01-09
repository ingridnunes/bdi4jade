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

package bdi4jade.examples.helloworld;

import bdi4jade.core.BDIAgent;
import bdi4jade.goal.Goal;
import bdi4jade.util.plan.SimplePlan;

public class HelloWorldAgent extends BDIAgent {

	private static final long serialVersionUID = 2712019445290687786L;

	protected void init() {
		this.getRootCapability()
				.getPlanLibrary()
				.addPlan(
						new SimplePlan(HelloWorldGoal.class,
								HelloWorldPlan.class));

		addGoal(new HelloWorldGoal("reader"));
	}

}

/**
 * @author ingridn
 * 
 */
class HelloWorldGoal implements Goal {

	private static final long serialVersionUID = -9039447524062487795L;

	private String name;

	public HelloWorldGoal(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
}
