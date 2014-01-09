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

package bdi4jade.util;

import bdi4jade.core.BDIAgent;
import bdi4jade.core.Capability;
import bdi4jade.util.goal.ParallelGoal;
import bdi4jade.util.goal.SequentialGoal;
import bdi4jade.util.plan.ParallelGoalPlanBody;
import bdi4jade.util.plan.SequentialGoalPlanBody;
import bdi4jade.util.plan.SimplePlan;

/**
 * This capability is added in all {@link BDIAgent}. It provides default plans.
 * 
 * @author ingrid
 */
public class DefaultCapability extends Capability {

	private static final long serialVersionUID = -2230280269621396198L;

	/**
	 * @see bdi4jade.core.Capability#setup()
	 */
	@Override
	protected void setup() {
		this.getPlanLibrary().addPlan(
				new SimplePlan(SequentialGoal.class, SequentialGoalPlanBody.class));
		this.getPlanLibrary().addPlan(
				new SimplePlan(ParallelGoal.class, ParallelGoalPlanBody.class));
	}

}
