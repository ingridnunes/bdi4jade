/*
 * Created on 27/01/2010 12:12:55 
 */
package br.pucrio.inf.les.bdijade.plan;

import br.pucrio.inf.les.bdijade.core.Intention;

/**
 * This interface indicates that a {@link PlanBody} should be finalized in case
 * of being terminated.
 * 
 * @author ingrid *
 */
public interface DisposablePlanBody {

	/**
	 * This method is called when a PlanBody is terminates before its end, for
	 * instance, when the {@link Intention} associated with the PlanBody is not
	 * longer desired.
	 */
	public void onAbort();

}
