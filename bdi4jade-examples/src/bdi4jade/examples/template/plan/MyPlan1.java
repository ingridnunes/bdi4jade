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

package bdi4jade.examples.template.plan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bdi4jade.examples.planselection.template.MyAgentSoftgoals;
import bdi4jade.examples.template.goal.MyGoal;
import bdi4jade.extension.planselection.utilitybased.PlanContribution;
import bdi4jade.goal.Softgoal;
import bdi4jade.plan.DefaultPlan;

/**
 * @author Ingrid Nunes
 */
public class MyPlan1 extends DefaultPlan {

	public MyPlan1() {
		super(MyGoal.class, MyPlan1Body.class);

		Map<Softgoal, List<PlanContribution>> contributions = new HashMap<Softgoal, List<PlanContribution>>();
		List<PlanContribution> sgContributions = null;

		sgContributions = new ArrayList<PlanContribution>();
		sgContributions.add(new PlanContribution(MyAgentSoftgoals.Softgoal1,
				0.6, 0.0));
		sgContributions.add(new PlanContribution(MyAgentSoftgoals.Softgoal1,
				0.4, 1.0));
		contributions.put(MyAgentSoftgoals.Softgoal1, sgContributions);

		sgContributions = new ArrayList<PlanContribution>();
		sgContributions.add(new PlanContribution(MyAgentSoftgoals.Softgoal2,
				0.2, 0.0));
		sgContributions.add(new PlanContribution(MyAgentSoftgoals.Softgoal2,
				0.8, 1.0));
		contributions.put(MyAgentSoftgoals.Softgoal2, sgContributions);

		putMetadata(PlanContribution.METADATA_NAME, contributions);
	}

}