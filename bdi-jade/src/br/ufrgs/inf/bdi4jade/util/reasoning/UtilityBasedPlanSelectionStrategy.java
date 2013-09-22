//----------------------------------------------------------------------------
// Copyright (C) 2013  Ingrid Nunes, et al.
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
// To contact the authors:
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package br.ufrgs.inf.bdi4jade.util.reasoning;

import java.util.List;
import java.util.Set;

import br.ufrgs.inf.bdi4jade.core.BDIAgent;
import br.ufrgs.inf.bdi4jade.goal.Goal;
import br.ufrgs.inf.bdi4jade.plan.Plan;
import br.ufrgs.inf.bdi4jade.plan.PlanContribution;
import br.ufrgs.inf.bdi4jade.preference.SoftgoalPreferences;
import br.ufrgs.inf.bdi4jade.reasoning.PlanSelectionStrategy;
import br.ufrgs.inf.bdi4jade.softgoal.Softgoal;

/**
 * A utility-based implementation of the {@link PlanSelectionStrategy}. It
 * selects the plan that has the best expected value based on the plan
 * contributions and the agent current preferences.
 * 
 * @author ingrid
 */
public class UtilityBasedPlanSelectionStrategy implements PlanSelectionStrategy {

	private final BDIAgent myAgent;

	public UtilityBasedPlanSelectionStrategy(BDIAgent myAgent) {
		this.myAgent = myAgent;
	}

	@SuppressWarnings("unchecked")
	private double calculateExpectedUtility(Plan plan, Softgoal softgoal) {
		List<PlanContribution> contributions = (List<PlanContribution>) plan
				.getMetadata(Plan.DefaultMetadata.CONTRIBUTIONS);

		double expectedUtility = 0;
		if (contributions != null) {
			for (PlanContribution contribution : contributions) {
				expectedUtility += contribution.getProbability()
						* contribution.getValue();
			}
		}
		return expectedUtility;
	}

	/**
	 * @return the myAgent
	 */
	public BDIAgent getMyAgent() {
		return myAgent;
	}

	/**
	 * @see br.ufrgs.inf.bdi4jade.reasoning.PlanSelectionStrategy#selectPlan(br.ufrgs.inf.bdi4jade.goal.Goal,
	 *      java.util.Set)
	 */
	@Override
	public Plan selectPlan(Goal goal, Set<Plan> plans) {
		Plan selectedPlan = null;
		Double maxUtility = null;

		for (Plan plan : plans) {
			double utility = 0;

			SoftgoalPreferences preferences = (SoftgoalPreferences) plan
					.getPlanLibrary().getCapability().getBeliefBase()
					.getBelief(SoftgoalPreferences.NAME);

			for (Softgoal softgoal : myAgent.getAllSoftgoals()) {
				Double preference = preferences
						.getPreferenceForSoftgoal(softgoal);
				if (preference != null) {

					double expectedUtility = calculateExpectedUtility(plan,
							softgoal);
					utility += preference * expectedUtility;

				}
			}
			if (selectedPlan == null || maxUtility < utility) {
				selectedPlan = plan;
				maxUtility = utility;
			}
		}

		return selectedPlan;
	}

}
