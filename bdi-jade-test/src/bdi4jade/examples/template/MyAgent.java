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

package bdi4jade.examples.template;

import bdi4jade.core.BDIAgent;
import bdi4jade.core.Capability;
import bdi4jade.examples.template.goal.MyGoal;
import bdi4jade.examples.template.plan.MyPlan1;
import bdi4jade.examples.template.plan.MyPlan2;
import bdi4jade.extension.planselection.utilitybased.SoftgoalPreferences;
import bdi4jade.extension.planselection.utilitybased.UtilityBasedBDIAgent;
import bdi4jade.goal.Softgoal;

/**
 * @author ingrid
 * 
 */
public class MyAgent extends BDIAgent {

	static final long serialVersionUID = 2712019445290687786L;

	private final Capability rootCapability;

	public MyAgent() {
		this.rootCapability = new UtilityBasedBDIAgent();
		this.addCapability(rootCapability);
	}

	public Capability getRootCapability() {
		return rootCapability;
	}

	protected void init() {
		for (Softgoal softgoal : MyAgentSoftgoals.ALL_SOFTGOALS) {
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

		preferences.setPreferenceForSoftgoal(MyAgentSoftgoals.Softgoal1, 0.3);
		preferences.setPreferenceForSoftgoal(MyAgentSoftgoals.Softgoal2, 0.7);

	}

}