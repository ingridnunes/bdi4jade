//----------------------------------------------------------------------------
// Copyright (C) 2013  Ingrid Nunes
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

package bdi4jade.examples.template.plan;

import jade.core.behaviours.OneShotBehaviour;
import bdi4jade.plan.PlanBody;
import bdi4jade.plan.PlanInstance;
import bdi4jade.plan.PlanInstance.EndState;

/**
 * @author ingrid
 * 
 */
public class MyPlan1Body extends OneShotBehaviour implements PlanBody {

	private static final long serialVersionUID = -3947024373151941681L;

	private EndState endState;

	@Override
	public void action() {
		// TODO Auto-generated method stub
		this.endState = EndState.SUCCESSFUL;
	}

	@Override
	public EndState getEndState() {
		// TODO Auto-generated method stub
		return endState;
	}

	@Override
	public void init(PlanInstance planInstance) {
		// TODO Auto-generated method stub
		this.endState = null;
	}

}
