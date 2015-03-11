package bdi4jade.extension.planselection.learningbased;

import bdi4jade.annotation.Belief;
import bdi4jade.core.Capability;
import bdi4jade.extension.planselection.utilitybased.SoftgoalPreferences;

/**
 * Represents a capability that implements the {@link}
 * LearningBasedPlanSelectionStrategy.
 * 
 * @author João Faccin
 */
public class LearningBasedCapability extends Capability {

	private static final long serialVersionUID = -1044132085270106726L;

	@Belief
	protected SoftgoalPreferences softgoalPreferences = new SoftgoalPreferences();

	/**
	 * Default constructor that sets the {@link}
	 * LearningBasedPlanSelectionStrategy as the plan selection strategy.
	 */
	public LearningBasedCapability() {
		setPlanSelectionStrategy(new LearningBasedPlanSelectionStrategy());
	}

}