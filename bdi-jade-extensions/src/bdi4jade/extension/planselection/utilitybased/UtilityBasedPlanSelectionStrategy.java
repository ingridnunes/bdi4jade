//----------------------------------------------------------------------------
// Copyright (C) 2013  Ingrid Nunes
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
// http://inf.ufrgs.br/prosoft/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.extension.planselection.utilitybased;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.goal.Goal;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;
import bdi4jade.reasoning.AbstractReasoningStrategy;
import bdi4jade.reasoning.PlanSelectionStrategy;

/**
 * A utility-based implementation of the {@link PlanSelectionStrategy}. It
 * selects the plan that has the best expected value based on the plan
 * contributions and the agent current preferences.
 * 
 * @author Ingrid Nunes
 */
public class UtilityBasedPlanSelectionStrategy extends
		AbstractReasoningStrategy implements PlanSelectionStrategy {

	private static final Log log = LogFactory
			.getLog(UtilityBasedPlanSelectionStrategy.class);

	public UtilityBasedPlanSelectionStrategy() {

	}

	@SuppressWarnings("unchecked")
	private double calculateExpectedUtility(Plan plan, Softgoal softgoal) {
		List<PlanContribution> contributions = ((Map<Softgoal, List<PlanContribution>>) plan
				.getMetadata(PlanContribution.METADATA_NAME)).get(softgoal);

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
	 * @see bdi4jade.reasoning.PlanSelectionStrategy#selectPlan(bdi4jade.goal.Goal,
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

			for (Softgoal softgoal : capability.getMyAgent().getSoftgoals()) {
				Double preference = preferences
						.getPreferenceForSoftgoal(softgoal);
				if (preference != null) {
					double expectedUtility = calculateExpectedUtility(plan,
							softgoal);
					utility += preference * expectedUtility;
				}
			}

			log.debug("EU[" + plan.getId() + "] = " + utility);

			if (selectedPlan == null || maxUtility < utility) {
				selectedPlan = plan;
				maxUtility = utility;
			}
		}

		return selectedPlan;
	}

}
