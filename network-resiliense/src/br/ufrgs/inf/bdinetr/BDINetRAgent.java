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
package br.ufrgs.inf.bdinetr;

import bdi4jade.belief.Belief;
import bdi4jade.belief.TransientBelief;
import bdi4jade.core.Capability;
import bdi4jade.core.SingleCapabilityAgent;
import br.ufrgs.inf.bdinetr.domain.Device;

/**
 * @author Ingrid Nunes
 */
public class BDINetRAgent extends SingleCapabilityAgent {

	private static final long serialVersionUID = 6534875498063013722L;

	public static class RootCapability extends Capability {

		private static final long serialVersionUID = -2156730094556459899L;

		public static final String DEVICE_BELIEF = "device";

		@bdi4jade.annotation.Belief
		private Belief<String, Device> device = new TransientBelief<>(
				DEVICE_BELIEF);

		public RootCapability(Device device) {
			this.device.setValue(device);
		}

	}

	public BDINetRAgent(Device device) {
		this(device, new Capability[0]);
	}

	public BDINetRAgent(Device device, Capability capability) {
		this(device, new Capability[] { capability });
	}

	public BDINetRAgent(Device device, Capability[] capabilities) {
		super(new RootCapability(device));
		for (Capability capability : capabilities) {
			this.getCapability().addPartCapability(capability);
		}
	}

}
