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

import bdi4jade.belief.BeliefSet;
import bdi4jade.examples.blocksworld.BlocksWorldCapability;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.PlanBody;
import bdi4jade.util.goal.BeliefSetValueGoal;

/**
 * @author ingrid
 * 
 */
public class ClearPlanBody extends PlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	private int index;
	private On on;
	private BeliefSet<On> onSet;
	private Thing thing;
	private boolean waiting;

	public ClearPlanBody() {
		this.waiting = false;
		this.index = 0;
	}

	@Override
	public void action() {
		if (!waiting) {
			for (; index < Thing.THINGS.length; index++) {
				Thing t = Thing.THINGS[index];
				on = new On(t, thing);
				if (onSet.hasValue(on)) {

					dispatchSubgoalAndListen(new BeliefSetValueGoal<On>(
							BlocksWorldCapability.BELIEF_ON, new On(t,
									Thing.TABLE)));
					waiting = true;
					break;
				}
			}
		} else if (getGoalEvent() != null) {
			onSet.removeValue(on);
			on = null;
			waiting = false;
			index++;
		}

		if (index >= Thing.THINGS.length) {
			setEndState(EndState.SUCCESSFUL);
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onStart() {
		this.onSet = (BeliefSet<On>) getBeliefBase().getBelief(
				BlocksWorldCapability.BELIEF_ON);
		BeliefSetValueGoal<Clear> achieveClear = (BeliefSetValueGoal<Clear>) getGoal();
		this.thing = achieveClear.getValue().getThing();
	}

}
