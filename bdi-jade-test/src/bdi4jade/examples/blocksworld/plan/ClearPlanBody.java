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

package bdi4jade.examples.blocksworld.plan;

import bdi4jade.annotation.Belief;
import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.belief.BeliefSet;
import bdi4jade.event.GoalEvent;
import bdi4jade.examples.blocksworld.BlocksWorldCapability;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.goal.BeliefSetHasValueGoal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.BeliefGoalPlanBody;

/**
 * @author Ingrid Nunes
 */
public class ClearPlanBody extends BeliefGoalPlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	private boolean goalDispatched;
	@Belief
	private BeliefSet<String, On> on;
	private Thing thing;

	@Override
	public void execute() {
		if (!goalDispatched) {
			for (int i = 0; i < Thing.THINGS.length; i++) {
				Thing t = Thing.THINGS[i];
				if (on.hasValue(new On(t, thing))) {
					dispatchSubgoalAndListen(new BeliefSetHasValueGoal<String, On>(
							BlocksWorldCapability.BELIEF_ON, new On(t,
									Thing.TABLE)));
					this.goalDispatched = true;
					break;
				}
			}
		} else {
			GoalEvent goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else if (!GoalStatus.ACHIEVED.equals(goalEvent.getStatus())) {
				setEndState(EndState.FAILED);
				return;
			}
		}
	}

	@Override
	public void init() {
		this.goalDispatched = false;
	}

	@Parameter(direction = Direction.IN, mandatory = true)
	public void setValue(Clear clear) {
		this.thing = clear.getThing();
	}

}
