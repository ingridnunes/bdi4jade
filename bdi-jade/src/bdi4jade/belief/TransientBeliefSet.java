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
	 * @see bdi4jade.belief.BeliefSet#addValue(java.lang.Object)
	 */
	public void addValue(T value) {
		this.values.add(value);
	}

	/**
	 * @see bdi4jade.belief.Belief#getValue()
	 */
	@Override
	public Set<T> getValue() {
		return values;
	};

	/**
	 * @see bdi4jade.belief.BeliefSet#hasValue(java.lang.Object)
	 */
	public boolean hasValue(T value) {
		return this.values.contains(value);
	}

	/**
	 * @see bdi4jade.belief.BeliefSet#iterator()
	 */
	public Iterator<T> iterator() {
		return this.values.iterator();
	}

	/**
	 * @see bdi4jade.belief.BeliefSet#removeValue(java.lang.Object)
	 */
	public boolean removeValue(T value) {
		return this.values.remove(value);
	}

	/**
	 * @see bdi4jade.belief.Belief#setValue(java.lang.Object)
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