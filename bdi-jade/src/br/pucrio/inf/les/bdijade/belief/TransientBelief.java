/*
 * Created on 13/12/2009 11:52:41 
 */
package br.pucrio.inf.les.bdijade.belief;

import jade.content.Concept;

import java.io.Serializable;

/**
 * This class extends the {@link Belief} and represents a transient belief,
 * which is not persisted in a permanent memory.
 * 
 * @author ingrid
 */
public class TransientBelief<T> extends Belief<T> implements Serializable,
		Concept {

	private static final long serialVersionUID = 2893517209462636003L;

	protected T value;

	/**
	 * Initializes a belief with its name.
	 * 
	 * @param name
	 *            the belief name.
	 */
	public TransientBelief(String name) {
		super(name);
	}

	/**
	 * Initializes a belief with its name and a initial value.
	 * 
	 * @param name
	 *            the belief name.
	 * @param value
	 *            the initial belief value.
	 */
	public TransientBelief(String name, T value) {
		super(name);
		this.value = value;
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.belief.Belief#getValue()
	 */
	@Override
	public T getValue() {
		return this.value;
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.belief.Belief#setValue(java.lang.Object)
	 */
	public void setValue(T value) {
		this.value = value;
	}

}
