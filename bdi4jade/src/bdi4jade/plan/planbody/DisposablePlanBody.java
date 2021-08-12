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

package bdi4jade.plan.planbody;

import bdi4jade.core.Intention;

/**
 * This interface indicates that a {@link PlanBody} should be finalized in case
 * of being terminated.
 * 
 * @author Ingrid Nunes
 */
public interface DisposablePlanBody {

	/**
	 * This method is called when a PlanBody is terminates before its end, for
	 * instance, when the {@link Intention} associated with the PlanBody is not
	 * longer desired.
	 */
	public void onAbort();

}
