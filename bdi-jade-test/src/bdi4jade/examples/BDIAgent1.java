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

package bdi4jade.examples;

import bdi4jade.core.BDIAgent;
import bdi4jade.examples.compositegoal.CompositeGoalCapability;

/**
 * @author ingrid
 * 
 */
public class BDIAgent1 extends BDIAgent {

	public static final String MY_NAME = "AGENT_1";
	private static final long serialVersionUID = -8505187840524213951L;

	@Override
	protected void init() {
		// this.addCapability(new BlocksWorldCapability());
		// this.addCapability(new PlanFailedCapability());
		// this.addCapability(new SubgoalCapability());
		// this.addCapability(new PingPongCapability(BDIAgent1.MY_NAME,
		// BDIAgent2.MY_NAME));
		this.addCapability(new CompositeGoalCapability(true));
		// this.addCapability(new CompositeGoalCapability(false));
	}
}
