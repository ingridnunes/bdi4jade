package bdi4jade.extension.planselection.learningbased;

import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;

public interface LearningAlgorithm {

	public double predictExpectedContribution(Plan plan, Softgoal softgoal);

}