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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package br.ufrgs.inf.bdi4jade.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.ufrgs.inf.bdi4jade.belief.Belief;
import br.ufrgs.inf.bdi4jade.event.BeliefEvent;
import br.ufrgs.inf.bdi4jade.event.BeliefEvent.Action;
import br.ufrgs.inf.bdi4jade.event.BeliefListener;
import br.ufrgs.inf.bdi4jade.exception.BeliefAlreadyExistsException;

/**
 * This class represents a belief base of a capability. It aggregates its
 * knowledge.
 * 
 * @author ingrid
 */
public class BeliefBase implements Serializable {

	private static final long serialVersionUID = -6411530721625492882L;

	private final Set<BeliefListener> beliefListeners;
	private final Map<String, Belief<?>> beliefs;
	private Capability capability;

	/**
	 * Creates a belief base.
	 */
	public BeliefBase() {
		this(null);
	}

	/**
	 * Creates a belief base associated with a capability and adds the beliefs
	 * in the provided belief set.
	 * 
	 * @param beliefs
	 *            the initial beliefs
	 */
	public BeliefBase(Set<Belief<?>> beliefs) {
		this.capability = null;
		this.beliefListeners = new HashSet<BeliefListener>();
		this.beliefs = new HashMap<String, Belief<?>>();
		if (beliefs != null) {
			for (Belief<?> belief : beliefs) {
				addBelief(belief);
			}
		}
	}

	/**
	 * Adds a belief to the belief base.
	 * 
	 * @param belief
	 *            the belief to be added.
	 */
	public void addBelief(Belief<?> belief) {
		if (!hasBelief(belief.getName())) {
			belief.addBeliefBase(this);
			this.beliefs.put(belief.getName(), belief);
			notifyBeliefChanged(new BeliefEvent(belief, Action.BELIEF_ADDED));
		} else {
			throw new BeliefAlreadyExistsException(belief);
		}
	}

	/**
	 * Adds a belief listener to be notified about changes in the belief base.
	 * 
	 * @param beliefListener
	 *            the listener to be added.
	 */
	public void addBeliefListener(BeliefListener beliefListener) {
		this.beliefListeners.add(beliefListener);
	}

	/**
	 * Adds a belief to the belief base. It overrides a belief, if it already
	 * exists.
	 * 
	 * @param belief
	 *            the belief to be added or updated.
	 */
	public void addOrUpdateBelief(Belief<?> belief) {
		if (hasBelief(belief.getName())) {
			updateBelief(belief.getName(), belief.getValue());
		} else {
			addBelief(belief);
		}
	}

	/**
	 * Gets all beliefs of this belief base and the belief bases of the parents
	 * of the capability that this belief base belongs to.
	 * 
	 * @return the beliefs
	 */
	public Collection<Belief<?>> getAllBeliefs() {
		Collection<Belief<?>> beliefs = new LinkedList<Belief<?>>();
		getAllBeliefs(beliefs);
		return beliefs;
	}

	private void getAllBeliefs(final Collection<Belief<?>> beliefs) {
		beliefs.addAll(this.beliefs.values());
		if (capability != null && capability.getParent() != null) {
			capability.getParent().getBeliefBase().getAllBeliefs(beliefs);
		}
	}

	/**
	 * Retrieves a belief from the belief base. If this belief does not contain
	 * it and this belief base is from a capability, it checks the common belief
	 * based of the agent, and returns it if it exists.
	 * 
	 * @param name
	 *            the name of the belief to be retrieved.
	 * @return the belief. Null if no belief is found.
	 */
	public Belief<?> getBelief(String name) {
		Belief<?> belief = this.beliefs.get(name);
		if (belief == null && capability != null
				&& capability.getParent() != null) {
			belief = capability.getParent().getBeliefBase().getBelief(name);
		}
		return belief;
	}

	/**
	 * @return the beliefListeners
	 */
	public Set<BeliefListener> getBeliefListeners() {
		return beliefListeners;
	}

	/**
	 * Gets all beliefs of this belief base.
	 * 
	 * @return the beliefs
	 */
	public Set<Belief<?>> getBeliefs() {
		Set<Belief<?>> beliefValues = new HashSet<Belief<?>>(beliefs.size());
		for (Belief<?> belief : beliefs.values())
			beliefValues.add(belief);
		return beliefValues;
	}

	/**
	 * Return a list of all belief values from this belief base.
	 * 
	 * @return the beliefValues
	 */
	public List<Object> getBeliefValues() {
		List<Object> beliefValues = new ArrayList<Object>(beliefs.size());
		for (Belief<?> belief : beliefs.values())
			beliefValues.add(belief.getValue());
		return beliefValues;
	}

	/**
	 * @return the capability
	 */
	public Capability getCapability() {
		return capability;
	}

	/**
	 * Checks if a belief is part of the belief base.
	 * 
	 * @param name
	 *            the belief to be checked
	 * @return true if the belief base contains the belief.
	 */
	public boolean hasBelief(String name) {
		boolean hasBelief = this.beliefs.containsKey(name);
		if (!hasBelief && capability != null && capability.getParent() != null) {
			hasBelief = capability.getParent().getBeliefBase().hasBelief(name);
		}
		return hasBelief;
	}

	/**
	 * Initialize the belief base, adding initial beliefs.
	 */
	protected void init() {
	}

	/**
	 * Notifies the capability associate with this BeliefBase that a belief was
	 * modified.
	 * 
	 * @param beliefChanged
	 *            the belief that was changed
	 */
	private void notifyBeliefChanged(BeliefEvent beliefChanged) {
		for (BeliefListener beliefListener : beliefListeners) {
			beliefListener.update(beliefChanged);
		}
		if (capability != null) {
			for (Capability child : capability.getChildren()) {
				child.getBeliefBase().notifyBeliefChanged(beliefChanged);
			}
		}
	}

	/**
	 * Removes a belief from the belief base.
	 * 
	 * @param name
	 *            the name of the belief to be removed.
	 * @return the belief was removed, null if it is not part of the belief
	 *         base.
	 */
	public Belief<?> removeBelief(String name) {
		Belief<?> belief = this.beliefs.remove(name);
		if (belief != null) {
			belief.removeBeliefBase(this);
			notifyBeliefChanged(new BeliefEvent(belief, Action.BELIEF_REMOVED));
		} else {
			if (capability != null && capability.getParent() != null) {
				belief = capability.getParent().getBeliefBase()
						.removeBelief(name);
			}
		}
		return belief;
	}

	/**
	 * Removes a belief listener.
	 * 
	 * @param beliefListener
	 *            the listener to be removed.
	 */
	public void removeBeliefListener(BeliefListener beliefListener) {
		this.beliefListeners.remove(beliefListener);
	}

	/**
	 * This method is an empty place holder for subclasses. It may be invoked to
	 * review beliefs from this belief base.
	 */
	public void reviewBeliefs() {

	}

	/**
	 * Sets the capability of this belief base. If the capability was already
	 * set, it throws a {@link RuntimeException}. After setting the capability,
	 * the {@link #init()} method is invoked.
	 * 
	 * @param capability
	 *            the capability to set
	 */
	public void setCapability(Capability capability) {
		if (this.capability != null) {
			throw new RuntimeException(
					"BeliefBase already binded to another capability!");
		}
		this.capability = capability;
		this.init();
	}

	/**
	 * Gets the size of this belief base (the number of beliefs).
	 * 
	 * @return the size of this belief base.
	 */
	public int size() {
		return this.beliefs.size();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return new StringBuffer("BeliefBase = ").append(this.getBeliefs())
				.toString();
	}

	/**
	 * Update the value of a belief in the belief base. In case the belief is
	 * not present in the belief base, nothing is performed and the method
	 * returns false. If the type of the new value being provided, it is still
	 * going to subscribe the previous value.
	 * 
	 * @param name
	 *            the belief to be updated.
	 * @param value
	 *            the new value to the belief.
	 * @return true if the belief was update.
	 */
	@SuppressWarnings("unchecked")
	public boolean updateBelief(String name, Object value) {
		Belief belief = this.beliefs.get(name);

		if (belief != null) {
			Object oldValue = belief.getValue();
			belief.setValue(value);
			notifyBeliefChanged(new BeliefEvent(belief, Action.BELIEF_UPDATED,
					oldValue));
			return true;
		} else if (capability != null && capability.getParent() != null) {
			return capability.getParent().getBeliefBase()
					.updateBelief(name, value);
		}

		return false;
	}

}
