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

package bdi4jade.examples.ping;

import jade.lang.acl.ACLMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.annotation.Parameter;
import bdi4jade.annotation.Parameter.Direction;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;

/**
 * @author Ingrid Nunes
 */
public class PongPlanBody extends AbstractPlanBody {

	private static final Log log = LogFactory.getLog(PongPlanBody.class);
	public static final String MSG_CONTENT = "pong";
	private static final long serialVersionUID = -3352874506241004611L;

	private ACLMessage pingMsg;

	@Override
	public void action() {
		log.info("Ping received from agent " + pingMsg.getSender().getName()
				+ "!");
		ACLMessage reply = pingMsg.createReply();
		reply.setContent(MSG_CONTENT);
		this.myAgent.send(reply);
		log.info("Pong sent to agent " + pingMsg.getSender().getName() + "!");
		setEndState(EndState.SUCCESSFUL);
	}

	@Parameter(direction = Direction.IN)
	public void setMessage(ACLMessage pingMsg) {
		this.pingMsg = pingMsg;
	}

}
