package bdi4jade.extension.planselection.learningbased.example;

import java.util.Random;

import bdi4jade.core.SingleCapabilityAgent;
import bdi4jade.extension.planselection.utilitybased.SoftgoalPreferences;
import bdi4jade.goal.Softgoal;

public class ExampleAgent extends SingleCapabilityAgent {

	private static final long serialVersionUID = 3086657102660054554L;

	public ExampleAgent() {
		super(new ExampleCapability());
	}
	
	@Override
	protected void init() {
		for (Softgoal softgoal : Softgoals.SOFTGOALS) {
			this.addSoftgoal(softgoal);
		}
		
		this.initPreferences();
	}
	
	private void initPreferences() {
		Random rand = new Random();
		SoftgoalPreferences preferences = (SoftgoalPreferences) this
				.getCapability().getBeliefBase()
				.getBelief(SoftgoalPreferences.NAME);

		double total = 0;
		for (Softgoal softgoal : Softgoals.SOFTGOALS) {
			double value = rand.nextDouble();
			total += value;
			preferences.setPreferenceForSoftgoal(softgoal, value);
		}
		for (Softgoal softgoal : Softgoals.SOFTGOALS) {
			double value = preferences.getPreferenceForSoftgoal(softgoal);
			double normValue = value / total;
			preferences.setPreferenceForSoftgoal(softgoal, normValue);
		}
	}
}
