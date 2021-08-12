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

package bdi4jade.message;

import jade.core.Agent;
import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;
import jade.proto.states.MsgReceiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.core.BDIAgent;

/**
 * This class extends the {@link MsgReceiver} behavior from the JADE platform
 * and is responsible for receiving agent messages and creating
 * {@link MessageGoal} so that a BDI agent can process it. Message goals are
 * creates solely if there is an agent plan of any capability that is able to
 * process the message.
 * 
 * @author Ingrid Nunes
 * 
 */
public class BDIAgentMsgReceiver extends MsgReceiver {

	/**
	 * This class implements the {@link MatchExpression} interface from JADE and
	 * is responsible for verifying if there is at least one agent capability
	 * with a plan that is able to process a given message.
	 * 
	 * @author Ingrid Nunes
	 * 
	 */
	public class BDIAgentMatchExpression implements MatchExpression {

		private static final long serialVersionUID = -1076583615928481034L;

		/**
		 * @see jade.lang.acl.MessageTemplate.MatchExpression#match(jade.lang.acl.ACLMessage)
		 */
		@Override
		public boolean match(ACLMessage msg) {
			log.debug("Message received.");
			if (getMyAgent().canHandle(msg)) {
				return true;
			} else {
				log.debug("Message cannot be handled:" + msg);
				return false;
			}
		}

	}

	private static final Log log = LogFactory.getLog(BDIAgentMsgReceiver.class);
	public static final Object MSG_KEY = "msgs";
	private static final long serialVersionUID = -4435254708782532901L;

	/**
	 * Initializes this message receiver, which is associated with a BDI agent.
	 * 
	 * @param agent
	 *            the BDI agent that this behavior is associated with.
	 */
	public BDIAgentMsgReceiver(BDIAgent agent) {
		super((Agent) agent, MessageTemplate.MatchAll(), INFINITE,
				new DataStore(), MSG_KEY);
		this.template = new MessageTemplate(new BDIAgentMatchExpression());
	}

	/**
	 * Returns always false, as this behavior is responsible for message
	 * processing while a BDI agent is alive.
	 * 
	 * @see jade.proto.states.MsgReceiver#done()
	 */
	@Override
	public boolean done() {
		return false;
	}

	private BDIAgent getMyAgent() {
		return (BDIAgent) this.myAgent;
	}

	/**
	 * Creates a {@link MessageGoal} for the received message, when handling the
	 * message.
	 * 
	 * @see jade.proto.states.MsgReceiver#handleMessage(jade.lang.acl.ACLMessage)
	 */
	@Override
	protected void handleMessage(ACLMessage msg) {
		log.debug("Message received.");
		MessageGoal goal = new MessageGoal(msg);
		getMyAgent().addGoal(goal);
		log.debug("Message goal added for message: " + msg);
	}

}
