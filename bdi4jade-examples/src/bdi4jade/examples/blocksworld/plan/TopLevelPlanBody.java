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

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.event.GoalEvent;
import bdi4jade.examples.blocksworld.BlocksWorldCapability;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.goal.BeliefSetHasValueGoal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.BeliefGoalPlanBody;

/**
 * @author Ingrid Nunes
 */
public class TopLevelPlanBody extends BeliefGoalPlanBody {

	private static final Log log = LogFactory.getLog(TopLevelPlanBody.class);
	private static final long serialVersionUID = -5919677537834351951L;

	private int counter;
	private On[] target;

	@Override
	public void execute() {
		// If a subgoal has been dispatched, wait for its completion
		if (counter != 0) {
			GoalEvent goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else if (!GoalStatus.ACHIEVED.equals(goalEvent.getStatus())) {
				setEndState(EndState.FAILED);
				return;
			}
		}
		// Dispatch the next subgoal, if there are subgoals left
		if (counter < target.length) {
			dispatchSubgoalAndListen(new BeliefSetHasValueGoal<String, On>(
					BlocksWorldCapability.BELIEF_ON, target[counter]));
		}
		counter++;
	}

	private On getOnOverThing(Set<On> target, Thing thing) {
		for (On on : target) {
			if (on.getThing2().equals(thing))
				return on;
		}
		return null;
	}

	@Override
	public int onEnd() {
		log.info("World Model at end is:");
		log.info(getBeliefBase());
		return super.onEnd();
	}

	@Override
	public void init() {
		this.counter = 0;
		log.info("World Model at start is:");
		log.info(getBeliefBase());
	}

	/**
	 * This method sets the target block configuration to be achieved. It is
	 * given in the form of a set of {@link On} values, and the method organize
	 * these values in the order in which they should be stacked, from table to
	 * top. This organized configuration is set in the target array of this plan
	 * body.
	 * 
	 * @param target
	 *            the target to set.
	 */
	@Parameter(direction = Direction.IN, mandatory = true)
	public void setValue(Set<On> target) {
		this.target = new On[target.size()];
		Thing thing = Thing.TABLE;
		for (int i = 0; i < target.size(); i++) {
			On on = getOnOverThing(target, thing);
			this.target[i] = on;
			thing = on.getThing1();
		}
		log.info(target);
	}

}
