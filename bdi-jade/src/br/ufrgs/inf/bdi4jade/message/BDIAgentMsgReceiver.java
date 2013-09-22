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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package br.ufrgs.inf.bdi4jade.message;

import jade.core.behaviours.DataStore;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.MessageTemplate.MatchExpression;
import jade.proto.states.MsgReceiver;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.ufrgs.inf.bdi4jade.core.BDIAgent;
import br.ufrgs.inf.bdi4jade.core.Capability;

/**
 * @author ingrid
 * 
 */
public class BDIAgentMsgReceiver extends MsgReceiver {

	public static class BDIAgentMatchExpression implements MatchExpression {

		private static final long serialVersionUID = -1076583615928481034L;

		private BDIAgentMsgReceiver bdiAgentMsgReceiver;

		private void getCanProcessCapabilities(final ACLMessage msg,
				final Set<Capability> capabilities, Capability capability) {
			if (capability.canProcess(msg)) {
				capabilities.add(capability);
			}
			for (Capability child : capability.getChildren()) {
				getCanProcessCapabilities(msg, capabilities, child);
			}
		}

		@Override
		public boolean match(ACLMessage msg) {
			Set<Capability> capabilities = new HashSet<Capability>();
			getCanProcessCapabilities(msg, capabilities, bdiAgentMsgReceiver
					.getMyAgent().getRootCapability());

			if (!capabilities.isEmpty()) {
				bdiAgentMsgReceiver.messageMatched(msg, capabilities);
				return true;
			} else {
				return false;
			}
		}

		public void setBdiAgentMsgReceiver(
				BDIAgentMsgReceiver bdiAgentMsgReceiver) {
			this.bdiAgentMsgReceiver = bdiAgentMsgReceiver;
		}

	}

	public static final Object MSG_KEY = "msgs";

	private static final long serialVersionUID = -4435254708782532901L;

	private final Log log;
	private final Map<ACLMessage, Set<Capability>> msgs;

	public BDIAgentMsgReceiver(BDIAgent agent,
			BDIAgentMatchExpression matchExpression) {
		super(agent, new MessageTemplate(matchExpression), INFINITE,
				new DataStore(), MSG_KEY);
		matchExpression.setBdiAgentMsgReceiver(this);
		this.msgs = new HashMap<ACLMessage, Set<Capability>>();
		this.log = LogFactory.getLog(this.getClass());
	}

	@Override
	public boolean done() {
		return false;
	}

	public BDIAgent getMyAgent() {
		return (BDIAgent) this.myAgent;
	}

	@Override
	protected void handleMessage(ACLMessage msg) {
		log.debug("Message received.");
		Set<Capability> capabilities = msgs.get(msg);
		if (capabilities != null) {
			MessageGoal goal = new MessageGoal(msg);
			log.debug("This capabilities can process the message:");
			for (Capability capability : capabilities) {
				log.info("* " + capability);
			}
			getMyAgent().addGoal(goal);
			msgs.remove(msg);
		}
	}

	public void messageMatched(ACLMessage msg, Set<Capability> capabilities) {
		this.msgs.put(msg, capabilities);
	}

}
