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
// http://inf.ufrgs.br/~ingridnunes/bdi4jade/
//
//----------------------------------------------------------------------------

package bdi4jade.examples.compositegoal;

import java.util.Random;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.PlanBody;

/**
 * @author ingrid
 * 
 */
public class MyPlan extends PlanBody {

	private static final long serialVersionUID = -220345270457161508L;

	private Log log = LogFactory.getLog(this.getClass());

	public void action() {
		long random = new Random().nextLong();
		log.info("Random: " + random);
		if (random % 3 != 0)
			setEndState(EndState.SUCCESSFUL);
		else
			setEndState(EndState.FAILED);
		log.info(getGoal() + " Plan#" + getPlan().getId() + " EndState: "
				+ getEndState());
	}

}
