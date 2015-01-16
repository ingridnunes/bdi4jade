package bdi4jade.extension.planselection.learningbased;

import bdi4jade.annotation.Belief;
import bdi4jade.core.Capability;
import bdi4jade.extension.planselection.utilitybased.SoftgoalPreferences;

public class LearningBasedCapability extends Capability {

	private static final long serialVersionUID = -1044132085270106726L;

	@Belief
	protected SoftgoalPreferences softgoalPreferences = new SoftgoalPreferences();

	/*
	 * Passando o LearningAlgorithm como parâmetro aqui ele será instanciado no
	 * agente. Assim será possível definir diferentes algoritmos de aprendizado
	 * para diferentes agentes.
	 */
	public LearningBasedCapability(LearningAlgorithm learningAlgorithm) {
		setPlanSelectionStrategy(new LearningBasedPlanSelectionStrategy(
				learningAlgorithm));
	}

}