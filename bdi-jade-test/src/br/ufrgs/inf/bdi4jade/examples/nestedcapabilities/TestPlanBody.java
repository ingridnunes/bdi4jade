package br.ufrgs.inf.bdi4jade.examples.nestedcapabilities;

import jade.core.behaviours.Behaviour;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.ufrgs.inf.bdi4jade.event.GoalFinishedEvent;
import br.ufrgs.inf.bdi4jade.examples.nestedcapabilities.NestedCapabilitiesAgent.Belief;
import br.ufrgs.inf.bdi4jade.goal.GoalStatus;
import br.ufrgs.inf.bdi4jade.plan.PlanBody;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance;
import br.ufrgs.inf.bdi4jade.plan.PlanInstance.EndState;

public class TestPlanBody extends Behaviour implements PlanBody {

	enum TestStep {
		BELIEF, CHILD_GOAL, COMPLETED, MY_GOAL, PARENT_GOAL, PARENT_PROTECTED_GOAL, SIBLING_GOAL, SIBLING_PROTECTED_GOAL;
	}

	private static final long serialVersionUID = -9039447524062487795L;

	private PlanInstance instance;
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
			instance.dispatchProtectedSubgoalAndListen(new MyGoal());
			this.step = TestStep.MY_GOAL;
			break;
		case MY_GOAL:
			GoalFinishedEvent goalEvent = instance.getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				instance.dispatchProtectedSubgoalAndListen(new ChildGoal());
			}
			this.step = TestStep.CHILD_GOAL;
			break;
		case CHILD_GOAL:
			goalEvent = instance.getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				instance.dispatchSubgoalAndListen(new ParentGoal());
			}

			this.step = TestStep.PARENT_GOAL;
			break;
		case PARENT_GOAL:
			goalEvent = instance.getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				instance.dispatchSubgoalAndListen(new SiblingGoal());
			}

			this.step = TestStep.SIBLING_GOAL;
			break;
		case SIBLING_GOAL:
			goalEvent = instance.getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, true);
				instance.dispatchProtectedSubgoalAndListen(new ParentGoal());
			}

			this.step = TestStep.PARENT_PROTECTED_GOAL;
			break;
		case PARENT_PROTECTED_GOAL:
			goalEvent = instance.getGoalEvent();
			if (goalEvent == null) {
				return;
			} else {
				printGoal(goalEvent, false);
				instance.dispatchProtectedSubgoalAndListen(new SiblingGoal());
			}

			this.step = TestStep.SIBLING_PROTECTED_GOAL;
			break;
		case SIBLING_PROTECTED_GOAL:
			goalEvent = instance.getGoalEvent();
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
	}

	@Override
	public boolean done() {
		return getEndState() != null;
	}

	public EndState getEndState() {
		return TestStep.COMPLETED.equals(step) ? EndState.SUCCESSFUL : null;
	}

	public void init(PlanInstance planInstance) {
		this.log = LogFactory.getLog(this.getClass());
		this.instance = planInstance;
		this.step = TestStep.BELIEF;
	}

	private void printBelief(Belief belief) {
		log.info(belief + ": "
				+ instance.getBeliefBase().getBelief(belief.name()));

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
