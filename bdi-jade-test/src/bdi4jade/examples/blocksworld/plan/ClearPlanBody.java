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

import bdi4jade.belief.BeliefSet;
import bdi4jade.examples.blocksworld.BlocksWorldAgent;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.goal.BeliefSetValueGoal;
import bdi4jade.plan.planbody.BeliefGoalPlanBody;

/**
 * @author ingrid
 * 
 */
public class ClearPlanBody extends BeliefGoalPlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	private BeliefSet<On> onSet;
	private Thing thing;

	@Override
	public void execute() {
		for (int i = 0; i < Thing.THINGS.length; i++) {
			Thing t = Thing.THINGS[i];
			On on = new On(t, thing);
			if (onSet.hasValue(on)) {
				dispatchSubgoalAndListen(new BeliefSetValueGoal<On>(
						BlocksWorldAgent.BELIEF_ON, new On(t, Thing.TABLE)));
				getGoalEvent();
				break;
			}
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public void onStart() {
		super.onStart();
		this.onSet = (BeliefSet<On>) getBeliefBase().getBelief(
				BlocksWorldAgent.BELIEF_ON);
		BeliefSetValueGoal<Clear> achieveClear = (BeliefSetValueGoal<Clear>) getGoal();
		this.thing = achieveClear.getValue().getThing();
	}

}
