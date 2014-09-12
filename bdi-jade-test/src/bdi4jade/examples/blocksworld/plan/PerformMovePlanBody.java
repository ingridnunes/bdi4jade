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
import bdi4jade.examples.blocksworld.BlocksWorldCapability;
import bdi4jade.examples.blocksworld.domain.Clear;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;

/**
 * @author Ingrid Nunes
 */
public class PerformMovePlanBody extends AbstractPlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	@Belief(name = BlocksWorldCapability.BELIEF_CLEAR)
	private BeliefSet<String, Clear> clearSet;
	@Belief(name = BlocksWorldCapability.BELIEF_ON)
	private BeliefSet<String, On> onSet;
	private Thing thing1;
	private Thing thing2;

	@Override
	public void action() {
		// If thing1 was over something, this something will now be clear
		for (Thing thing : Thing.THINGS) {
			On on = new On(thing1, thing);
			if (onSet.hasValue(on)) {
				onSet.removeValue(on);
				if (!Thing.TABLE.equals(thing)) {
					clearSet.addValue(new Clear(thing));
				}
			}
		}

		if (!thing2.equals(Thing.TABLE)) {
			clearSet.removeValue(new Clear(thing2));
		}
		onSet.addValue(new On(thing1, thing2));

		setEndState(EndState.SUCCESSFUL);
	}

	@Parameter(direction = Direction.IN, mandatory = true)
	public void setThing1(Thing thing1) {
		this.thing1 = thing1;
	}

	@Parameter(direction = Direction.IN, mandatory = true)
	public void setThing2(Thing thing2) {
		this.thing2 = thing2;
	}

}
