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

package bdi4jade.examples.planfailed;

import jade.core.behaviours.Behaviour;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.plan.PlanBody;
import bdi4jade.plan.PlanInstance;
import bdi4jade.plan.PlanInstance.EndState;

/**
 * @author ingrid
 * 
 */
public class MyPlan extends Behaviour implements PlanBody {

	private static final long serialVersionUID = -220345270457161508L;
	
	private EndState endState = null;
	private PlanInstance planInstance;
	private Log log = LogFactory.getLog(this.getClass());

	public void action() {
		long random = new Random().nextLong();
		log.info("Random: " + random);
		endState = (random % 3 == 0) ? EndState.SUCCESSFUL : EndState.FAILED;
		log.info(planInstance.getGoal() + " Plan#"
				+ planInstance.getPlan().getId() + " EndState: " + endState);
	}

	public boolean done() {
		return true;
	}

	public EndState getEndState() {
		return endState;
	}

	public void init(PlanInstance planInstance) {
		this.planInstance = planInstance;
	}
}