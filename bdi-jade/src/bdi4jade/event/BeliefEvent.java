//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
// 
// To contact the authors:
// http://inf.ufrgs.br/prosoft/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.event;

import jade.content.AgentAction;
import bdi4jade.belief.Belief;

/**
 * This class represents an event performed over a belief.
 * 
 * @author Ingrid Nunes
 */
public class BeliefEvent implements AgentAction {

	/**
	 * This enumeration represents the set of possible actions that can be
	 * performed over a belief.
	 * 
	 * @author Ingrid Nunes
	 */
	public enum Action {
		BELIEF_ADDED, BELIEF_REMOVED, BELIEF_SET_VALUE_ADDED, BELIEF_SET_VALUE_REMOVED, BELIEF_UPDATED
	}

	private static final long serialVersionUID = 1749139390567331926L;

	private Action action;
	private Object args;
	private Belief<?, ?> belief;

	/**
	 * Default constructor.
	 */
	public BeliefEvent() {
		this(null);
	}

	/**
	 * Creates a belief event.
	 * 
	 * @param belief
	 *            the belief over which the event has occurred.
	 */
	public BeliefEvent(Belief<?, ?> belief) {
		this(belief, Action.BELIEF_UPDATED);
	}

	/**
	 * Creates a belief event.
	 * 
	 * @param belief
	 *            the belief over which the event has occurred.
	 * @param action
	 *            the action performed.
	 */
	public BeliefEvent(Belief<?, ?> belief, Action action) {
		this(belief, action, null);
	}

	/**
	 * Creates a belief event.
	 * 
	 * @param belief
	 *            the belief over which the event has occurred.
	 * @param action
	 *            the action performed.
	 * @param args
	 *            an argument passed for this action.
	 */
	public BeliefEvent(Belief<?, ?> belief, Action action, Object args) {
		this.belief = belief;
		this.action = action;
		this.args = args;
	}

	/**
	 * Returns the action performed.
	 * 
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * Returns arguments associated with the action performed.
	 * 
	 * @return the args
	 */
	public Object getArgs() {
		return args;
	}

	/**
	 * Returns the belief over which the event has occurred.
	 * 
	 * @return the belief
	 */
	public Belief<?, ?> getBelief() {
		return belief;
	}

	/**
	 * Sets the action performed.
	 * 
	 * @param action
	 *            the action to set.
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * Sets the arguments associated with the action performed.
	 * 
	 * @param args
	 *            the args to set.
	 */
	public void setArgs(Object args) {
		this.args = args;
	}

	/**
	 * Sets the belief over which the event has occurred.
	 * 
	 * @param belief
	 *            the belief to set.
	 */
	public void setBelief(Belief<?, ?> belief) {
		this.belief = belief;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(getClass().getSimpleName()).append("\n");
		sb.append("Belief: ").append(belief).append("\n");
		sb.append("Action: ").append(action).append(" - ").append(args);
		return sb.toString();
	}

}
