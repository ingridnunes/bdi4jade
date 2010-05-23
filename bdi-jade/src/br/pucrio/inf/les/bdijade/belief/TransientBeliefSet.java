/*
 * Created on 27/01/2010 13:47:54 
 */
package br.pucrio.inf.les.bdijade.belief;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * This class extends the {@link BeliefSet} and represents a transient belief
 * set, which is not persisted in a permanent memory.
 * 
 * @author ingrid
 */
public class TransientBeliefSet<T> extends BeliefSet<T> {

	private static final long serialVersionUID = 8345025506647930L;

	protected Set<T> values;

	/**
	 * Creates a transient belief set.
	 * 
	 * @param name
	 *            the name of the belief set.
	 */
	public TransientBeliefSet(String name) {
		super(name);
		this.values = new HashSet<T>();
	}

	/**
	 * Creates a transient belief set.
	 * 
	 * @param name
	 *            the name of the belief set.
	 * @param values
	 *            the initial values of this belief set.
	 */
	public TransientBeliefSet(String name, Set<T> values) {
		super(name);
		this.values = values;
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.belief.BeliefSet#addValue(java.lang.Object)
	 */
	public void addValue(T value) {
		this.values.add(value);
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.belief.Belief#getValue()
	 */
	@Override
	public Set<T> getValue() {
		return values;
	};

	/**
	 * @see br.pucrio.inf.les.bdijade.belief.BeliefSet#hasValue(java.lang.Object)
	 */
	public boolean hasValue(T value) {
		return this.values.contains(value);
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.belief.BeliefSet#iterator()
	 */
	public Iterator<T> iterator() {
		return this.values.iterator();
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.belief.BeliefSet#remove(java.lang.Object)
	 */
	public boolean remove(T value) {
		return this.values.remove(value);
	}

	/**
	 * @see br.pucrio.inf.les.bdijade.belief.Belief#setValue(java.lang.Object)
	 */
	public void setValue(Set<T> values) {
		this.values = values;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return this.values.toString();
	}

}