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

import jade.core.behaviours.Behaviour;
import bdi4jade.belief.BeliefSet;
import bdi4jade.examples.blocksworld.BlocksWorldCapability;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.examples.blocksworld.goal.PerformMove;
import bdi4jade.plan.PlanBody;
import bdi4jade.plan.PlanInstance;
import bdi4jade.plan.PlanInstance.EndState;

/**
 * @author ingrid
 * 
 */
public class PerformMovePlanBody extends Behaviour implements PlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	private BeliefSet<Clear> clearSet;
	private boolean done;
	private BeliefSet<On> onSet;
	private Thing thing1;
	private Thing thing2;

	public PerformMovePlanBody() {
		this.done = false;
	}

	@Override
	public void action() {
		if (!thing2.equals(Thing.TABLE)) {
			clearSet.removeValue(new Clear(thing2));
		}

		for (Thing thing : Thing.THINGS) {
			On on = new On(thing1, thing);
			if (onSet.hasValue(on)) {
				onSet.removeValue(on);
				if (!Thing.TABLE.equals(thing)) {
					clearSet.addValue(new Clear(thing));
				}
			}
		}

		onSet.addValue(new On(thing1, thing2));
		this.done = true;
	}

	@Override
	public boolean done() {
		return done;
	}

	@Override
	public EndState getEndState() {
		return done ? EndState.SUCCESSFUL : null;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void init(PlanInstance planInstance) {
		this.onSet = (BeliefSet<On>) planInstance.getBeliefBase().getBelief(
				BlocksWorldCapability.BELIEF_ON);
		this.clearSet = (BeliefSet<Clear>) planInstance.getBeliefBase()
				.getBelief(BlocksWorldCapability.BELIEF_CLEAR);
		PerformMove goal = (PerformMove) planInstance.getGoal();
		this.thing1 = goal.getThing1();
		this.thing2 = goal.getThing2();
	}

}
