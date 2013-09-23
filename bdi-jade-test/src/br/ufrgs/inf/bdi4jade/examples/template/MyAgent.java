/**
 * 
 */
package br.ufrgs.inf.bdi4jade.examples.template;

import br.ufrgs.inf.bdi4jade.examples.template.goal.MyGoal;
import br.ufrgs.inf.bdi4jade.examples.template.plan.MyPlan1;
import br.ufrgs.inf.bdi4jade.examples.template.plan.MyPlan2;
import br.ufrgs.inf.bdi4jade.preference.SoftgoalPreferences;
import br.ufrgs.inf.bdi4jade.softgoal.Softgoal;
import br.ufrgs.inf.bdi4jade.util.agent.UtilityBasedBDIAgent;

/**
 * @author ingrid
 * 
 */
public class MyAgent extends UtilityBasedBDIAgent {

	private static final long serialVersionUID = 2712019445290687786L;

	public MyAgent() {

	}

	protected void init() {
		for (Softgoal softgoal : Constants.ALL_SOFTGOALS) {
			this.addSoftgoal(softgoal);
		}

		this.getRootCapability().getPlanLibrary().addPlan(new MyPlan1());
		this.getRootCapability().getPlanLibrary().addPlan(new MyPlan2());

		initPreferences();
		
		addGoal(new MyGoal());
	}

	public void initPreferences() {
		SoftgoalPreferences preferences = (SoftgoalPreferences) this
				.getRootCapability().getBeliefBase()
				.getBelief(SoftgoalPreferences.NAME);

		preferences.setPreferenceForSoftgoal(Constants.COMFORT, 0.1);
		preferences.setPreferenceForSoftgoal(Constants.COST, 0.2);
		preferences.setPreferenceForSoftgoal(Constants.PERFORMANCE, 0.15);
		preferences.setPreferenceForSoftgoal(Constants.SAFETY, 0.4);
		preferences.setPreferenceForSoftgoal(Constants.SECURITY, 0.15);
	}

}