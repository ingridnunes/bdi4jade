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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.belief.Belief;
import bdi4jade.event.GoalEvent;
import bdi4jade.goal.Goal;
import bdi4jade.goal.GoalStatus;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;

/**
 * @author Ingrid Nunes
 */
public class TestPlanBody extends AbstractPlanBody {

	enum TestStep {
		BELIEF, BOTTOM_EXTERNAL_GOAL, BOTTOM_INTERNAL_GOAL, COMPLETED, MIDDLE2_EXTERNAL_GOAL, MY_EXTERNAL_GOAL, MY_INTERNAL_GOAL, MY_PARENT_INTERNAL_GOAL, TOP_EXTERNAL_GOAL, TOP_INTERNAL_GOAL, TOP_PARENT_INTERNAL_GOAL, ASSOCIATED_MIDDLE_1_INTERNAL_GOAL, ASSOCIATED_MIDDLE_1_EXTERNAL_GOAL, ASSOCIATED_MIDDLE_1_PARENT_INTERNAL_GOAL, ASSOCIATED_MIDDLE_1_PARENT_EXTERNAL_GOAL, ASSOCIATED_TOP_INTERNAL_GOAL, ASSOCIATED_TOP_EXTERNAL_GOAL;
	}

	private static final Log log = LogFactory.getLog(TestPlanBody.class);
	private static final long serialVersionUID = -9039447524062487795L;

	@bdi4jade.annotation.Belief
	private Belief<String, String> bottomBelief;
	@bdi4jade.annotation.Belief
	private Belief<String, String> middle1Belief;
	@bdi4jade.annotation.Belief
	private Belief<String, String> middle1ParentBelief;
	@bdi4jade.annotation.Belief
	private Belief<String, String> middle2Belief;
	private TestStep step;
	@bdi4jade.annotation.Belief
	private Belief<String, String> topBelief;
	@bdi4jade.annotation.Belief
	private Belief<String, String> topParentBelief;
	@bdi4jade.annotation.Belief
	private Belief<String, String> associatedMiddle1Belief;
	@bdi4jade.annotation.Belief
	private Belief<String, String> associatedMiddle1ParentBelief;
	@bdi4jade.annotation.Belief
	private Belief<String, String> associatedTopBelief;

	public void action() {
		switch (step) {
		case BELIEF:
			log.info("Testing beliefs...");
			log.info("These should be not null:");
			log.info("topParentBelief: " + topParentBelief);
			log.info("topBelief: " + topBelief);
			log.info("middle1ParentBelief: " + middle1ParentBelief);
			log.info("middle1Belief: " + middle1Belief);
			log.info("These should be null:");
			log.info("middle2Belief: " + middle2Belief);
			log.info("bottomBelief: " + bottomBelief);
			log.info("associatedTopBelief: " + associatedTopBelief);
			log.info("associatedMiddle1Belief: " + associatedMiddle1Belief);
			log.info("associatedMiddle1ParentBelief: "
					+ associatedMiddle1ParentBelief);

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
				dispatchSubgoalAndListen(new Middle1ParentCapability.Middle1ParentInternalGoal());
			}
			this.step = TestStep.MY_PARENT_INTERNAL_GOAL;
			break;
		case MY_PARENT_INTERNAL_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				dispatchSubgoalAndListen(new TopCapability.TopExternalGoal());
			}
			this.step = TestStep.TOP_EXTERNAL_GOAL;
			break;
		case TOP_EXTERNAL_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				dispatchSubgoalAndListen(new TopCapability.TopInternalGoal());
			}
			this.step = TestStep.TOP_INTERNAL_GOAL;
			break;
		case TOP_INTERNAL_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				dispatchSubgoalAndListen(new TopParentCapability.TopParentInternalGoal());
			}
			this.step = TestStep.TOP_PARENT_INTERNAL_GOAL;
			break;
		case TOP_PARENT_INTERNAL_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				Goal goal = new Middle2Capability.Middle2ExternalGoal();
				printGoal(goal, dispatchSubgoal(goal), false);
			}
			this.step = TestStep.MIDDLE2_EXTERNAL_GOAL;
			break;
		case MIDDLE2_EXTERNAL_GOAL:
			dispatchSubgoalAndListen(new BottomCapability.BottomExternalGoal());
			this.step = TestStep.BOTTOM_EXTERNAL_GOAL;
			break;
		case BOTTOM_EXTERNAL_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				Goal goal = new BottomCapability.BottomInternalGoal();
				printGoal(goal, dispatchSubgoal(goal), false);
			}
			this.step = TestStep.BOTTOM_INTERNAL_GOAL;
			break;
		case BOTTOM_INTERNAL_GOAL:
			dispatchSubgoalAndListen(new AssociatedMiddle1Capability.AssociatedMiddle1ExternalGoal());
			this.step = TestStep.ASSOCIATED_MIDDLE_1_EXTERNAL_GOAL;
			break;
		case ASSOCIATED_MIDDLE_1_EXTERNAL_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				Goal goal = new AssociatedMiddle1Capability.AssociatedMiddle1InternalGoal();
				printGoal(goal, dispatchSubgoal(goal), false);
			}
			this.step = TestStep.ASSOCIATED_MIDDLE_1_INTERNAL_GOAL;
			break;
		case ASSOCIATED_MIDDLE_1_INTERNAL_GOAL:
			dispatchSubgoalAndListen(new AssociatedMiddle1ParentCapability.AssociatedMiddle1ParentExternalGoal());
			this.step = TestStep.ASSOCIATED_MIDDLE_1_PARENT_EXTERNAL_GOAL;
			break;
		case ASSOCIATED_MIDDLE_1_PARENT_EXTERNAL_GOAL:
			goalEvent = getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				Goal goal = new AssociatedMiddle1ParentCapability.AssociatedMiddle1ParentInternalGoal();
				printGoal(goal, dispatchSubgoal(goal), false);
			}
			this.step = TestStep.ASSOCIATED_MIDDLE_1_PARENT_INTERNAL_GOAL;
			break;
		case ASSOCIATED_MIDDLE_1_PARENT_INTERNAL_GOAL:
			Goal goal = new AssociatedTopCapability.AssociatedTopExternalGoal();
			printGoal(goal, dispatchSubgoal(goal), false);
			this.step = TestStep.ASSOCIATED_TOP_EXTERNAL_GOAL;
			break;
		case ASSOCIATED_TOP_EXTERNAL_GOAL:
			goal = new AssociatedTopCapability.AssociatedTopInternalGoal();
			printGoal(goal, dispatchSubgoal(goal), false);
			this.step = TestStep.ASSOCIATED_TOP_INTERNAL_GOAL;
			break;
		case ASSOCIATED_TOP_INTERNAL_GOAL:
			this.step = TestStep.COMPLETED;
			break;
		case COMPLETED:
			setEndState(EndState.SUCCESSFUL);
			break;
		}
	}

	public void onStart() {
		this.step = TestStep.BELIEF;
	}

	private void printGoal(Goal goal, boolean observed, boolean expected) {
		log.debug("Goal " + goal.getClass().getSimpleName() + " dispatched - "
				+ ((observed == expected) ? "" : "un") + "expected result");
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
