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

import bdi4jade.examples.blocksworld.BlocksWorldCapability;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.examples.blocksworld.goal.PerformMove;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.PlanBody;
import bdi4jade.util.goal.BeliefSetValueGoal;

/**
 * @author ingrid
 * 
 */
public class AchieveOnPlanBody extends PlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	private int counter;
	private Thing thing1;
	private Thing thing2;

	public AchieveOnPlanBody() {
		this.counter = 0;
	}

	@Override
	public void action() {
		switch (counter) {
		case 0:
			if (new BeliefSetValueGoal<On>(BlocksWorldCapability.BELIEF_ON,
					new On(thing1, thing2)).isAchieved(getBeliefBase())) {
				counter = 6;
			} else {
				counter = checkClearAndDispatch(thing1) ? 1 : 2;
			}
			break;
		case 2:
			counter = checkClearAndDispatch(thing2) ? 3 : 4;
			break;
		case 4:
			dispatchSubgoalAndListen(new PerformMove(thing1, thing2));
			counter = 5;
		case 1:
		case 3:
		case 5:
			if (getGoalEvent() != null)
				counter++;
			break;
		}

		if (counter == 6)
			setEndState(EndState.SUCCESSFUL);
	}

	private boolean checkClearAndDispatch(Thing thing) {
		BeliefSetValueGoal<Clear> clearBelief = new BeliefSetValueGoal<Clear>(
				BlocksWorldCapability.BELIEF_CLEAR, new Clear(thing1));
		if (!clearBelief.isAchieved(getBeliefBase())) {
			dispatchSubgoalAndListen(clearBelief);
			return true;
		} else {
			return false;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onStart() {
		BeliefSetValueGoal<On> achieveOn = (BeliefSetValueGoal<On>) getGoal();
		this.thing1 = achieveOn.getValue().getThing1();
		this.thing2 = achieveOn.getValue().getThing2();
	}

}
