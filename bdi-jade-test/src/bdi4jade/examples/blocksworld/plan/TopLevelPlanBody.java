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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.examples.blocksworld.BlocksWorldCapability;
import bdi4jade.examples.blocksworld.domain.On;
import bdi4jade.examples.blocksworld.goal.AchieveBlocksStacked;
import bdi4jade.goal.Goal;
import bdi4jade.plan.AbstractPlanBody;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.util.goal.BeliefSetValueGoal;

/**
 * @author ingrid
 * 
 */
public class TopLevelPlanBody extends AbstractPlanBody {

	private static final long serialVersionUID = -5919677537834351951L;

	private int counter;
	private Log log;
	private On[] target;

	public TopLevelPlanBody() {
		this.counter = 0;
		this.log = LogFactory.getLog(this.getClass());
	}

	@Override
	public void action() {
		// If a subgoal has been dispatched, wait for its completion
		if (counter != 0) {
			if ((getGoalEvent() == null)) {
				return;
			}
		}
		// Dispatch the next subgoal, if there are subgoals left
		if (counter != target.length) {
			Goal goal = new BeliefSetValueGoal<On>(
					BlocksWorldCapability.BELIEF_ON, target[counter]);
			dispatchSubgoalAndListen(goal);
			log.debug("Goal dispatched: " + goal);
		}
		counter++;

		if (counter > target.length)
			setEndState(EndState.SUCCESSFUL);
	}

	@Override
	public int onEnd() {
		log.info("World Model at end is:");
		log.info(getBeliefBase());
		return super.onEnd();
	}

	@Override
	public void onStart() {
		log.info("World Model at start is:");
		this.target = ((AchieveBlocksStacked) getGoal()).getTarget();
		log.info(getBeliefBase());
	}

}
