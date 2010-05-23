/*
 * Created on 27/01/2010 14:05:16 
 */
package br.pucrio.inf.les.bdijade.exception;

import br.pucrio.inf.les.bdijade.belief.Belief;

/**
 * This class represents an exception that a belief that already exists in the
 * belief base is trying to be added.
 * 
 * @author ingrid
 */
public class BeliefAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -6082968354395705561L;

	private Belief<?> belief;

	/**
	 * Creates a new instance of BeliefAlreadyExistsException.
	 * 
	 * @param belief
	 *            the belief that already exists.
	 */
	public BeliefAlreadyExistsException(Belief<?> belief) {
		this.belief = belief;
	}

	/**
	 * @return the belief
	 */
	public Belief<?> getBelief() {
		return belief;
	}

	/**
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return "Belief already exists exception: " + belief;
	}
}
