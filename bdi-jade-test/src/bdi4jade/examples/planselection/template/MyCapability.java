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

package bdi4jade.examples.planselection.template;

import bdi4jade.examples.template.goal.MyGoal;
import bdi4jade.examples.template.plan.MyPlan1;
import bdi4jade.examples.template.plan.MyPlan2;
import bdi4jade.extension.planselection.utilitybased.UtilityBasedCabability;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.Plan;

/**
 * @author Ingrid Nunes
 */
public class MyCapability extends UtilityBasedCabability {

	static final long serialVersionUID = 2712019445290687786L;

	@bdi4jade.annotation.Plan
	private Plan myPlan1 = new MyPlan1();

	@bdi4jade.annotation.Plan
	private Plan myPlan2 = new MyPlan2();

	protected void setup() {
		for (Softgoal softgoal : MyAgentSoftgoals.ALL_SOFTGOALS) {
			getMyAgent().addSoftgoal(softgoal);
		}
		initPreferences();
		getMyAgent().addGoal(new MyGoal());
	}

	public void initPreferences() {
		this.softgoalPreferences.setPreferenceForSoftgoal(
				MyAgentSoftgoals.Softgoal1, 0.3);
		this.softgoalPreferences.setPreferenceForSoftgoal(
				MyAgentSoftgoals.Softgoal2, 0.7);
	}

}