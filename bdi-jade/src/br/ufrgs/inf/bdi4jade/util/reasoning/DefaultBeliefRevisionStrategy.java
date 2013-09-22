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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package br.ufrgs.inf.bdi4jade.util.reasoning;

import br.ufrgs.inf.bdi4jade.core.BDIAgent;
import br.ufrgs.inf.bdi4jade.core.BeliefBase;
import br.ufrgs.inf.bdi4jade.core.Capability;
import br.ufrgs.inf.bdi4jade.reasoning.BeliefRevisionStrategy;

/**
 * This class is the default strategy for the belief revision. It invokes the
 * {@link BeliefBase#reviewBeliefs()} method for the belief base of all
 * capabilities of the agent.
 * 
 * @author ingrid
 */
public class DefaultBeliefRevisionStrategy implements BeliefRevisionStrategy {

	/**
	 * Invokes the {@link BeliefBase#reviewBeliefs()} for the belief base of all
	 * capabilties.
	 * 
	 * @see br.ufrgs.inf.bdi4jade.reasoning.BeliefRevisionStrategy#reviewBeliefs(br.ufrgs.inf.bdi4jade.core.BDIAgent)
	 */
	@Override
	public void reviewBeliefs(BDIAgent bdiAgent) {
		reviewBeliefs(bdiAgent.getRootCapability());
	}

	public void reviewBeliefs(Capability capability) {
		capability.getBeliefBase().reviewBeliefs();
		for (Capability child : capability.getChildren()) {
			reviewBeliefs(child);
		}
	}

}
