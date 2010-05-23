/*
 * Created on 31/01/2010 11:30:45 
 */
package br.pucrio.inf.les.bdijade.examples;

import br.pucrio.inf.les.bdijade.core.BDIAgent;
import br.pucrio.inf.les.bdijade.examples.blocksworld.BlocksWorldCapability;
import br.pucrio.inf.les.bdijade.examples.compositegoal.CompositeGoalCapability;
import br.pucrio.inf.les.bdijade.examples.ping.PingPongCapability;
import br.pucrio.inf.les.bdijade.examples.planfailed.PlanFailedCapability;
import br.pucrio.inf.les.bdijade.examples.subgoal.SubgoalCapability;

/**
 * @author ingrid
 * 
 */
public class BDIAgent1 extends BDIAgent {

	private static final long serialVersionUID = -8505187840524213951L;
	public static final String MY_NAME = "AGENT_1";

	@Override
	protected void init() {
//		this.addCapability(new BlocksWorldCapability());
//		this.addCapability(new PlanFailedCapability());
//		this.addCapability(new SubgoalCapability());
//		this.addCapability(new PingPongCapability(BDIAgent1.MY_NAME,
//				BDIAgent2.MY_NAME));
	//	this.addCapability(new CompositeGoalCapability(true));
		this.addCapability(new CompositeGoalCapability(false));
	}

}
