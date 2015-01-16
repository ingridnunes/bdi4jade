package bdi4jade.extension.planselection.learningbased;

import java.util.Collection;
import java.util.Map;

import bdi4jade.goal.Softgoal;
import bdi4jade.plan.planbody.AbstractPlanBody;

public abstract class LearningBasedPlanBody extends AbstractPlanBody {

	private static final long serialVersionUID = -5064965263121492233L;

	@SuppressWarnings("unchecked")
	@Override
	public void onStart() {
		Collection<PlanMetadata> planMetadata = ((Map<Softgoal, PlanMetadata>) this
				.getPlan().getMetadata(PlanMetadata.METADATA_NAME)).values();

		for (PlanMetadata metadata : planMetadata) {
			metadata.getNotifiedAtStartedPlanExecution();
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public int onEnd() {
		Collection<PlanMetadata> planMetadata = ((Map<Softgoal, PlanMetadata>) this
				.getPlan().getMetadata(PlanMetadata.METADATA_NAME)).values();

		for (PlanMetadata metadata : planMetadata) {
			metadata.getNotifiedAtEndedPlanExecution();
		}
		return super.onEnd();
	}
}