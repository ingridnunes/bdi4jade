package bdi4jade.extension.planselection.learningbased;

import java.util.Set;

import bdi4jade.extension.planselection.utilitybased.SoftgoalPreferences;
import bdi4jade.goal.Goal;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;
import bdi4jade.reasoning.AbstractReasoningStrategy;
import bdi4jade.reasoning.PlanSelectionStrategy;

public class LearningBasedPlanSelectionStrategy extends
		AbstractReasoningStrategy implements PlanSelectionStrategy {

	private LearningAlgorithm learningAlgorithm;

	/*
	 * Instanciarei um algoritmo de aprendizagem aqui, e.g. public
	 * UtilityBasedPlanSelectionStrategy(LearningAlgorithm la){}.
	 * LearningAlgorithm será uma interface que obrigará a criação de uma função
	 * getValue() ou algo do tipo. Criarei uma série de classes (nesse momento
	 * apenas duas) que implementam essa interface, e.g.
	 * LinearRegressionAlgorithm e SVMAlgorithm, que serão criadas no pacote
	 * implementation.
	 */
	public LearningBasedPlanSelectionStrategy(
			LearningAlgorithm learningAlgorithm) {
		this.learningAlgorithm = learningAlgorithm;
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