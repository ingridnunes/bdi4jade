package bdi4jade.extension.planselection.learningbased;

import java.util.Collection;
import java.util.Map;

import bdi4jade.goal.Softgoal;
import bdi4jade.plan.planbody.AbstractPlanBody;

/**
 * Represents the learning-based plan body abstraction, being an extension of
 * the {@link} AbstractPlanBody.
 * 
 * @author João Faccin
 */
public abstract class LearningBasedPlanBody extends AbstractPlanBody {

	private static final long serialVersionUID = -5064965263121492233L;

	/**
	 * Notifies some elements of a plan metadata (e.g. {@link} Outcome instance)
	 * that a plan execution will start.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onStart() {
		Collection<PlanMetadataElement> planMetadata = ((Map<Softgoal, PlanMetadataElement>) this
				.getPlan().getMetadata(PlanMetadataElement.METADATA_NAME)).values();

		for (PlanMetadataElement metadata : planMetadata) {
			metadata.getNotifiedAtStartedPlanExecution();
		}
	}

	/**
	 * Notifies some elements of a plan metadata (e.g. {@link} Outcome instance)
	 * that a plan execution ended.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int onEnd() {
		Collection<PlanMetadataElement> planMetadata = ((Map<Softgoal, PlanMetadataElement>) this
				.getPlan().getMetadata(PlanMetadataElement.METADATA_NAME)).values();

		for (PlanMetadataElement metadata : planMetadata) {
			metadata.getNotifiedAtEndedPlanExecution();
			metadata.increasePlanExecutionsCounter();
		}

		return super.onEnd();
	}
}