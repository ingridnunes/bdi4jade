/*
 * Created on 31/01/2010 17:54:22 
 */
package br.pucrio.inf.les.bdijade.goal;

/**
 * This interface defines the abstraction of a goal, as the {@link Goal}
 * interface. However, if an instance of the {@link InternalGoal} is dispatched
 * by a plan of a capability, this goal is going to be achieved (or tried to be)
 * by the capability that dispatched it.
 * 
 * @author ingrid
 */
public interface InternalGoal extends Goal {

}
