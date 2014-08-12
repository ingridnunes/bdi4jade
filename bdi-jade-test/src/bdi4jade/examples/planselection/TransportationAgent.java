//----------------------------------------------------------------------------
// Copyright (C) 2013 Ingrid Nunes
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

package bdi4jade.examples.planselection;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.belief.TransientBelief;
import bdi4jade.core.BDIAgent;
import bdi4jade.core.Capability;
import bdi4jade.extension.planselection.utilitybased.SoftgoalPreferences;
import bdi4jade.extension.planselection.utilitybased.UtilityBasedBDIAgent;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;

/**
 * @author ingrid
 * 
 */
public class TransportationAgent extends BDIAgent {

	public static final String SATISFACTION = "Satisfaction";

	static final long serialVersionUID = 2712019445290687786L;

	private final Log log;
	private final Random rand;
	private final Capability rootCapability;

	public TransportationAgent() {
		this.log = LogFactory.getLog(this.getClass());
		this.rand = new Random(System.currentTimeMillis());
		this.rootCapability = new UtilityBasedBDIAgent();
		this.addCapability(rootCapability);
	}

	public Capability getRootCapability() {
		return rootCapability;
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

	public void updatePreferences() {
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
