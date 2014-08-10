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

package bdi4jade.belief;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bdi4jade.core.Capability;
import bdi4jade.event.BeliefEvent;
import bdi4jade.event.BeliefEvent.Action;
import bdi4jade.event.BeliefListener;
import bdi4jade.exception.BeliefAlreadyExistsException;

/**
 * This class represents a belief base of a capability. It aggregates its
 * knowledge.
 * 
 * @author Ingrid Nunes
 */
public final class BeliefBase implements Serializable {

	private static final long serialVersionUID = -6411530721625492882L;

	private final Set<BeliefListener> beliefListeners;
	private final Map<String, Belief<?>> beliefs;
	private Capability capability;

	/**
	 * The default constructor. It should be only used if persistence frameworks
	 * are used.
	 */
	protected BeliefBase() {
		this.beliefListeners = new HashSet<BeliefListener>();
		this.beliefs = new HashMap<String, Belief<?>>();
	}

	/**
	 * Creates a belief base associated with a capability.
	 * 
	 * @param capability
	 *            the capability to which this belief base belongs.
	 */
	public BeliefBase(final Capability capability) {
		this(capability, null);
	}

	/**
	 * Creates a belief base associated with a capability and adds the beliefs
	 * in the provided belief set as the initial beliefs of this belief base.
	 * 
	 * @param capability
	 *            the capability to which this belief base belongs.
	 * @param beliefs
	 *            the initial beliefs.
	 */
	public BeliefBase(final Capability capability, Set<Belief<?>> beliefs) {
		if (capability == null)
			throw new NullPointerException("Capability must be not null.");

		this.capability = capability;
		this.beliefListeners = new HashSet<BeliefListener>();
		this.beliefs = new HashMap<String, Belief<?>>();
		if (beliefs != null) {
			for (Belief<?> belief : beliefs) {
				this.beliefs.put(belief.getName(), belief);
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
	 * Adds a belief to the belief base. It updates the belief value, if it
	 * already exists.
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
	 * Gets all beliefs of this belief base and the belief bases of the
	 * whole-capabilities of the capability that this belief base belongs to.
	 * 
	 * @return the beliefs of this capability and all of its whole-capabilities.
	 */
	public Collection<Belief<?>> getAllBeliefs() {
		Collection<Belief<?>> beliefs = new LinkedList<Belief<?>>();
		getAllBeliefs(beliefs);
		return beliefs;
	}

	/**
	 * This is a recursive method to implement the {@link #getAllBeliefs()}
	 * method.
	 * 
	 * @param beliefs
	 *            the set to which beliefs are added.
	 */
	private void getAllBeliefs(final Collection<Belief<?>> beliefs) {
		beliefs.addAll(this.beliefs.values());
		if (capability.getWholeCapability() != null) {
			capability.getWholeCapability().getBeliefBase()
					.getAllBeliefs(beliefs);
		}
	}

	/**
	 * Retrieves a belief from the belief base. If this belief base does not
	 * contain it, the method checks whole-capabilities' belief base
	 * recursively.
	 * 
	 * @param name
	 *            the name of the belief to be retrieved.
	 * @return the belief, or null if no belief is found.
	 */
	public Belief<?> getBelief(String name) {
		Belief<?> belief = this.beliefs.get(name);
		if (belief == null && capability.getWholeCapability() != null) {
			belief = capability.getWholeCapability().getBeliefBase()
					.getBelief(name);
		}
		return belief;
	}

	/**
	 * Returns all the current belief listeners of this belief base.
	 * 
	 * @return the belief listeners.
	 */
	public Set<BeliefListener> getBeliefListeners() {
		return new HashSet<BeliefListener>(beliefListeners);
	}

	/**
	 * Gets all beliefs of this specific belief base.
	 * 
	 * @return the beliefs
	 */
	public Set<Belief<?>> getBeliefs() {
		return new HashSet<Belief<?>>(beliefs.values());
	}

	/**
	 * Returns a list of all belief values from this belief base.
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
	 * Returns the capability with which this belief base is associated.
	 * 
	 * @return the capability.
	 */
	public Capability getCapability() {
		return capability;
	}

	/**
	 * Checks whether a belief is part of the belief base. If this belief base
	 * does not contain it, the method checks whole-capabilities' belief base
	 * recursively.
	 * 
	 * @param name
	 *            the belief to be checked
	 * @return true if the belief base contains the belief.
	 */
	public boolean hasBelief(String name) {
		boolean hasBelief = this.beliefs.containsKey(name);
		if (!hasBelief && capability.getWholeCapability() != null) {
			hasBelief = capability.getWholeCapability().getBeliefBase()
					.hasBelief(name);
		}
		return hasBelief;
	}

	/**
	 * Notifies the capability associated with this belief base that a belief
	 * was modified. It also recursively notifies belief listeners of part
	 * capabilities.
	 * 
	 * @param beliefChanged
	 *            the belief that was changed
	 */
	protected void notifyBeliefChanged(BeliefEvent beliefChanged) {
		for (BeliefListener beliefListener : beliefListeners) {
			beliefListener.update(beliefChanged);
		}
		for (Capability part : capability.getPartCapabilities()) {
			part.getBeliefBase().notifyBeliefChanged(beliefChanged);
		}
	}

	/**
	 * Removes a belief from the belief base. If this belief base does not
	 * contain it, the method checks whole-capabilities' belief base recursively
	 * to remove this belief..
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
			if (capability.getWholeCapability() != null) {
				belief = capability.getWholeCapability().getBeliefBase()
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
	 * Associates a capability with this belief base. Ideally, the capability
	 * should be final and initialized in the constructor. This method should be
	 * only used if persistence frameworks are used.
	 * 
	 * @param capability
	 *            the capability to set.
	 */
	protected void setCapability(Capability capability) {
		this.capability = capability;
	}

	/**
	 * Gets the size of this specific belief base (the number of beliefs).
	 * 
	 * @return the size of this belief base.
	 */
	public int size() {
		return this.beliefs.size();
	}

	/**
	 * Returns this belief base as a string in the form:
	 * "Belief base of Capability ID = [ BELIEFS ]".
	 * 
	 * @return the string representation of this belief base.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("Belief base of Capability ");
		if (capability == null)
			sb.append(" NO ID");
		else
			sb.append(capability.getId());
		sb.append(" = ").append(beliefs);
		return sb.toString();
	}

	/**
	 * Updates the value of a belief in the belief base. In case the belief is
	 * not present in the belief base (of in its whole-capabilities' belief
	 * bases), nothing is performed and the method returns false. If the type of
	 * the new value being provided does not match the current type, the method
	 * still subscribes the previous value.
	 * 
	 * @param name
	 *            the belief to be updated.
	 * @param value
	 *            the new value to the belief.
	 * @return true if the belief was updated.
	 */
	@SuppressWarnings("unchecked")
	public boolean updateBelief(String name, Object value) {
		Belief belief = this.beliefs.get(name);
		if (belief != null) {
			belief.setValue(value);
			return true;
		} else if (capability.getWholeCapability() != null) {
			return capability.getWholeCapability().getBeliefBase()
					.updateBelief(name, value);
		}
		return false;
	}

}
