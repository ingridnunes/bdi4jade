/*
 * Created on 28/01/2010 22:45:06 
 */
package br.pucrio.inf.les.bdijade.message;

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

import br.pucrio.inf.les.bdijade.core.BDIAgent;
import br.pucrio.inf.les.bdijade.core.Capability;

/**
 * @author ingrid
 * 
 */
public class BDIAgentMsgReceiver extends MsgReceiver {

	public static class BDIAgentMatchExpression implements MatchExpression {

		private static final long serialVersionUID = -1076583615928481034L;

		private BDIAgentMsgReceiver bdiAgentMsgReceiver;

		@Override
		public boolean match(ACLMessage msg) {
			Set<Capability> capabilities = new HashSet<Capability>();

			for (Capability capability : bdiAgentMsgReceiver.getMyAgent()
					.getCapabilities()) {
				if (capability.canProcess(msg)) {
					capabilities.add(capability);
				}
			}

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
