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

package bdi4jade.examples.blocksworld.plan;

import bdi4jade.examples.blocksworld.BlocksWorldAgent;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.examples.blocksworld.goal.PerformMove;
import bdi4jade.util.goal.BeliefSetValueGoal;
import bdi4jade.util.plan.BeliefGoalPlanBody;

/**
 * @author ingrid
 * 
 */
public class AchieveOnPlanBody extends BeliefGoalPlanBody {

	enum Step {
		CLEAR_1, CLEAR_2, PERFORM_MOVE, WAIT_CLEAR_1, WAIT_CLEAR_2, WAIT_DONE;
	}

	private static final long serialVersionUID = -5919677537834351951L;

	private Step step;
	private Thing thing1;
	private Thing thing2;

	@Override
	public void execute() {
		switch (step) {
		case CLEAR_1:
			dispatchSubgoalAndListen(new BeliefSetValueGoal<Clear>(
					BlocksWorldAgent.BELIEF_CLEAR, new Clear(thing1)));
			step = Step.WAIT_CLEAR_1;
		case WAIT_CLEAR_1:
			if (getGoalEvent() != null) {
				step = Step.CLEAR_2;
			}
			break;
		case CLEAR_2:
			dispatchSubgoalAndListen(new BeliefSetValueGoal<Clear>(
					BlocksWorldAgent.BELIEF_CLEAR, new Clear(thing2)));
			step = Step.WAIT_CLEAR_2;
		case WAIT_CLEAR_2:
			if (getGoalEvent() != null) {
				step = Step.PERFORM_MOVE;
			}
			break;
		case PERFORM_MOVE:
			dispatchSubgoalAndListen(new PerformMove(thing1, thing2));
			step = Step.WAIT_DONE;
			break;
		case WAIT_DONE:
			getGoalEvent();
			break;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onStart() {
		super.onStart();
		BeliefSetValueGoal<On> achieveOn = (BeliefSetValueGoal<On>) getGoal();
		this.thing1 = achieveOn.getValue().getThing1();
		this.thing2 = achieveOn.getValue().getThing2();
		this.step = Step.CLEAR_1;
	}

}
