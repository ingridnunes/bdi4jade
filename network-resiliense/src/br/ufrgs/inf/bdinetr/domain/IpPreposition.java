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
public class IpPreposition {

	public static class Anomalous extends IpPreposition {
		public Anomalous(IpAddress ip) {
			super(ip);
		}
	}

	public static class Benign extends IpPreposition {
		public Benign(IpAddress ip) {
			super(ip);
		}
	}

	public static class FlowRecord extends IpPreposition {
		public FlowRecord(IpAddress ip) {
			super(ip);
		}
	}

	public static class RateLimited extends IpPreposition {
		public RateLimited(IpAddress ip) {
			super(ip);
		}
	}

	public static class Restricted extends IpPreposition {
		public Restricted(IpAddress ip) {
			super(ip);
		}
	}

	protected IpAddress ip;

	public IpPreposition(IpAddress ip) {
		this.ip = ip;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().equals(obj.getClass())) {
			IpPreposition lp = (IpPreposition) obj;
			return this.ip.equals(lp.ip);
		}
		return false;
	}

	public IpAddress getIp() {
		return ip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.getClass() == null) ? 0 : this.getClass().hashCode());
		result = prime * result + ((ip == null) ? 0 : ip.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append("(").append(ip).append(")");
		return sb.toString();
	}

}
