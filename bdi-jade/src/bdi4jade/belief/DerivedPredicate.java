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
 * This class represents a logic predicate that is derived from other agent
 * belief. The evaluation of this predicate is not given, but derived from other
 * values of a belief base.
 * 
 * @author Ingrid Nunes
 */
public abstract class DerivedPredicate<K> extends DerivedBelief<K, Boolean>
		implements Predicate<K> {

	private static final long serialVersionUID = -1551397656846999182L;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	public DerivedPredicate() {
		super();
	}

	/**
	 * Creates a new derived predicate. The value of this belief cannot be set
	 * as it is derived from other values of a belief base.
	 * 
	 * @param name
	 *            the belief name.
	 */
	public DerivedPredicate(K name) {
		super(name);
	}

}
