/*
 * Created on 13/12/2009 11:38:51 
 */
package br.pucrio.inf.les.bdijade.plan;

import jade.core.behaviours.Behaviour;
import br.pucrio.inf.les.bdijade.plan.PlanInstance.EndState;

/**
 * This interface defines a PlanBody. Plans are executed as behaviors (
 * {@link Behaviour}), but executed in the BDI context, these behaviors should
 * also implement this interface.
 * 
 * @author ingrid
 */
public interface PlanBody {

	/**
	 * Returns the end state of the execution of this plan.
	 * 
	 * @return the end state of this plan, or null if it has not finished yet.
	 */
	public EndState getEndState();

	/**
	 * Initializes the PlanBody. It is invoked just after its instantiation.
	 * 
	 * @param planInstance
	 *            the plan instance that contains contextual information for
	 *            this plan body.
	 */
	public void init(PlanInstance planInstance);

}
