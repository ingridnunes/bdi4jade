/*
 * Created on 25/01/2010 21:57:43 
 */
package br.pucrio.inf.les.bdijade.event;

import java.util.EventListener;

/**
 * This interface defined the method that a goal listener should implement. A
 * goal listener can be notified about changes in goal, when it subscribed to a
 * class that can notify updates.
 * 
 * @author ingrid
 */
public interface GoalListener extends EventListener {

	/**
	 * Notifies the listener that the goal was performed.
	 * 
	 * @param event
	 *            the performed goal event.
	 */
	public void goalPerformed(GoalEvent event);

}
