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

package bdi4jade.core;

import java.util.Collection;

/**
 * This class is a BDIAgent that has multiple aggregated capabilities.
 * 
 * @author Ingrid Nunes
 */
public class MultipleCapabilityAgent extends AbstractBDIAgent {

	private static final long serialVersionUID = 6369037881807947402L;

	/**
	 * Default constructor.
	 */
	public MultipleCapabilityAgent() {

	}

	/**
	 * Creates a new BDIAgent with a single capability.
	 * 
	 * @param capability
	 *            the capability to be added to the agent.
	 */
	public MultipleCapabilityAgent(Capability capability) {
		super();
		this.addCapability(capability);
	}

	/**
	 * Creates a new BDIAgent with a set of capabilities.
	 * 
	 * @param capabilities
	 *            the capabilities to be added to the agent.
	 */
	public MultipleCapabilityAgent(Capability[] capabilities) {
		super();
		for (Capability capability : capabilities) {
			this.addCapability(capability);
		}
	}

	/**
	 * Creates a new BDIAgent with a set of capabilities.
	 * 
	 * @param capabilities
	 *            the capabilities to be added to the agent.
	 */
	public MultipleCapabilityAgent(Collection<Capability> capabilities) {
		super();
		for (Capability capability : capabilities) {
			this.addCapability(capability);
		}
	}

	/**
	 * @see bdi4jade.core.AbstractBDIAgent#addCapability(bdi4jade.core.Capability)
	 */
	@Override
	public final void addCapability(Capability capability) {
		super.addCapability(capability);
	}

	/**
	 * @see bdi4jade.core.AbstractBDIAgent#removeCapability(bdi4jade.core.Capability)
	 */
	@Override
	public final boolean removeCapability(Capability capability) {
		return super.removeCapability(capability);
	}

}
