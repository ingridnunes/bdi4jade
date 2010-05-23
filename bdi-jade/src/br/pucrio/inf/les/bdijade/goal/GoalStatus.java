/*
 * Created on 25/01/2010 21:55:33 
 */
package br.pucrio.inf.les.bdijade.goal;

import br.pucrio.inf.les.bdijade.core.Intention;


/**
 * This enumeration provides the possible status that a goal can have. This
 * status is determined by {@link Intention}.
 * 
 * @author ingrid
 */
public enum GoalStatus {

	ACHIEVED, NO_LONGER_DESIRED, PLAN_FAILED, TRYING_TO_ACHIEVE, UNACHIEVABLE, WAITING;

}
