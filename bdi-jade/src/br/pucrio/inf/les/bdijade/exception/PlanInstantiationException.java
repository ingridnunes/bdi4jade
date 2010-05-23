/*
 * Created on 25/01/2010 16:44:11 
 */
package br.pucrio.inf.les.bdijade.exception;

import br.pucrio.inf.les.bdijade.plan.PlanBody;

/**
 * This method represents an exception that occurred during the instantiation
 * process of a {@link PlanBody}.
 * 
 * @author ingrid
 */
public class PlanInstantiationException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of PlanInstantiationException.
	 */
	public PlanInstantiationException() {
	}

	/**
	 * Creates a new instance of PlanInstantiationException.
	 * 
	 * @param _message
	 *            the message to show.
	 */
	public PlanInstantiationException(final String _message) {
		super(_message);
	}

	/**
	 * Creates a new instance of PlanInstantiationException.
	 * 
	 * @param _message
	 *            the message to show.
	 * @param _cause
	 *            the exception that caused this exception.
	 */
	public PlanInstantiationException(final String _message,
			final Throwable _cause) {
		super(_message, _cause);
	}

	/**
	 * Creates a new instance of PlanInstantiationException.
	 * 
	 * @param _cause
	 *            the exception that caused this exception.
	 */
	public PlanInstantiationException(final Throwable _cause) {
		super(_cause);
	}

}
