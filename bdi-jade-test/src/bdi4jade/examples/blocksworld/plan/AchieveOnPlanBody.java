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

import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.event.GoalEvent;
import bdi4jade.examples.blocksworld.BlocksWorldCapability;
import bdi4jade.examples.blocksworld.BlocksWorldCapability.PerformMove;
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
public class AchieveOnPlanBody extends BeliefGoalPlanBody {

	enum Step {
		CLEAR_1, CLEAR_2, DONE, PERFORM_MOVE, WAIT_DONE;
	}

	private static final long serialVersionUID = -5919677537834351951L;

	private Step step;
	private Thing thing1;
	private Thing thing2;

	@Override
	public void execute() {
		switch (step) {
		case CLEAR_1:
			dispatchSubgoalAndListen(new BeliefSetHasValueGoal<String, Clear>(
					BlocksWorldCapability.BELIEF_CLEAR, new Clear(thing1)));
			step = Step.CLEAR_2;
		case CLEAR_2:
			if (isSubgoalAchieved()) {
				dispatchSubgoalAndListen(new BeliefSetHasValueGoal<String, Clear>(
						BlocksWorldCapability.BELIEF_CLEAR, new Clear(thing2)));
				step = Step.PERFORM_MOVE;
			}
			break;
		case PERFORM_MOVE:
			if (isSubgoalAchieved()) {
				dispatchSubgoalAndListen(new PerformMove(thing1, thing2));
				step = Step.WAIT_DONE;
			}
			break;
		case WAIT_DONE:
			if (isSubgoalAchieved()) {
				step = Step.DONE;
			}
			break;
		default:
			break;
		}
	}

	private boolean isSubgoalAchieved() {
		GoalEvent goalEvent = getGoalEvent();
		if (goalEvent == null) {
			return false;
		} else if (!GoalStatus.ACHIEVED.equals(goalEvent.getStatus())) {
			setEndState(EndState.FAILED);
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void init() {
		this.step = Step.CLEAR_1;
	}

	@Parameter(direction = Direction.IN, mandatory = true)
	public void setValue(On on) {
		this.thing1 = on.getThing1();
		this.thing2 = on.getThing2();
	}

}
