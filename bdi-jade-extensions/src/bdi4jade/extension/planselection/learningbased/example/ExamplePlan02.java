package bdi4jade.extension.planselection.learningbased.example;

import bdi4jade.goal.GoalTemplateFactory;
import bdi4jade.plan.DefaultPlan;

public class ExamplePlan02 extends DefaultPlan {

	public ExamplePlan02() {
		super(GoalTemplateFactory.goalOfType(ExampleGoal.class), ExamplePlanBody.class);
	}
}