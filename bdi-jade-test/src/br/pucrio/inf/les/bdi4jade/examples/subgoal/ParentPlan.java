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

package br.pucrio.inf.les.bdi4jade.examples.subgoal;

import jade.core.behaviours.Behaviour;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdi4jade.plan.PlanBody;
import br.pucrio.inf.les.bdi4jade.plan.PlanInstance;
import br.pucrio.inf.les.bdi4jade.plan.PlanInstance.EndState;

/**
 * @author ingrid
 * 
 */
public class ParentPlan extends Behaviour implements PlanBody {

	private static final long serialVersionUID = -5432560989511973914L;

	private int counter;
	private Log log = LogFactory.getLog(this.getClass());
	private PlanInstance planInstance;

	@Override
	public void action() {
		if (counter == 0) {
			this.planInstance.dispatchSubgoal(new Subgoal());
		}
		log.info("ParentPlan executing... counter " + counter);
		counter++;
	}

	@Override
	public boolean done() {
		return counter >= 10;
	}

	@Override
	public EndState getEndState() {
		return done() ? EndState.FAILED : null;
	}

	@Override
	public void init(PlanInstance planInstance) {
		this.planInstance = planInstance;
		this.counter = 0;
	}

}
