//----------------------------------------------------------------------------
// Copyright (C) 2011  Ingrid Nunes, et al.
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
// http://www.inf.puc-rio.br/~ionunes/
//
//----------------------------------------------------------------------------

package br.pucrio.inf.les.bdi4jade.examples.blocksworld.goal;

import br.pucrio.inf.les.bdi4jade.examples.blocksworld.domain.On;
import br.pucrio.inf.les.bdi4jade.goal.Goal;

/**
 * @author ingrid
 * 
 */
public class AchieveBlocksStacked implements Goal {

	private static final long serialVersionUID = -8126833927953226126L;

	private On[] target;

	public AchieveBlocksStacked(On[] target) {
		this.target = target;
	}

	public On[] getTarget() {
		return target;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("AchieveBlocksStacked: ");
		for (On on : target) {
			sb.append(on).append(" ");
		}
		return sb.toString();
	}

}
