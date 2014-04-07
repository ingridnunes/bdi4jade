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

/**
 * This class extends the {@link Belief} and represents a transient belief,
 * which is not persisted in a permanent memory.
 * 
 * @author ingrid
 */
public class TransientBelief<T> extends AbstractBelief<T> {

	private static final long serialVersionUID = 2893517209462636003L;

	protected T value;

	/**
	 * Initializes a belief with its name.
	 * 
	 * @param name
	 *            the belief name.
	 */
	public TransientBelief(String name) {
		super(name);
	}

	/**
	 * Initializes a belief with its name and a initial value.
	 * 
	 * @param name
	 *            the belief name.
	 * @param value
	 *            the initial belief value.
	 */
	public TransientBelief(String name, T value) {
		super(name);
		this.value = value;
	}

	/**
	 * @see bdi4jade.belief.Belief#getValue()
	 */
	@Override
	public T getValue() {
		return this.value;
	}

	/**
	 * @see bdi4jade.belief.Belief#setValue(java.lang.Object)
	 */
	public void setValue(T value) {
		this.value = value;
	}

}
