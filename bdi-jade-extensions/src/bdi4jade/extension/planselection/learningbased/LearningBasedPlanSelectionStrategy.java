package bdi4jade.extension.planselection.learningbased;

import java.util.Set;

import bdi4jade.extension.planselection.utilitybased.SoftgoalPreferences;
import bdi4jade.goal.Goal;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;
import bdi4jade.reasoning.AbstractReasoningStrategy;
import bdi4jade.reasoning.PlanSelectionStrategy;

/**
 * A learning-based implementation of the {@link} PlanSelectionStrategy. It
 * selects the plan that has the best expected contribution based on a predicted
 * outcome value and agent preferences.
 * 
 * @author João Faccin
 */
public class LearningBasedPlanSelectionStrategy extends
		AbstractReasoningStrategy implements PlanSelectionStrategy {

	private LearningAlgorithm learningAlgorithm;

	/**
	 * Default constructor that initializes the {@link} LearningAlgorithm used
	 * in the plan selection process.
	 */
	public LearningBasedPlanSelectionStrategy() {
		this.learningAlgorithm = new LearningAlgorithm();
	}

	@Override
	public Plan selectPlan(Goal goal, Set<Plan> candidatePlans) {
		Plan selectedPlan = null;
		Double maxContribution = null;

		for (Plan plan : candidatePlans) {
			double contribution = 0;

			SoftgoalPreferences preferences = (SoftgoalPreferences) plan
					.getPlanLibrary().getCapability().getBeliefBase()
					.getBelief(SoftgoalPreferences.NAME);

			for (Softgoal softgoal : capability.getMyAgent().getSoftgoals()) {
				Double preference = preferences
						.getPreferenceForSoftgoal(softgoal);

				if (preference != null) {
					double expectedContribution = learningAlgorithm
							.predictExpectedContribution(plan, softgoal);
					contribution += preference * expectedContribution;
				}
			}

			if (selectedPlan == null || maxContribution < contribution) {
				selectedPlan = plan;
				maxContribution = contribution;
			}

		}
		return selectedPlan;
	}

}