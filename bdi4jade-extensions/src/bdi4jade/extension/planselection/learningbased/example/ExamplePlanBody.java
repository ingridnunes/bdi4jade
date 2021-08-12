package bdi4jade.extension.planselection.learningbased.example;

import bdi4jade.extension.planselection.learningbased.LearningBasedPlanBody;
import bdi4jade.plan.Plan.EndState;

public class ExamplePlanBody extends LearningBasedPlanBody {

	private static final long serialVersionUID = 9022081049199181816L;

	@Override
	public void action() {
		System.out.println("Acting...DONE!");
		setEndState(EndState.SUCCESSFUL);
	}

}
