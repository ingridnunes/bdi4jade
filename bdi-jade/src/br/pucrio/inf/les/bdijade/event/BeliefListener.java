/*
 * Created on 14/12/2009 09:56:43
 */
package br.pucrio.inf.les.bdijade.event;

import java.util.EventListener;

/**
 * This interface defined the method that a belief listener should implement. A
 * belief listener can be notified about changes in beliefs, when it subscribed
 * to a class that can notify updates.
 * 
 * @author ingridnunes
 */
public interface BeliefListener extends EventListener {

	/**
	 * Updates the listener according to a change in a belief.
	 * 
	 * @param beliefEvent
	 */
	public void update(BeliefEvent beliefEvent);

}
