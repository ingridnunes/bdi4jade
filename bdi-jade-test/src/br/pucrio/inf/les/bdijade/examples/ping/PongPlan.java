/*
 * Created on 29/01/2010 00:42:42 
 */
package br.pucrio.inf.les.bdijade.examples.ping;

import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import br.pucrio.inf.les.bdijade.message.MessageGoal;
import br.pucrio.inf.les.bdijade.plan.PlanBody;
import br.pucrio.inf.les.bdijade.plan.PlanInstance;
import br.pucrio.inf.les.bdijade.plan.PlanInstance.EndState;

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
