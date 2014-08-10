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

import bdi4jade.event.BeliefEvent;
import bdi4jade.event.BeliefEvent.Action;

/**
 * This is an abstract class that implements the {@link BeliefSet} interface,
 * and extends the {@link AbstractBeliefSet} class, parameterizing it with a
 * parameterized {@link Set}. It implements some of the interface methods,
 * leaving some implementations to the subclasses, mainly the choice of how the
 * belief set values are stored.
 * 
 * @author Ingrid Nunes
 * 
 * @param <T>
 *            the type of the belief set values.
 */
public abstract class AbstractBeliefSet<T> extends AbstractBelief<Set<T>>
		implements BeliefSet<T> {

	private static final long serialVersionUID = 8345025506647930L;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	protected AbstractBeliefSet() {

	}

	/**
	 * Initializes a belief set with its name.
	 * 
	 * @param name
	 *            the name of this belief set.
	 */
	public AbstractBeliefSet(String name) {
		super(name, new HashSet<T>());
	}

	/**
	 * Initializes a belief set with its name and an initial set of values.
	 * 
	 * @param name
	 *            the name of the belief set.
	 * @param values
	 *            the initial values of this belief set.
	 */
	public AbstractBeliefSet(String name, Set<T> values) {
		super(name);
		updateValue(new HashSet<T>(values));
	}

	/**
	 * Adds a value of this belief set. It is invoked by the
	 * {@link #addValue(Object)} method.
	 * 
	 * @param value
	 *            the value to be added.
	 */
	protected abstract void addSetValue(T value);

	/**
	 * Adds a value to the belief set and notifies belief bases of the addition
	 * of this value.
	 * 
	 * @param value
	 *            the value to be added.
	 * 
	 * @see BeliefSet#addValue(Object)
	 */
	@Override
	public final void addValue(T value) {
		if (!hasValue(value)) {
			addSetValue(value);
			notifyBeliefBases(new BeliefEvent(this,
					Action.BELIEF_SET_VALUE_ADDED, value));
		}
	}

	/**
	 * Removes a value of this belief set. It is invoked by the
	 * {@link #removeValue(Object)} method.
	 * 
	 * @param value
	 *            the value to be added.
	 * @return true if the value was removed, false otherwise.
	 */
	protected abstract boolean removeSetValue(T value);

	/**
	 * Removes a value of the belief set and notifies belief bases of the
	 * removal of this value.
	 * 
	 * @param value
	 *            the value to be removed.
	 * 
	 * @see BeliefSet#removeValue(Object)
	 */
	@Override
	public final boolean removeValue(T value) {
		boolean removed = removeSetValue(value);
		if (removed) {
			notifyBeliefBases(new BeliefEvent(this,
					Action.BELIEF_SET_VALUE_REMOVED, value));
		}
		return removed;
	}

}