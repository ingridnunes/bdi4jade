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

package bdi4jade.examples.blocksworld.goal;

import bdi4jade.examples.blocksworld.domain.Thing;
import bdi4jade.goal.Goal;

/**
 * @author ingrid
 * 
 */
public class PerformMove implements Goal {

	private static final long serialVersionUID = 8286023371969088149L;

	private Thing thing1;
	private Thing thing2;

	public PerformMove(Thing thing1, Thing thing2) {
		this.thing1 = thing1;
		this.thing2 = thing2;
	}

	public Thing getThing1() {
		return thing1;
	}

	public Thing getThing2() {
		return thing2;
	}

	@Override
	public String toString() {
		return "PerformMove: " + thing1 + " to " + thing2;
	}

}
