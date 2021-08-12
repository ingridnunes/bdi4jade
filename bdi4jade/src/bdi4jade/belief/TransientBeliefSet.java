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
import java.util.Iterator;
import java.util.Set;

/**
 * This class extends the {@link AbstractBeliefSet} class and implements
 * {@link BeliefSet} interface, representing a transient belief set, which is
 * not persisted in a permanent memory.
 * 
 * @author Ingrid Nunes
 * 
 * @param <K>
 *            the type of the belief name.
 * @param <V>
 *            the type of the belief set values.
 */
public class TransientBeliefSet<K, V> extends AbstractBeliefSet<K, V> {

	private static final long serialVersionUID = 8345025506647930L;

	private Set<V> value;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	public TransientBeliefSet() {

	}

	/**
	 * Initializes a belief set with its name.
	 * 
	 * @param name
	 *            the name of this belief set.
	 */
	public TransientBeliefSet(K name) {
		super(name, new HashSet<V>());
	}

	/**
	 * Initializes a belief set with its name and an initial set of values.
	 * 
	 * @param name
	 *            the name of the belief set.
	 * @param values
	 *            the initial values of this belief set.
	 */
	public TransientBeliefSet(K name, Set<V> values) {
		super(name, values);
	}

	/**
	 * @see bdi4jade.belief.AbstractBeliefSet#addSetValue(Object)
	 */
	@Override
	protected void addSetValue(V value) {
		this.value.add(value);
	}

	/**
	 * @see bdi4jade.belief.Belief#getValue()
	 */
	@Override
	public Set<V> getValue() {
		return value;
	}

	/**
	 * @see bdi4jade.belief.BeliefSet#hasValue(java.lang.Object)
	 */
	@Override
	public boolean hasValue(V value) {
		return this.value.contains(value);
	}

	/**
	 * @see bdi4jade.belief.BeliefSet#iterator()
	 */
	@Override
	public Iterator<V> iterator() {
		return this.value.iterator();
	}

	/**
	 * @see bdi4jade.belief.AbstractBeliefSet#removeSetValue(Object)
	 */
	@Override
	protected boolean removeSetValue(V value) {
		return this.value.remove(value);
	}

	/**
	 * @see bdi4jade.belief.AbstractBelief#updateValue(java.lang.Object)
	 */
	@Override
	protected void updateValue(Set<V> value) {
		this.value = value;
	}

}