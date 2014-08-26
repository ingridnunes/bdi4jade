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
package br.ufrgs.inf.bdinetr.domain;

/**
 * @author Ingrid Nunes
 */
public class Link {

	private Double bandwidth;
	private final String id;
	private Device source;
	private Device target;
	private Double usedBandwidth;

	public Link(String id) {
		this.id = id;
	}

	public Link(String id, Double bandwidth, Device source, Device target) {
		this(id);
		this.bandwidth = bandwidth;
		this.usedBandwidth = 0.0;
		setSource(source);
		setTarget(target);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Link) {
			Link l = (Link) obj;
			return this.id.equals(l.id);
		}
		return false;
	}

	public Double getBandwidth() {
		return bandwidth;
	}

	public Device getSource() {
		return source;
	}

	public Device getTarget() {
		return target;
	}

	public Double getUsedBandwidth() {
		return usedBandwidth;
	}

	public Double getUsedBandwidthPercentage() {
		return usedBandwidth / bandwidth;
	}

	@Override
	public int hashCode() {
		return id == null ? 0 : id.hashCode();
	}

	public void setBandwidth(Double bandwidth) {
		this.bandwidth = bandwidth;
	}

	public void setSource(Device source) {
		if (this.source != null) {
			this.source.disconnectLink(this);
		}
		this.source = source;
		this.source.connectLink(this);
	}

	public void setTarget(Device target) {
		if (this.target != null) {
			this.target.disconnectLink(this);
		}
		this.target = source;
		this.target.connectLink(this);
	}

	public void setUsedBandwidth(Double usedBandwidth) {
		this.usedBandwidth = usedBandwidth;
	}

	@Override
	public String toString() {
		return id;
	}

}
