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

package bdi4jade.examples.capabilities;

import bdi4jade.belief.Belief;
import bdi4jade.event.GoalEvent;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;

/**
 * @author Ingrid Nunes
 */
public class TestPlanBody extends AbstractPlanBody {

	enum TestStep {
		BELIEF, CHILD_GOAL, COMPLETED, MY_EXTERNAL_GOAL, MY_INTERNAL_GOAL, PARENT_GOAL, PARENT_PROTECTED_GOAL, SIBLING_GOAL, SIBLING_PROTECTED_GOAL;
	}

	private static final long serialVersionUID = -9039447524062487795L;

	@bdi4jade.annotation.Belief
	private Belief<String> bottomBelief;
	@bdi4jade.annotation.Belief
	private Belief<String> middle1Belief;
	@bdi4jade.annotation.Belief
	private Belief<String> middle2Belief;
	private TestStep step;
	@bdi4jade.annotation.Belief
	private Belief<String> topBelief;

	public void action() {
		switch (step) {
		case BELIEF:
			log.info("Testing beliefs...");
			log.info("These should be not null:");
			log.info("topBelief: " + topBelief);
			log.info("middle1Belief: " + middle1Belief);
			log.info("These should be null:");
			log.info("middle2Belief: " + middle2Belief);
			log.info("bottomBelief: " + bottomBelief);

			log.info("Testing plans...");
			dispatchSubgoalAndListen(new Middle1Capability.Middle1ExternalGoal());
			this.step = TestStep.MY_EXTERNAL_GOAL;
			break;
		case MY_EXTERNAL_GOAL:
			GoalEvent goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				dispatchSubgoalAndListen(new Middle1Capability.Middle1InternalGoal());
			}
			this.step = TestStep.MY_INTERNAL_GOAL;
			break;
		case MY_INTERNAL_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				// FIXME dispatchSubgoalAndListen(new TopGoal());
			}

			this.step = TestStep.COMPLETED;
			break;
		case COMPLETED:
			setEndState(EndState.SUCCESSFULL);
			break;
		}
	}

	public void onStart() {
		this.step = TestStep.BELIEF;
	}

	private void printGoal(GoalEvent goalEvent, boolean achievedExpected) {
		if (GoalStatus.ACHIEVED.equals(goalEvent.getStatus())) {
			log.debug("Goal " + goalEvent.getGoal().getClass().getSimpleName()
					+ " completed - " + (achievedExpected ? "" : "un")
					+ "expected result");
		} else {
			log.debug("A goal has failed: "
					+ goalEvent.getGoal().getClass().getSimpleName() + " - "
					+ (achievedExpected ? "un" : "") + "expected result");
		}

	}

}
