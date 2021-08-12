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

package bdi4jade.examples.ping;

import bdi4jade.annotation.Belief;
import bdi4jade.annotation.GoalOwner;
import bdi4jade.core.Capability;
import bdi4jade.goal.Goal;
import bdi4jade.plan.DefaultPlan;
import bdi4jade.plan.Plan;
import jade.lang.acl.MessageTemplate;

/**
 * @author Ingrid Nunes
 */
public class PingPongCapability extends Capability {

	@GoalOwner(capability = PingPongCapability.class)
	public static class PingGoal implements Goal {
		private static final long serialVersionUID = -7733145369836002329L;
	}

	private static final long serialVersionUID = -4800805796961540570L;

	@Belief
	String neighbour;

	@bdi4jade.annotation.Plan
	private Plan pingPlan = new DefaultPlan(PingGoal.class, PingPlanBody.class);

	@Belief
	Integer pingTimes;

	@bdi4jade.annotation.Plan
	private Plan pongPlan = new DefaultPlan(MessageTemplate.MatchContent(PingPlanBody.MSG_CONTENT), PongPlanBody.class);

	public PingPongCapability(String neighbour, int pingTimes) {
		this.neighbour = neighbour;
		this.pingTimes = pingTimes;
	}

}
