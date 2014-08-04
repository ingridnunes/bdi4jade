//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
// To contact the authors:
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.belief;

import java.security.InvalidParameterException;
import java.util.HashSet;
import java.util.Set;

import bdi4jade.core.MetadataElementImpl;
import bdi4jade.event.BeliefEvent;
import bdi4jade.event.BeliefEvent.Action;

/**
 * @author ingrid
 * 
 */
public abstract class AbstractBelief<T> extends MetadataElementImpl implements
		Belief<T> {

	private static final long serialVersionUID = 5098122115249071355L;

	private final Set<BeliefBase> beliefBases;
	protected final String name;

	/**
	 * Initializes a belief with its name.
	 * 
	 * @param name
	 *            the belief name.
	 */
	public AbstractBelief(String name) {
		if (name == null)
			throw new NullPointerException("Belief name must be not null.");
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
	public AbstractBelief(String name, T value) {
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
	 * Returns the belief bases with which this belief is associated.
	 * 
	 * @return the beliefBases.
	 */
	public Set<BeliefBase> getBeliefBases() {
		return new HashSet<BeliefBase>(beliefBases);
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
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return this.name.hashCode();
	}

	protected void notifyBeliefBases(BeliefEvent beliefEvent) {
		for (BeliefBase beliefBase : beliefBases) {
			beliefBase.notifyBeliefChanged(beliefEvent);
		}
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
	public final void setValue(T value) {
		Object oldValue = getValue();
		updateValue(value);
		notifyBeliefBases(new BeliefEvent(this, Action.BELIEF_UPDATED, oldValue));
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer(name).append(" = ").append(getValue())
				.toString();
	}

	protected abstract void updateValue(T value);

}
