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

import java.lang.reflect.Field;

import bdi4jade.core.Capability;

/**
 * This class extends the {@link AbstractBelief} class and represents a belief
 * stored in an attribute of a capability.
 * 
 * @author Ingrid Nunes
 * 
 * @param <V>
 *            the type of the belief value.
 */
public class AttributeBelief<V> extends AbstractBelief<String, V> {

	private static final long serialVersionUID = 6600374179600475835L;

	private final Field field;
	private final Capability object;

	/**
	 * Initializes an attribute belief with the field that represents the belief
	 * and the capability it belongs to.
	 * 
	 * @param object
	 *            the capability to which this belief belongs to.
	 * @param field
	 *            the field that represents this belief.
	 */
	public AttributeBelief(Capability object, Field field) {
		super(field.getName());
		this.object = object;
		this.field = field;
	}

	/**
	 * Gets the value from the field that represents this belief.
	 * 
	 * @see bdi4jade.belief.Belief#getValue()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public V getValue() {
		field.setAccessible(true);
		V value = null;
		try {
			value = (V) field.get(object);
		} catch (IllegalAccessException iae) {

		}
		field.setAccessible(false);
		return value;
	}

	/**
	 * Sets the value given as parameter in the field that represents this
	 * belief.
	 * 
	 * @see bdi4jade.belief.AbstractBelief#updateValue(java.lang.Object)
	 */
	@Override
	protected void updateValue(V value) {
		field.setAccessible(true);
		try {
			field.set(object, value);
		} catch (IllegalAccessException iae) {

		}
		field.setAccessible(false);
	}

}
