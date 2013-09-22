/**
 * 
 */
package br.ufrgs.inf.bdi4jade.examples.planselection;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.ufrgs.inf.bdi4jade.belief.TransientBelief;
import br.ufrgs.inf.bdi4jade.plan.Plan;
import br.ufrgs.inf.bdi4jade.preference.SoftgoalPreferences;
import br.ufrgs.inf.bdi4jade.softgoal.Softgoal;
import br.ufrgs.inf.bdi4jade.util.agent.UtilityBasedBDIAgent;

/**
 * @author ingrid
 * 
 */
public class TransportationAgent extends UtilityBasedBDIAgent {

	private static final long serialVersionUID = 2712019445290687786L;
	public static final String SATISFACTION = "Satisfaction";

	private final Random rand;
	private final Log log;

	public TransportationAgent() {
		this.log = LogFactory.getLog(this.getClass());
		this.rand = new Random(System.currentTimeMillis());
	}

	protected void init() {
		for (Softgoal softgoal : Softgoals.SOFTGOALS) {
			this.addSoftgoal(softgoal);
		}
		for (Plan plan : Plans.PLANS) {
			this.getRootCapability().getPlanLibrary().addPlan(plan);
		}
		this.getRootCapability()
				.getBeliefBase()
				.addBelief(
						new TransientBelief<GenericValueFunction<Integer>>(
								SATISFACTION,
								new GenericValueFunction<Integer>()));
	}

	public void updatedPreferences() {
		SoftgoalPreferences preferences = (SoftgoalPreferences) this
				.getRootCapability().getBeliefBase()
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
		log.debug("Preferences: " + preferences);
	}

}
