/*
 * Created on 29/01/2010 00:37:31 
 */
package br.pucrio.inf.les.bdijade.examples.ping;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdijade.plan.PlanBody;
import br.pucrio.inf.les.bdijade.plan.PlanInstance;
import br.pucrio.inf.les.bdijade.plan.PlanInstance.EndState;

/**
 * @author ingrid
 * 
 */
public class PingPlan extends Behaviour implements PlanBody {

	private static final long serialVersionUID = -6288758975856575305L;

	private String agent;
	private boolean done;
	private Log log;
	private MessageTemplate mt;
	private boolean sent;
	private int times;
	private int counter;

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
					this.done = true;
				} else {
					this.sent = false;
				}
			} else {
				block();
			}
		}
	}

	@Override
	public boolean done() {
		return done;
	}

	@Override
	public EndState getEndState() {
		return done ? EndState.SUCCESSFUL : null;
	}

	@Override
	public void init(PlanInstance planInstance) {
		this.log = LogFactory.getLog(this.getClass());
		Ping ping = (Ping) planInstance.getGoal();
		this.agent = ping.getAgent();
		this.sent = false;
		this.done = false;
		this.counter = 0;
		this.times = 1;
	}

}
