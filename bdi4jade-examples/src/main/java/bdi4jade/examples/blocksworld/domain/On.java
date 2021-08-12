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
public class On {

	private Thing thing1;
	private Thing thing2;

	public On(Thing thing1, Thing thing2) {
		this.thing1 = thing1;
		this.thing2 = thing2;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof On) {
			On other = (On) obj;
			return this.thing1.equals(other.thing1)
					&& this.thing2.equals(other.thing2);
		} else {
			return false;
		}
	}

	public Thing getThing1() {
		return thing1;
	}

	public Thing getThing2() {
		return thing2;
	}

	@Override
	public int hashCode() {
		return (int) Math.pow(thing1.hashCode(), thing2.hashCode());
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("ON");
		sb.append("_").append(thing1);
		sb.append("_").append(thing2);
		return sb.toString();
	}

}
