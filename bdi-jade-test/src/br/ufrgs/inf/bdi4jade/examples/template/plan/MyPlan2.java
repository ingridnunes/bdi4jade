/**
 * 
 */
package br.ufrgs.inf.bdi4jade.examples.template.plan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import br.ufrgs.inf.bdi4jade.examples.planselection.Softgoals;
import br.ufrgs.inf.bdi4jade.examples.template.Constants;
import br.ufrgs.inf.bdi4jade.examples.template.goal.MyGoal;
import br.ufrgs.inf.bdi4jade.plan.PlanContribution;
import br.ufrgs.inf.bdi4jade.softgoal.Softgoal;
import br.ufrgs.inf.bdi4jade.util.plan.SimplePlan;

/**
 * @author ingrid
 * 
 */
public class MyPlan2 extends SimplePlan {

	public MyPlan2() {
		super(MyGoal.class, MyPlan2Body.class);

		Map<Softgoal, List<PlanContribution>> contributions = (Map<Softgoal, List<PlanContribution>>) getMetadata(DefaultMetadata.CONTRIBUTIONS);
		List<PlanContribution> sgContributions = null;

		sgContributions = new ArrayList<PlanContribution>();
		sgContributions.add(new PlanContribution(Constants.Softgoal1, 0.3, 0.0));
		sgContributions.add(new PlanContribution(Constants.Softgoal2, 0.7, 1.0));
		contributions.put(Softgoals.SAFETY, sgContributions);

		sgContributions = new ArrayList<PlanContribution>();
		sgContributions.add(new PlanContribution(Constants.Softgoal1, 0.5, 0.0));
		sgContributions.add(new PlanContribution(Constants.Softgoal2, 0.5, 1.0));
		contributions.put(Softgoals.PERFORMANCE, sgContributions);
	}

}