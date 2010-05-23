/*
 * Created on 29/01/2010 00:35:39 
 */
package br.pucrio.inf.les.bdijade.examples.ping;

import jade.lang.acl.MessageTemplate;

import java.util.HashSet;
import java.util.Set;

import br.pucrio.inf.les.bdijade.core.BeliefBase;
import br.pucrio.inf.les.bdijade.core.Capability;
import br.pucrio.inf.les.bdijade.core.PlanLibrary;
import br.pucrio.inf.les.bdijade.plan.Plan;
import br.pucrio.inf.les.bdijade.util.plan.SimplePlan;

/**
 * @author ingrid
 * 
 */
public class PingPongCapability extends Capability {

	public static final String PING = "ping";
	public static final String PONG = "pong";

	private static final long serialVersionUID = -4800805796961540570L;

	private static Set<Plan> getPlans() {
		Set<Plan> plans = new HashSet<Plan>();
		plans.add(new SimplePlan(Ping.class, PingPlan.class));
		plans.add(new SimplePlan(MessageTemplate.MatchContent(PING),
				PongPlan.class));
		return plans;
	}

	private String otherAgent;

	public PingPongCapability(String id, String otherAgent) {
		super(id, new BeliefBase(), new PlanLibrary(getPlans()));
		this.otherAgent = otherAgent;
	}

	@Override
	protected void setup() {
		myAgent.addGoal(new Ping(otherAgent));
	}

}
