/*
 * Created on 20/12/2009 17:16:12 
 */
package br.pucrio.inf.les.bdijade.belief;

import jade.content.Concept;

import java.io.Serializable;

/**
 * @author ingrid
 * 
 */
public class PersistentBelief<T> extends Belief<T> implements Serializable,
		Concept {

	private static final long serialVersionUID = 2893517209462636003L;

	protected T value;

	/**
	 * Initializes a belief with its name.
	 * 
	 * @param name
	 *            the belief name.
	 */
	public PersistentBelief(String name) {
		super(name);
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.belief.Belief#setValue(java.lang.Object)
	 */
	public void setValue(T value) {
		// XXX PersistentBelief.setValue(T value)
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.belief.Belief#getValue()
	 */
	@Override
	public T getValue() {
		// XXX PersistentBelief.getValue()
		return null;
	}

}
