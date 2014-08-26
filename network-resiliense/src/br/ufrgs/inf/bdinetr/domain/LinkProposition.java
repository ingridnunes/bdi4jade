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
public class LinkProposition {

	public static class AttackPrevented extends LinkProposition {
		public AttackPrevented(Link link) {
			super(link);
		}
	}

	public static class FullyOperational extends LinkProposition {
		public FullyOperational(Link link) {
			super(link);
		}
	}

	public static class OverUsage extends LinkProposition {
		public OverUsage(Link link) {
			super(link);
		}
	}

	public static class RegularUsage extends LinkProposition {
		public RegularUsage(Link link) {
			super(link);
		}
	}

	public static class Usage extends LinkProposition {
		public Usage(Link link) {
			super(link);
		}
	}

	protected Link link;

	public LinkProposition(Link link) {
		this.link = link;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().equals(obj.getClass())) {
			LinkProposition lp = (LinkProposition) obj;
			return this.link.equals(lp.link);
		}
		return false;
	}

	public Link getLink() {
		return link;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.getClass() == null) ? 0 : this.getClass().hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		return result;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append(this.getClass().getSimpleName());
		sb.append("(").append(link).append(")");
		return sb.toString();
	}

}
