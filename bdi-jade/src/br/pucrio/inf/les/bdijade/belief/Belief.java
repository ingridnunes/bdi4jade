/*
 * Created on 13/12/2009 11:42:55 
 */
package br.pucrio.inf.les.bdijade.belief;

import jade.content.Concept;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

import br.pucrio.inf.les.bdijade.core.BeliefBase;
import br.pucrio.inf.les.bdijade.util.MetadataElement;

/**
 * Represents a belief of the belief base. It has a name and a value associate
 * with it.
 * 
 * @author ingrid
 */
public abstract class Belief<T> extends MetadataElement implements
		Serializable, Concept {

	private static final long serialVersionUID = 5098122115249071355L;

	private final Set<BeliefBase> beliefBases;
	protected final String name;

	/**
	 * Initializes a belief with its name.
	 * 
	 * @param name
	 *            the belief name.
	 */
	public Belief(String name) {
		if (name == null)
			throw new InvalidParameterException("Belief name must be not null.");
		this.name = name;
		this.beliefBases = new HashSet<BeliefBase>();
	}

	/**
	 * Initializes a belief with its name.
	 * 
	 * @param name
	 *            the belief name.
	 * @param value
	 *            the belief initial value.
	 */
	public Belief(String name, T value) {
		this(name);
		setValue(value);
	}

	/**
	 * Adds a belief base that contains this belief. The agent whose capability
	 * contains this belief in the belief base believes in this belief.
	 * 
	 * @param beliefBase
	 *            the belief base to be added.
	 */
	public void addBeliefBase(BeliefBase beliefBase) {
		this.beliefBases.add(beliefBase);
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof Belief<?>) {
			Belief<?> b = (Belief<?>) obj;
			return this.name.equals(b.getName());
		}
		return false;
	}

	/**
	 * @return the beliefBases
	 */
	public Set<BeliefBase> getBeliefBases() {
		return beliefBases;
	}

	/**
	 * Gets the name of the Belief.
	 * 
	 * @return the belief name.
	 */
	public final String getName() {
		return name;
	}

	/**
	 * Gets the current value of the Belief.
	 * 
	 * @return the belief value.
	 */
	public abstract T getValue();

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}

	/**
	 * Removes a belief base that does not contain this belief anymore. The
	 * agent whose capability does not contain this belief in the belief base
	 * does not believe in this belief anymore.
	 * 
	 * @param beliefBases
	 *            the belief base to be removed.
	 */
	public void removeBeliefBase(BeliefBase beliefBases) {
		this.beliefBases.remove(beliefBases);
	}

	/**
	 * Sets a new value to the belief.
	 * 
	 * @param value
	 *            the new value.
	 */
	public abstract void setValue(T value);

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer(name).append(" = ").append(getValue())
				.toString();
	}

}
