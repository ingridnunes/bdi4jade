//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes
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
package br.ufrgs.inf.bdinetr.capability;

import java.util.Set;

import bdi4jade.belief.Belief;
import bdi4jade.belief.PropositionalBelief;
import bdi4jade.core.Capability;
import bdi4jade.core.GoalUpdateSet;
import bdi4jade.goal.PropositionalBeliefValueGoal;
import bdi4jade.reasoning.AbstractReasoningStrategy;
import bdi4jade.reasoning.OptionGenerationFunction;
import br.ufrgs.inf.bdinetr.domain.FlowPreposition.Threat;
import br.ufrgs.inf.bdinetr.domain.FlowPreposition.ThreatResponded;

/**
 * @author Ingrid Nunes
 */
public class ClassifierCapability extends Capability {

	private class ReasoningStrategy extends AbstractReasoningStrategy implements
			OptionGenerationFunction {
		@Override
		public void generateGoals(GoalUpdateSet goalUpdateSet) {
			Set<Belief<?, ?>> threatBeliefs = getBeliefBase().getBeliefsByType(
					Threat.class);
			for (Belief<?, ?> belief : threatBeliefs) {
				PropositionalBelief<Threat> threat = (PropositionalBelief<Threat>) belief;
				if (threat.getValue()) {
					getMyAgent().addGoal(
							ClassifierCapability.this,
							new PropositionalBeliefValueGoal<ThreatResponded>(
									new ThreatResponded(threat.getName()
											.getFlow()), Boolean.TRUE));
					log.debug("goal(threatResponded("
							+ threat.getName().getFlow() + "))");
				}
			}
		}
	}

	private static final long serialVersionUID = -1705728861020677126L;

	public ClassifierCapability() {
		ReasoningStrategy strategy = new ReasoningStrategy();
		setOptionGenerationFunction(strategy);
	}

}
