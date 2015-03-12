package bdi4jade.extension.planselection.learningbased.example;

import bdi4jade.goal.GoalTemplateFactory;
import bdi4jade.plan.DefaultPlan;

public class ExamplePlan extends DefaultPlan {

	public ExamplePlan(String id) {
		super(id, GoalTemplateFactory.goalOfType(ExampleGoal.class), ExamplePlanBody.class);
	}
}
	