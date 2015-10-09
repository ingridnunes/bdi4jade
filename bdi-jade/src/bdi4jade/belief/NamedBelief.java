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

/**
 * This class extends the {@link TransientBelief} class and represents a
 * transient belief with a name given by a string.
 * 
 * @author Ingrid Nunes
 * 
 * @param <V>
 *            the type of the belief value.
 */
public class NamedBelief<V> extends TransientBelief<String, V> {

	private static final long serialVersionUID = -1803100612413928203L;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	public NamedBelief() {

	}

	/**
	 * Initializes a named belief with its name.
	 * 
	 * @param name
	 *            the belief name.
	 */
	public NamedBelief(String name) {
		super(name);
	}

	/**
	 * Initializes a named belief with its name and a initial value.
	 * 
	 * @param name
	 *            the belief name.
	 * @param value
	 *            the initial belief value.
	 */
	public NamedBelief(String name, V value) {
		super(name);
		this.value = value;
	}

}
