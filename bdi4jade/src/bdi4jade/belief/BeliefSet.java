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

import java.util.Iterator;
import java.util.Set;

/**
 * This interface represents a belief that has a set of values associated with
 * it.
 * 
 * @param <K>
 *            the type of the belief name or key.
 * 
 * @param <V>
 *            the type of the belief set values.
 * 
 * @author Ingrid Nunes
 */
public interface BeliefSet<K, V> extends Belief<K, Set<V>> {

	/**
	 * Adds a new value to this belief set.
	 * 
	 * @param value
	 *            the value to be added.
	 */
	public void addValue(V value);

	/**
	 * Checks whether this belief set has the provided value.
	 * 
	 * @param value
	 *            the value to be checked.
	 * @return true if the belief set contains this value, false otherwise.
	 */
	public boolean hasValue(V value);

	/**
	 * Returns an iterator for this belief set.
	 * 
	 * @return the iterator to iterate the values of the belief set.
	 */
	public Iterator<V> iterator();

	/**
	 * Removes a value from this belief set.
	 * 
	 * @param value
	 *            the value to be removed.
	 * @return true if the value was removed, false otherwise.
	 */
	public boolean removeValue(V value);

}
