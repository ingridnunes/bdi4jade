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

import java.util.HashSet;
import java.util.Set;

import bdi4jade.event.BeliefEvent;
import bdi4jade.event.BeliefEvent.Action;

/**
 * @author ingrid
 * 
 */
public abstract class AbstractBeliefSet<T> extends AbstractBelief<Set<T>>
		implements BeliefSet<T> {

	private static final long serialVersionUID = 8345025506647930L;

	/**
	 * Creates a new transient belief set with the provided name.
	 * 
	 * @param name
	 *            the name of this belief set.
	 */
	public AbstractBeliefSet(String name) {
		super(name, new HashSet<T>());
	}

	/**
	 * Creates a transient belief set.
	 * 
	 * @param name
	 *            the name of the belief set.
	 * @param values
	 *            the initial values of this belief set.
	 */
	public AbstractBeliefSet(String name, Set<T> values) {
		super(name, values);
	}

	protected abstract void addSetValue(T value);

	/**
	 * @see bdi4jade.belief.BeliefSet#addValue(java.lang.Object)
	 */
	@Override
	public final void addValue(T value) {
		if (!hasValue(value)) {
			addSetValue(value);
			notifyBeliefBases(new BeliefEvent(this, Action.BELIEF_ADDED, value));
		}
	}

	protected abstract boolean removeSetValue(T value);

	/**
	 * @see bdi4jade.belief.BeliefSet#removeValue(java.lang.Object)
	 */
	@Override
	public final boolean removeValue(T value) {
		boolean removed = removeSetValue(value);
		if (removed) {
			notifyBeliefBases(new BeliefEvent(this, Action.BELIEF_ADDED, value));
		}
		return removed;
	}

}