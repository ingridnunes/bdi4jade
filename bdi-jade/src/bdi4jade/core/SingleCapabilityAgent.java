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

/**
 * This class is a BDIAgent that has a single capability.
 * 
 * @author Ingrid Nunes
 */
public class SingleCapabilityAgent extends AbstractBDIAgent {

	private static final long serialVersionUID = 6369037881807947402L;

	private Capability capability;

	/**
	 * Default constructor. Creates a new BDIAgent with a single capability.
	 */
	public SingleCapabilityAgent() {
		this(new Capability());
	}

	/**
	 * Creates a new BDIAgent with the given capability.
	 * 
	 * @param capability
	 *            the capability to be added to the agent.
	 */
	public SingleCapabilityAgent(Capability capability) {
		super();
		setCapability(capability);
	}

	/**
	 * Returns the capability of this agent.
	 * 
	 * @return the capability.
	 */
	public synchronized Capability getCapability() {
		return capability;
	}

	/**
	 * Sets the capability of this agent.
	 * 
	 * @param capability
	 *            the capability to set.
	 */
	public synchronized void setCapability(Capability capability) {
		if (this.capability != null) {
			this.removeCapability(this.capability);
		}
		this.addCapability(capability);
		this.capability = capability;
	}

}
