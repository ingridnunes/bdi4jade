/*
 * Created on 29/01/2010 10:36:54 
 */
package br.pucrio.inf.les.bdijade.reasoning;

import br.pucrio.inf.les.bdijade.core.BDIAgent;

/**
 * This is used during the BDI interpreter cycle to review the agent's beliefs.
 * 
 * @author ingrid
 */
public interface BeliefRevisionStrategy {

	/**
	 * This method receives an agent whose beliefs are to be reviewed. It may
	 * check for inconsistencies among beliefs, associate time with them, and
	 * son on. These beliefs may be analyzed as a whole (all beliefs from all
	 * belief bases of each capability) or each belief base can be analyzed
	 * individually.
	 * 
	 * @param bdiAgent
	 *            the {@link BDIAgent} whose beliefs are to be reviewed.
	 */
	public void reviewBeliefs(BDIAgent bdiAgent);

}
