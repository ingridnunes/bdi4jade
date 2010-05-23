/*
 * Created on 31/01/2010 15:49:17 
 */
package br.pucrio.inf.les.bdijade.examples;

import br.pucrio.inf.les.bdijade.core.BDIAgent;
import br.pucrio.inf.les.bdijade.examples.ping.PingPongCapability;

/**
 * @author ingrid
 * 
 */
public class BDIAgent2 extends BDIAgent {

	private static final long serialVersionUID = -8505187840524213951L;
	public static final String MY_NAME = "AGENT_2";

	@Override
	protected void init() {
		this.addCapability(new PingPongCapability(BDIAgent2.MY_NAME,
				BDIAgent1.MY_NAME));
	}

}
