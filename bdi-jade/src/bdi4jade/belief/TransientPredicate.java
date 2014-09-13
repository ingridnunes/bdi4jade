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
 * transient propositional belief, which is not persisted in a permanent memory.
 * 
 * @author Ingrid Nunes
 */
public class TransientPredicate<K> extends TransientBelief<K, Boolean>
		implements Predicate<K> {

	private static final long serialVersionUID = -2315938302480821432L;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	public TransientPredicate() {

	}

	/**
	 * Creates a new transient propositional belief, whose value is true.
	 * 
	 * @param name
	 *            the belief name.
	 */
	public TransientPredicate(K name) {
		super(name, Boolean.TRUE);
	}

	/**
	 * Initializes a transient propositional belief with its name and a initial
	 * value.
	 * 
	 * @param name
	 *            the belief name.
	 * @param value
	 *            the initial belief value.
	 */
	public TransientPredicate(K name, Boolean value) {
		super(name, value);
	}

}
