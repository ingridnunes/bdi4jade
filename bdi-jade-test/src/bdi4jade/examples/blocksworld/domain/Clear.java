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
package bdi4jade.examples.blocksworld.domain;

/**
 * @author Ingrid Nunes
 */
public class Clear {

	private Thing thing;

	public Clear(Thing thing) {
		this.thing = thing;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Clear) {
			Clear other = (Clear) obj;
			return this.thing.equals(other.thing);
		} else {
			return false;
		}
	}

	public Thing getThing() {
		return thing;
	}

	@Override
	public int hashCode() {
		return thing.hashCode();
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CLEAR");
		sb.append("_").append(thing);
		return sb.toString();
	}

}
