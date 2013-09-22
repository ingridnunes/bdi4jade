//----------------------------------------------------------------------------
// Copyright (C) 2013  Ingrid Nunes, et al.
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

package br.ufrgs.inf.bdi4jade.util.agent;

import br.ufrgs.inf.bdi4jade.core.BDIAgent;
import br.ufrgs.inf.bdi4jade.preference.SoftgoalPreferences;
import br.ufrgs.inf.bdi4jade.util.reasoning.UtilityBasedPlanSelectionStrategy;

/**
 * @author ingrid
 * 
 */
public class UtilityBasedBDIAgent extends BDIAgent {

	private static final long serialVersionUID = -1721751203235905764L;

	public UtilityBasedBDIAgent() {
		setPlanSelectionStrategy(new UtilityBasedPlanSelectionStrategy(this));
		getRootCapability().getBeliefBase()
				.addBelief(new SoftgoalPreferences());
	}

}
