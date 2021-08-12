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

import bdi4jade.belief.Belief;
import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.planbody.AbstractPlanBody;

/**
 * @author Ingrid Nunes
 */
public class PingPlanBody extends AbstractPlanBody {

	private static final Log log = LogFactory.getLog(PingPlanBody.class);

	public static final String MSG_CONTENT = "ping";
	private static final long serialVersionUID = -6288758975856575305L;

	private int counter;
	private MessageTemplate mt;
	@bdi4jade.annotation.Belief
	private Belief<String, String> neighbour;
	@bdi4jade.annotation.Belief
	private Belief<String, Integer> pingTimes;
	private boolean sent;

	@Override
	public void action() {
		if (!sent) {
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setContent(MSG_CONTENT);
			msg.addReceiver(new AID(neighbour.getValue(), false));
			msg.setConversationId("cin" + System.currentTimeMillis());
			msg.setReplyWith("inform" + System.currentTimeMillis());
			myAgent.send(msg);
			this.mt = MessageTemplate.and(MessageTemplate
					.MatchConversationId(msg.getConversationId()),
					MessageTemplate.MatchInReplyTo(msg.getReplyWith()));
			this.sent = true;
			log.info("Ping sent to agent " + neighbour.getValue() + "!");
		} else {
			ACLMessage reply = myAgent.receive(mt);
			if (reply != null) {
				log.info("Pong received from " + reply.getSender().getName()
						+ "!");
				log.info("Content: " + reply.getContent());
				counter++;
				if (counter == pingTimes.getValue()) {
					setEndState(EndState.SUCCESSFUL);
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
		this.sent = false;
		this.counter = 0;
	}

}
