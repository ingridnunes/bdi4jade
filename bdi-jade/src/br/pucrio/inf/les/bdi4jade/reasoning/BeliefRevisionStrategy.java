//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes, et al.
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
// http://www.inf.puc-rio.br/~ionunes/
//
//----------------------------------------------------------------------------

package br.pucrio.inf.les.bdi4jade.reasoning;

import br.pucrio.inf.les.bdi4jade.core.BDIAgent;

/**
 * This is used during the BDI interpreter cycle to review the agent's beliefs.
 * 
 * @author ingrid
 */
public interface BeliefRevisionStrategy {

	/**
	 * This method receives an agent whose beliefs are to be reviewed. It may
	 * check for inconsistencies among beliefs, associate time with them, and
	 * son on. These beliefs may be analyzed as a whole (all beliefs from all
	 * belief bases of each capability) or each belief base can be analyzed
	 * individually.
	 * 
	 * @param bdiAgent
	 *            the {@link BDIAgent} whose beliefs are to be reviewed.
	 */
	public void reviewBeliefs(BDIAgent bdiAgent);

}
