//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes, et al.
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
// http://www.inf.puc-rio.br/~ionunes/
//
//----------------------------------------------------------------------------

package br.pucrio.inf.les.bdi4jade.event;

import jade.content.AgentAction;
import br.pucrio.inf.les.bdi4jade.belief.Belief;

/**
 * This class represents an event performed over a belief.
 * 
 * @author ingridnunes
 */
public class BeliefEvent implements AgentAction {

	/**
	 * This enumeration represents the set of possible actions that can be
	 * performed over a belief.
	 * 
	 * @author ingrid
	 */
	public enum Action {
		BELIEF_ADDED, BELIEF_REMOVED, BELIEF_UPDATED
	}

	private static final long serialVersionUID = 1749139390567331926L;

	private Action action;
	private Object args;
	private Belief<?> belief;

	/**
	 * Default constructor.
	 */
	public BeliefEvent() {
		this(null);
	}

	/**
	 * Creates a Belief Changed.
	 * 
	 * @param belief
	 *            the belief that has changed.
	 */
	public BeliefEvent(Belief<?> belief) {
		this(belief, Action.BELIEF_UPDATED);
	}

	/**
	 * Creates a Belief Changed.
	 * 
	 * @param belief
	 *            the belief that has changed.
	 * @param action
	 *            the action performed.
	 */
	public BeliefEvent(Belief<?> belief, Action action) {
		this(belief, action, null);
	}

	/**
	 * Creates a Belief Changed.
	 * 
	 * @param belief
	 *            the belief that has changed.
	 * @param action
	 *            the action performed.
	 * @param args
	 *            an argument passed for this action.
	 */
	public BeliefEvent(Belief<?> belief, Action action, Object args) {
		this.belief = belief;
		this.action = action;
		this.args = args;
	}

	/**
	 * @return the action
	 */
	public Action getAction() {
		return action;
	}

	/**
	 * @return the args
	 */
	public Object getArgs() {
		return args;
	}

	/**
	 * @return the belief
	 */
	public Belief<?> getBelief() {
		return belief;
	}

	/**
	 * @param action
	 *            the action to set
	 */
	public void setAction(Action action) {
		this.action = action;
	}

	/**
	 * @param args
	 *            the args to set
	 */
	public void setArgs(Object args) {
		this.args = args;
	}

	/**
	 * @param belief
	 *            the belief to set
	 */
	public void setBelief(Belief<?> belief) {
		this.belief = belief;
	}

}
