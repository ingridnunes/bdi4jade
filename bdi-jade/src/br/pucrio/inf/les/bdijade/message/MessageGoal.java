/*
 * Created on 25/01/2010 14:20:48 
 */
package br.pucrio.inf.les.bdijade.message;

import jade.lang.acl.ACLMessage;
import br.pucrio.inf.les.bdijade.goal.Goal;

/**
 * This class represents the goal of processing a message.
 * 
 * @author ingrid
 */
public class MessageGoal implements Goal {

	private static final long serialVersionUID = -5960866880528268312L;

	private ACLMessage message;

	/**
	 * Constructor.
	 * 
	 * @param message
	 *            the message to be processed.
	 */
	public MessageGoal(ACLMessage message) {
		this.message = message;
	}

	/**
	 * @return the message
	 */
	public ACLMessage getMessage() {
		return message;
	}

}
