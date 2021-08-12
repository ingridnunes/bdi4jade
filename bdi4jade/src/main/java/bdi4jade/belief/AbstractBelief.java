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
// http://inf.ufrgs.br/prosoft/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.belief;

import java.util.HashSet;
import java.util.Set;

import bdi4jade.core.MetadataElementImpl;
import bdi4jade.event.BeliefEvent;
import bdi4jade.event.BeliefEvent.Action;

/**
 * This is an abstract class that implements the {@link Belief} interface. It
 * implements some of the interface methods, leaving some implementations to the
 * subclasses, mainly the choice of how the belief value is stored.
 * 
 * It is class observable by belief bases ({@link BeliefBase}), allowing the
 * observation on changes in the value of this belief.
 * 
 * @param <K>
 *            the type of the belief name or key.
 * 
 * @param <V>
 *            the type of the belief value.
 * 
 * @author Ingrid Nunes
 */
public abstract class AbstractBelief<K, V> extends MetadataElementImpl
		implements Belief<K, V> {

	private static final long serialVersionUID = 5098122115249071355L;

	private final Set<BeliefBase> beliefBases;
	private K name;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	public AbstractBelief() {
		this.beliefBases = new HashSet<>();
	}

	/**
	 * Initializes a belief with its name.
	 * 
	 * @param name
	 *            the belief name.
	 */
	public AbstractBelief(K name) {
		if (name == null)
			throw new NullPointerException("Belief name must be not null.");
		this.name = name;
		this.beliefBases = new HashSet<BeliefBase>();
	}

	/**
	 * Initializes a belief with its name and an initial value.
	 * 
	 * @param name
	 *            the belief name.
	 * @param value
	 *            the belief initial value.
	 */
	public AbstractBelief(K name, V value) {
		this(name);
		updateValue(value);
	}

	/**
	 * @see Belief#addBeliefBase(BeliefBase)
	 */
	public void addBeliefBase(BeliefBase beliefBase) {
		this.beliefBases.add(beliefBase);
	}

	/**
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() throws CloneNotSupportedException {
		AbstractBelief<?, ?> clone = (AbstractBelief<?, ?>) super.clone();
		clone.beliefBases.clear();
		return clone;
	}

	/**
	 * Returns true of the object is a belief and has the same name of this
	 * belief.
	 * 
	 * @param obj
	 *            to object to be tested if it is equal to this belief.
	 * 
	 * @see Object#equals(Object)
	 */
	@Override
	public final boolean equals(Object obj) {
		if (obj instanceof Belief<?, ?>) {
			Belief<?, ?> b = (Belief<?, ?>) obj;
			return this.name.equals(b.getName());
		}
		return false;
	}

	/**
	 * @see Belief#getBeliefBases()
	 */
	public Set<BeliefBase> getBeliefBases() {
		return new HashSet<BeliefBase>(beliefBases);
	}

	/**
	 * @see Belief#getName()
	 */
	public final K getName() {
		return name;
	}

	/**
	 * Returns the hash code of this belief name.
	 * 
	 * @return the hash code of this belief.
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public final int hashCode() {
		return name == null ? 0 : this.name.hashCode();
	}

	/**
	 * Notifies the belief bases with which this belief is associated that the
	 * value of this belief has changed.
	 * 
	 * @param beliefEvent
	 *            the {@link BeliefEvent} describing the change on this belief
	 *            value
	 */
	protected void notifyBeliefBases(BeliefEvent beliefEvent) {
		for (BeliefBase beliefBase : beliefBases) {
			beliefBase.notifyBeliefChanged(beliefEvent);
		}
	}

	/**
	 * @see Belief#removeBeliefBase(BeliefBase)
	 */
	public void removeBeliefBase(BeliefBase beliefBase) {
		this.beliefBases.remove(beliefBase);
	}

	/**
	 * Sets the name of this belief. Ideally, a belief name should be final and
	 * initialized in the constructor. This method should be only used if
	 * persistence frameworks are used.
	 * 
	 * @param name
	 *            the name to set.
	 */
	public void setName(K name) {
		this.name = name;
	}

	/**
	 * Sets a new value to the belief and notifies belief bases of changes on
	 * this belief value.
	 * 
	 * @param value
	 *            the new value.
	 * 
	 * @see Belief#setValue(Object)
	 */
	public final void setValue(V value) {
		Object oldValue = getValue();
		updateValue(value);
		notifyBeliefBases(new BeliefEvent(this, Action.BELIEF_UPDATED, oldValue));
	}

	/**
	 * Returns this belief as a string in the form:
	 * "belief name = belief value".
	 * 
	 * @return the string representation of this belief.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer().append(name).append(" = ").append(getValue())
				.toString();
	}

	/**
	 * Sets the value of this belief. It is invoked by the
	 * {@link #setValue(Object)} method.
	 * 
	 * @param value
	 *            the value to set.
	 */
	protected abstract void updateValue(V value);

}
