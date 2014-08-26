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
import bdi4jade.goal.BeliefGoal;
import bdi4jade.goal.PropositionalBeliefValueGoal;
import bdi4jade.reasoning.AbstractReasoningStrategy;
import bdi4jade.reasoning.OptionGenerationFunction;
import br.ufrgs.inf.bdinetr.domain.IpPreposition.Anomalous;
import br.ufrgs.inf.bdinetr.domain.IpPreposition.Benign;
import br.ufrgs.inf.bdinetr.domain.IpPreposition.Restricted;

/**
 * @author Ingrid Nunes
 */
public class AnomalyDetectionCapability extends Capability {

	private class ReasoningStrategy extends AbstractReasoningStrategy implements
			OptionGenerationFunction {
		@Override
		public void generateGoals(GoalUpdateSet goalUpdateSet) {
			Set<Belief<?, ?>> anomalousIpBeliefs = getBeliefBase()
					.getBeliefsByType(Anomalous.class);
			for (Belief<?, ?> belief : anomalousIpBeliefs) {
				PropositionalBelief<Anomalous> anomalous = (PropositionalBelief<Anomalous>) belief;
				if (anomalous.getValue()) {
					getMyAgent()
							.addGoal(
									AnomalyDetectionCapability.this,
									new PropositionalBeliefValueGoal<Restricted>(
											new Restricted(anomalous.getName()
													.getIp()), Boolean.TRUE));
					log.debug("goal(restricted(" + anomalous.getName().getIp()
							+ "))");
					getMyAgent().addGoal(
							AnomalyDetectionCapability.this,
							new BeliefGoal<Benign>(new Benign(anomalous
									.getName().getIp())));
					log.debug("goal(?benign(" + anomalous.getName().getIp()
							+ "))");
				}
			}

			Set<Belief<?, ?>> restrictedBeliefs = getBeliefBase()
					.getBeliefsByType(Restricted.class);
			for (Belief<?, ?> belief : restrictedBeliefs) {
				PropositionalBelief<Restricted> restricted = (PropositionalBelief<Restricted>) belief;
				if (restricted.getValue()) {
					PropositionalBelief<Anomalous> anomalous = (PropositionalBelief<Anomalous>) getBeliefBase()
							.getBelief(
									new Anomalous(restricted.getName().getIp()));
					if (anomalous != null && !anomalous.getValue()) {
						getMyAgent().addGoal(
								AnomalyDetectionCapability.this,
								new PropositionalBeliefValueGoal<Restricted>(
										new Restricted(restricted.getName()
												.getIp()), Boolean.FALSE));
						log.debug("goal(not restricted("
								+ restricted.getName().getIp() + "))");
					}

				}
			}
		}
	}

	private static final long serialVersionUID = -1705728861020677126L;

	public AnomalyDetectionCapability() {
		ReasoningStrategy strategy = new ReasoningStrategy();
		setOptionGenerationFunction(strategy);
	}

}
