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

package bdi4jade.examples.nestedcapabilities;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import bdi4jade.plan.Plan.EndState;
import bdi4jade.plan.PlanBody;

public class SuccessPlanBody extends PlanBody {

	private static final long serialVersionUID = -9039447524062487795L;

	private Log log;

	public void action() {
		log.info(this.getClass().getSimpleName() + " executed.");
		setEndState(EndState.SUCCESSFUL);
	}

	public void onStart() {
		this.log = LogFactory.getLog(this.getClass());
	}

}
