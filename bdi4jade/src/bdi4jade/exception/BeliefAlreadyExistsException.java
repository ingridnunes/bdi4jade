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

package bdi4jade.exception;

import bdi4jade.belief.Belief;

/**
 * This class represents an exception that a belief that already exists in the
 * belief base is trying to be added.
 * 
 * @author Ingrid Nunes
 */
public class BeliefAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = -6082968354395705561L;

	private Belief<?, ?> belief;

	/**
	 * Creates a new instance of BeliefAlreadyExistsException.
	 * 
	 * @param belief
	 *            the belief that already exists.
	 */
	public BeliefAlreadyExistsException(Belief<?, ?> belief) {
		this.belief = belief;
	}

	/**
	 * @return the belief
	 */
	public Belief<?, ?> getBelief() {
		return belief;
	}

	/**
	 * @see java.lang.Throwable#toString()
	 */
	@Override
	public String toString() {
		return "Belief already exists exception: " + belief;
	}

}
