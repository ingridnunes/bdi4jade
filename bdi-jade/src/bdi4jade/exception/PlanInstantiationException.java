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

import bdi4jade.plan.planbody.PlanBody;

/**
 * This method represents an exception that occurred during the instantiation
 * process of a {@link PlanBody}.
 * 
 * @author Ingrid Nunes
 */
public class PlanInstantiationException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new instance of PlanInstantiationException.
	 */
	public PlanInstantiationException() {
	}

	/**
	 * Creates a new instance of PlanInstantiationException.
	 * 
	 * @param _message
	 *            the message to show.
	 */
	public PlanInstantiationException(final String _message) {
		super(_message);
	}

	/**
	 * Creates a new instance of PlanInstantiationException.
	 * 
	 * @param _message
	 *            the message to show.
	 * @param _cause
	 *            the exception that caused this exception.
	 */
	public PlanInstantiationException(final String _message,
			final Throwable _cause) {
		super(_message, _cause);
	}

	/**
	 * Creates a new instance of PlanInstantiationException.
	 * 
	 * @param _cause
	 *            the exception that caused this exception.
	 */
	public PlanInstantiationException(final Throwable _cause) {
		super(_cause);
	}

}
