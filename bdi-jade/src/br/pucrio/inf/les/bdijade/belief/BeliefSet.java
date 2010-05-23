/*
 * Created on 27/01/2010 13:38:58 
 */
package br.pucrio.inf.les.bdijade.belief;

import java.util.Iterator;
import java.util.Set;

/**
 * This class represents a belief that has a set of values associated with it.
 * 
 * @author ingrid
 */
public abstract class BeliefSet<T> extends Belief<Set<T>> {

	private static final long serialVersionUID = 8345025506647930L;

	/**
	 * Creates a new belief set with the provided name.
	 * 
	 * @param name
	 *            the name of this belief set.
	 */
	public BeliefSet(String name) {
		super(name);
	}

	/**
	 * Adds a new value to this belief set.
	 * 
	 * @param value
	 *            the value to be added.
	 */
	public abstract void addValue(T value);

	/**
	 * Checks if this belief set has the provided value.
	 * 
	 * @param value
	 *            the value to be tested.
	 * @return true if the belief set contains this value.
	 */
	public abstract boolean hasValue(T value);

	/**
	 * Returns an iterator for this belief set.
	 * 
	 * @return the iterator.
	 */
	public abstract Iterator<T> iterator();

	/**
	 * Removes a value from this belief set.
	 * 
	 * @param value
	 *            the value to be removed.
	 * @return true if the value was removed.
	 */
	public abstract boolean remove(T value);

}
