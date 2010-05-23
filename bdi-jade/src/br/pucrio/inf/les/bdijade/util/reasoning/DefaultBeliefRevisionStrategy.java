/*
 * Created on 29/01/2010 10:40:16 
 */
package br.pucrio.inf.les.bdijade.util.reasoning;

import br.pucrio.inf.les.bdijade.core.BDIAgent;
import br.pucrio.inf.les.bdijade.core.BeliefBase;
import br.pucrio.inf.les.bdijade.core.Capability;
import br.pucrio.inf.les.bdijade.reasoning.BeliefRevisionStrategy;

/**
 * This class is the default strategy for the belief revision. It invokes the
 * {@link BeliefBase#reviewBeliefs()} method for the belief base of all
 * capabilities of the agent.
 * 
 * @author ingrid
 */
public class DefaultBeliefRevisionStrategy implements BeliefRevisionStrategy {

	/**
	 * Invokes the {@link BeliefBase#reviewBeliefs()} for the belief base of all
	 * capabilties.
	 * 
	 * @see br.pucrio.inf.les.bdijade.reasoning.BeliefRevisionStrategy#reviewBeliefs(br.pucrio.inf.les.bdijade.core.BDIAgent)
	 */
	@Override
	public void reviewBeliefs(BDIAgent bdiAgent) {
		for (Capability capability : bdiAgent.getCapabilities()) {
			capability.getBeliefBase().reviewBeliefs();
		}
	}

}
