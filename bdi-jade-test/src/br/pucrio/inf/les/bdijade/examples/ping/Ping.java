/*
 * Created on 29/01/2010 00:36:05 
 */
package br.pucrio.inf.les.bdijade.examples.ping;

import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * @author ingrid
 * 
 */
public class Ping implements Goal {

	private static final long serialVersionUID = -7733145369836002329L;

	private String agent;

	public Ping(String agent) {
		this.agent = agent;
	}

	public String getAgent() {
		return agent;
	}

}
