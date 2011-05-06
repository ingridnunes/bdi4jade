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

package br.pucrio.inf.les.bdi4jade.examples.ping;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdi4jade.message.MessageGoal;
import br.pucrio.inf.les.bdi4jade.plan.PlanBody;
import br.pucrio.inf.les.bdi4jade.plan.PlanInstance;
import br.pucrio.inf.les.bdi4jade.plan.PlanInstance.EndState;

/**
 * @author ingrid
 * 
 */
public class PongPlan extends OneShotBehaviour implements PlanBody {

	private static final long serialVersionUID = -3352874506241004611L;

	private Log log;
	private ACLMessage pingMsg;

	@Override
	public void action() {
		log.info("Ping received from agent " + pingMsg.getSender().getName()
				+ "!");
		ACLMessage reply = pingMsg.createReply();
		reply.setContent(PingPongCapability.PONG);
		this.myAgent.send(reply);
		log.info("Pong sent to agent" + pingMsg.getSender().getName() + "!");
	}

	@Override
	public EndState getEndState() {
		return EndState.SUCCESSFUL;
	}

	@Override
	public void init(PlanInstance planInstance) {
		this.log = LogFactory.getLog(this.getClass());
		MessageGoal goal = (MessageGoal) planInstance.getGoal();
		pingMsg = goal.getMessage();
	}

}
