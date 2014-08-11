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

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;

/**
 * @author ingrid
 * 
 */
public class PingPlan extends AbstractPlanBody {

	private static final long serialVersionUID = -6288758975856575305L;

	private String agent;
	private int counter;
	private Log log;
	private MessageTemplate mt;
	private boolean sent;
	private int times;

	@Override
	public void action() {
		if (!sent) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setContent(PingPongCapability.PING);
			msg.addReceiver(new AID(agent, false));
			msg.setConversationId("cin" + System.currentTimeMillis());
			msg.setReplyWith("inform" + System.currentTimeMillis());
			myAgent.send(msg);
			this.mt = MessageTemplate.and(MessageTemplate
					.MatchConversationId(msg.getConversationId()),
					MessageTemplate.MatchInReplyTo(msg.getReplyWith()));
			this.sent = true;
			log.info("Ping sent to agent " + agent + "!");
		} else {
			ACLMessage reply = myAgent.receive(mt);
			if (reply != null) {
				log.info("Pong received from " + reply.getSender().getName()
						+ "!");
				log.info("Content: " + reply.getContent());
				counter++;
				if (counter == times) {
					setEndState(EndState.SUCCESSFULL);
				} else {
					this.sent = false;
				}
			} else {
				block();
			}
		}
	}

	@Override
	public void onStart() {
		this.log = LogFactory.getLog(this.getClass());
		Ping ping = (Ping) getGoal();
		this.agent = ping.getAgent();
		this.sent = false;
		this.counter = 0;
		this.times = 1;
	}

}
