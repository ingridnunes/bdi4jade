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

package bdi4jade.examples.nestedcapabilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.event.GoalFinishedEvent;
import bdi4jade.examples.nestedcapabilities.NestedCapabilitiesAgent.Belief;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.AbstractPlanBody;

public class TestPlanBody extends AbstractPlanBody {

	enum TestStep {
		BELIEF, CHILD_GOAL, COMPLETED, MY_GOAL, PARENT_GOAL, PARENT_PROTECTED_GOAL, SIBLING_GOAL, SIBLING_PROTECTED_GOAL;
	}

	private static final long serialVersionUID = -9039447524062487795L;

	private Log log;
	private TestStep step;

	public void action() {
		switch (step) {
		case BELIEF:
			log.info("Testing beliefs...");
			log.info("These should be not null:");
			printBelief(Belief.MY_BELIEF);
			printBelief(Belief.PARENT_BELIEF);
			log.info("These should be null:");
			printBelief(Belief.SIBLING_BELIEF);
			printBelief(Belief.CHILD_BELIEF);

			log.info("Testing plans...");
			dispatchProtectedSubgoalAndListen(new MyGoal());
			this.step = TestStep.MY_GOAL;
			break;
		case MY_GOAL:
			GoalFinishedEvent goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				dispatchProtectedSubgoalAndListen(new ChildGoal());
			}
			this.step = TestStep.CHILD_GOAL;
			break;
		case CHILD_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				dispatchSubgoalAndListen(new ParentGoal());
			}

			this.step = TestStep.PARENT_GOAL;
			break;
		case PARENT_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				dispatchSubgoalAndListen(new SiblingGoal());
			}

			this.step = TestStep.SIBLING_GOAL;
			break;
		case SIBLING_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				dispatchProtectedSubgoalAndListen(new ParentGoal());
			}

			this.step = TestStep.PARENT_PROTECTED_GOAL;
			break;
		case PARENT_PROTECTED_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, false);
				dispatchProtectedSubgoalAndListen(new SiblingGoal());
			}

			this.step = TestStep.SIBLING_PROTECTED_GOAL;
			break;
		case SIBLING_PROTECTED_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, false);
			}

			this.step = TestStep.COMPLETED;
			break;
		case COMPLETED:
			break;
		}

		if (TestStep.COMPLETED.equals(step))
			setEndState(EndState.SUCCESSFUL);
	}

	public void onStart() {
		this.log = LogFactory.getLog(this.getClass());
		this.step = TestStep.BELIEF;
	}

	private void printBelief(Belief belief) {
		log.info(belief + ": " + getBeliefBase().getBelief(belief.name()));

	}

	private void printGoal(GoalFinishedEvent goalEvent, boolean achievedExpected) {
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
